package com.haulcompliance.inspections.infrastructure.data.orm.table

import com.haulcompliance.inspections.testing.fixtures.ViolationSpawner
import io.mockk.mockk
import io.tcds.orm.MapOrmResultSet
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class ViolationTableTest {
    private val table = ViolationTable(mockk(relaxed = true))

    private val row = mapOf(
        "inspection_id" to "id-inoperable-tail-inspection",
        "code" to "393.9T",
        "description" to "Inoperable tail lamp",
        "oos" to false,
        "time_severity_weight" to 6,
        "basic" to "Vehicle Maint.",
        "unit" to 1,
        "convicted_of_dif_charge" to true,
    )

    private val model = ViolationTable.Model(
        inspectionId = "id-inoperable-tail-inspection",
        violation = ViolationSpawner.inoperableTail(),
    )

    @Test
    fun `given a row then map to model`() = Assertions.assertEquals(model, table.entry(MapOrmResultSet(row)))

    @Test
    fun `given an model then map to a row`() = Assertions.assertEquals(row, table.values(model))
}
