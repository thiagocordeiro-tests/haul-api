package com.haulcompliance.inspections.infrastructure.data.orm.table

import com.haulcompliance.inspections.infrastructure.data.orm.table.InspectionVehicleTable.Model
import io.tcds.orm.OrmResultSet
import io.tcds.orm.Table
import io.tcds.orm.connection.Connection
import io.tcds.orm.extension.integer
import io.tcds.orm.extension.varchar

@Suppress("MemberVisibilityCanBePrivate")
class InspectionVehicleTable(
    connection: Connection,
) : Table<Model>(
    connection,
    "inspection_vehicles",
) {
    data class Model(val inspectionId: String, val vehicleId: String, val unit: Int)

    val inspectionId = varchar("inspection_id") { it.inspectionId }
    val vehicleId = varchar("vehicle_id") { it.vehicleId }
    val unit = integer("unit") { it.unit }

    override fun entry(row: OrmResultSet): Model = Model(
        inspectionId = row.get(inspectionId),
        vehicleId = row.get(vehicleId),
        unit = row.get(unit),
    )
}
