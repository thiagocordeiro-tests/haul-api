package com.haulcompliance.inspections.infrastructure.data.orm.table

import com.haulcompliance.inspections.domain.inspection.Inspection
import com.haulcompliance.inspections.domain.vehicle.Vehicles
import io.tcds.orm.EntityTable
import io.tcds.orm.OrmResultSet
import io.tcds.orm.connection.Connection
import io.tcds.orm.extension.*

@Suppress("MemberVisibilityCanBePrivate")
class InspectionTable(
    connection: Connection,
    private val inspectionVehicleTable: InspectionVehicleTable,
    private val violationTable: ViolationTable,
    private val vehicles: Vehicles,
) : EntityTable<Inspection, String>(
    connection,
    "inspections",
) {
    override val id = varchar("id") { it.id }
    val state = varchar("state") { it.state }
    val level = integer("level") { it.level }
    val timeWeight = integer("time_weight") { it.timeWeight }
    val placarableHazmatInspection = bool("placarable_hazmat_inspection") { it.placarableHazmatInspection }
    val hazmatInspection = bool("hazmat_inspection") { it.hazmatInspection }
    val status = enum("status") { it.status }
    val inspectedAt = date("inspected_at") { it.inspectedAt.toInstant() }
    val importedAt = date("imported_at") { it.importedAt.toInstant() }

    override fun entry(row: OrmResultSet): Inspection {
        return row.get(id).let { id ->
            Inspection(
                id = id,
                state = row.get(state),
                level = row.get(level),
                timeWeight = row.get(timeWeight),
                placarableHazmatInspection = row.get(placarableHazmatInspection),
                hazmatInspection = row.get(hazmatInspection),
                inspectedAt = row.get(inspectedAt).toLocalDate(),
                status = row.get(status),
                vehicles = lazy {
                    inspectionVehicleTable.findBy(
                        where(inspectionVehicleTable.inspectionId equalsTo id),
                    ).map {
                        Inspection.InspectedVehicle(
                            unit = it.unit,
                            vehicle = vehicles.loadById(it.vehicleId),
                        )
                    }.toList()
                },
                violations = lazy {
                    violationTable.findBy(
                        where(violationTable.inspectionId equalsTo id),
                    ).map {
                        it.violation
                    }.toList()
                },
                importedAt = row.get(importedAt).toLocalDateTime(),
            )
        }
    }
}
