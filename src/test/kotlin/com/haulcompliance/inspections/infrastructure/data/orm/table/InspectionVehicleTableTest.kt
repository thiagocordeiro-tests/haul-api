package com.haulcompliance.inspections.infrastructure.data.orm.table

import io.mockk.mockk
import io.tcds.orm.MapOrmResultSet
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class InspectionVehicleTableTest {
    private val table = InspectionVehicleTable(mockk(relaxed = true))

    private val row = mapOf(
        "inspection_id" to "id-inspection",
        "vehicle_id" to "id-vehicle",
        "unit" to 1,
    )

    private val entry = InspectionVehicleTable.Model(
        inspectionId = "id-inspection",
        vehicleId = "id-vehicle",
        unit = 1,
    )

    @Test
    fun `given a row then map to entry`() = Assertions.assertEquals(entry, table.entry(MapOrmResultSet(row)))

    @Test
    fun `given an entry then map to a row`() = Assertions.assertEquals(row, table.values(entry))
}
