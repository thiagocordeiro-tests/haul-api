package com.haulcompliance.inspections.infrastructure.data.orm.table

import com.haulcompliance.inspections.domain.vehicle.Vehicle
import com.haulcompliance.inspections.domain.vehicle.Vehicle.License
import io.tcds.orm.EntityTable
import io.tcds.orm.OrmResultSet
import io.tcds.orm.connection.Connection
import io.tcds.orm.extension.varchar

@Suppress("MemberVisibilityCanBePrivate")
class VehicleTable(
    connection: Connection,
) : EntityTable<Vehicle, String>(
    connection,
    "vehicles",
) {
    override val id = varchar("id") { it.id }
    val type = varchar("type") { it.type }
    val licenseNumber = varchar("license_number") { it.license.number }
    val licenseState = varchar("license_state") { it.license.state }

    override fun entry(row: OrmResultSet): Vehicle = Vehicle(
        id = row.get(id),
        type = row.get(type),
        license = License(
            number = row.get(licenseNumber),
            state = row.get(licenseState),
        ),
    )
}
