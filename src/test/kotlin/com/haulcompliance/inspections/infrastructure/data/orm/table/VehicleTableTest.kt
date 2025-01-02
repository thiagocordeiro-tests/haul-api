package com.haulcompliance.inspections.infrastructure.data.orm.table

import com.haulcompliance.inspections.testing.fixtures.VehicleSpawner
import io.mockk.mockk
import io.tcds.orm.MapOrmResultSet
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class VehicleTableTest {
    private val table = VehicleTable(mockk(relaxed = true))

    private val row = mapOf(
        "id" to "TC12345",
        "type" to "Truck Tractor",
        "license_number" to "TRUCK-C12345",
        "license_state" to "NY",
    )

    private val entity = VehicleSpawner.truckTractor()

    @Test
    fun `given a row then map to entity`() = Assertions.assertEquals(entity, table.entry(MapOrmResultSet(row)))

    @Test
    fun `given an entity then map to a row`() = Assertions.assertEquals(row, table.values(entity))
}
