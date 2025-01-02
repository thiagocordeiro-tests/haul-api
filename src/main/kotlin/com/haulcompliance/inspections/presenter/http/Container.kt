package com.haulcompliance.inspections.presenter.http

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.xml.XmlMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import com.haulcompliance.inspections.application.inspection.get.GetInspectionsQueryHandler
import com.haulcompliance.inspections.application.inspection.import.ImportInspectionReportCommandHandler
import com.haulcompliance.inspections.application.inspection.list.ListInspectionsQueryHandler
import com.haulcompliance.inspections.domain.inspection.Inspections
import com.haulcompliance.inspections.domain.inspection.ReportParser
import com.haulcompliance.inspections.domain.vehicle.Vehicles
import com.haulcompliance.inspections.domain.vehicle.crash.Crashes
import com.haulcompliance.inspections.infrastructure.data.api.fmcsa.FmcsaXmlReportParser
import com.haulcompliance.inspections.infrastructure.data.orm.OrmCrashes
import com.haulcompliance.inspections.infrastructure.data.orm.OrmInspections
import com.haulcompliance.inspections.infrastructure.data.orm.OrmVehicles
import com.haulcompliance.inspections.infrastructure.data.orm.table.*
import io.tcds.orm.connection.NestedTransactionConnection
import io.tcds.orm.connection.ReconnectableConnection
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.sql.DriverManager
import java.text.SimpleDateFormat

fun ObjectMapper.setup() {
    registerKotlinModule()
    registerModule(JavaTimeModule())
    configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
    dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
}

val xmlMapper: ObjectMapper = XmlMapper().apply { setup() }
val jsonMapper: ObjectMapper = jacksonObjectMapper().apply { setup() }

/**
 * Ideally it should be replaced by a proper dependency container
 */
object Container {
    // Framework
    val logger: Logger = LoggerFactory.getLogger("application")

    // ORM
    val connection = NestedTransactionConnection(
        readOnly = ReconnectableConnection { DriverManager.getConnection(System.getenv("JDBC_READ_URL")) },
        readWrite = ReconnectableConnection { DriverManager.getConnection(System.getenv("JDBC_WRITE_URL")) },
        logger = null,
    )
    private val violationTable = ViolationTable(connection)
    private val inspectionVehicleTable = InspectionVehicleTable(connection)
    private val vehicleTable = VehicleTable(connection)

    val vehicles: Vehicles = OrmVehicles(chunkSize = 1000, table = vehicleTable)
    val inspections: Inspections = OrmInspections(
        chunkSize = 1000,
        connection = connection,
        inspectionTable = InspectionTable(
            connection = connection,
            inspectionVehicleTable = inspectionVehicleTable,
            violationTable = violationTable,
            vehicles = vehicles,
        ),
        inspectionVehicleTable = inspectionVehicleTable,
        violationTable = violationTable,
    )
    val crashes: Crashes = OrmCrashes(chunkSize = 1000, table = CrashTable(connection, vehicles))

    // FMCSA
    val parser: ReportParser = FmcsaXmlReportParser(xmlMapper)

    // Command/Query
    val importInspectionsCommandHandler = ImportInspectionReportCommandHandler(parser, vehicles, inspections, crashes)
    val listInspectionsQueryHandler = ListInspectionsQueryHandler(inspections)
    val getInspectionQueryHandler = GetInspectionsQueryHandler(inspections)
}
