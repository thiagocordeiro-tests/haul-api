package com.haulcompliance.inspections.infrastructure.data.orm

import com.haulcompliance.inspections.domain.EntityNotFound
import com.haulcompliance.inspections.domain.vehicle.Vehicle
import com.haulcompliance.inspections.domain.vehicle.Vehicles
import com.haulcompliance.inspections.infrastructure.data.orm.table.VehicleTable

class OrmVehicles(
    private val chunkSize: Int,
    private val table: VehicleTable,
) : Vehicles {
    override fun store(vehicles: Iterable<Vehicle>) {
        vehicles
            .chunked(chunkSize)
            .forEach { chunk -> table.bulkInsertIgnore(chunk) }
    }

    override fun loadById(id: String): Vehicle {
        return table.loadById(id) ?: throw EntityNotFound("Vehicles with id <$id> not found")
    }
}
