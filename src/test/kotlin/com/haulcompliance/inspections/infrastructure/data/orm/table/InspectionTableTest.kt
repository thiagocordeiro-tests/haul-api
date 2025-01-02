package com.haulcompliance.inspections.infrastructure.data.orm.table

import com.haulcompliance.inspections.domain.vehicle.Vehicles
import com.haulcompliance.inspections.testing.fixtures.InspectionSpawner
import com.haulcompliance.inspections.testing.fixtures.VehicleSpawner
import com.haulcompliance.inspections.testing.fixtures.ViolationSpawner
import io.mockk.every
import io.mockk.mockk
import io.tcds.orm.MapOrmResultSet
import io.tcds.orm.testing.assertObjects
import org.junit.jupiter.api.Test

class InspectionTableTest {
    private val inspectionVehicleTable: InspectionVehicleTable = mockk()
    private val violationTable: ViolationTable = mockk()
    private val vehicles: Vehicles = mockk()

    private val table = InspectionTable(
        connection = mockk(relaxed = true),
        inspectionVehicleTable = inspectionVehicleTable,
        violationTable = violationTable,
        vehicles = vehicles,
    )

    private val row = mapOf(
        "id" to "id-truck-tractor-inspection",
        "state" to "TX",
        "level" to 1,
        "time_weight" to 1,
        "placarable_hazmat_inspection" to false,
        "hazmat_inspection" to false,
        "status" to "Unresolved",
        "inspected_at" to "2023-08-25T00:00:00Z",
        "imported_at" to "2025-01-02T15:15:15Z",
    )

    private val entity = InspectionSpawner.truckTractor()
    private val inspectedVehicles = sequenceOf(
        InspectionVehicleTable.Model("id-truck-tractor-inspection", "id-truck-tractor", 1),
        InspectionVehicleTable.Model("id-truck-tractor-inspection", "id-semi-trailer", 2),
    )
    private val violations = sequenceOf(
        ViolationTable.Model("id-truck-tractor-inspection", ViolationSpawner.inoperableTail()),
        ViolationTable.Model("id-truck-tractor-inspection", ViolationSpawner.breakTubing()),
    )

    @Test
    fun `given a row then map to entity`() {
        every { vehicles.loadById(any()) } returnsMany listOf(VehicleSpawner.truckTractor(), VehicleSpawner.semiTrailer())
        ViolationTable(mockk()).let { every { violationTable.inspectionId } returns it.inspectionId }
        InspectionVehicleTable(mockk()).let { every { inspectionVehicleTable.inspectionId } returns it.inspectionId }
        every { inspectionVehicleTable.findBy(any()) } returns inspectedVehicles
        every { violationTable.findBy(any()) } returns violations

        val result = table.entry(MapOrmResultSet(row))

        assertObjects(entity, result)
    }

    @Test
    fun `given an entity then map to a row`() = assertObjects(row, table.values(entity))
}
