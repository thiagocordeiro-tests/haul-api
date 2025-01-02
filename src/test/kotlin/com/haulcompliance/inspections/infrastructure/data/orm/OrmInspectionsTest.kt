package com.haulcompliance.inspections.infrastructure.data.orm

import com.haulcompliance.inspections.domain.EntityNotFound
import com.haulcompliance.inspections.domain.EntryList
import com.haulcompliance.inspections.domain.inspection.Inspection
import com.haulcompliance.inspections.domain.inspection.InspectionFilter
import com.haulcompliance.inspections.infrastructure.data.orm.table.InspectionTable
import com.haulcompliance.inspections.infrastructure.data.orm.table.InspectionVehicleTable
import com.haulcompliance.inspections.infrastructure.data.orm.table.ViolationTable
import com.haulcompliance.inspections.testing.fixtures.InspectionSpawner
import com.haulcompliance.inspections.testing.fixtures.VehicleSpawner
import com.haulcompliance.inspections.testing.fixtures.ViolationSpawner
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.mockk.verifySequence
import io.tcds.orm.MapOrmResultSet
import io.tcds.orm.connection.Connection
import io.tcds.orm.extension.param
import io.tcds.orm.testing.assertObjects
import io.tcds.orm.testing.matchObject
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class OrmInspectionsTest {
    private val connection: Connection = mockk()
    private val inspectionTable: InspectionTable = mockk()
    private val inspectionVehicleTable: InspectionVehicleTable = mockk()
    private val violationTable: ViolationTable = mockk()

    private val repository = OrmInspections(1, connection, inspectionTable, inspectionVehicleTable, violationTable)

    init {
        InspectionTable(mockk(relaxed = true), mockk(relaxed = true), mockk(relaxed = true), mockk(relaxed = true)).let {
            every { inspectionTable.inspectedAt } returns it.inspectedAt
            every { inspectionTable.status } returns it.status
        }
    }

    @Test
    fun `given a list of inspections then insert the inspections the related vehicles and violations in chunks`() {
        every { inspectionTable.bulkInsertIgnore(any()) } returns mockk(relaxed = true)
        every { inspectionVehicleTable.bulkInsertIgnore(any()) } returns mockk(relaxed = true)
        every { violationTable.bulkInsertIgnore(any()) } returns mockk(relaxed = true)

        repository.store(
            listOf(
                InspectionSpawner.truckTractor(),
                InspectionSpawner.straightTruck(),
            ),
        )

        verifySequence {
            inspectionTable.bulkInsertIgnore(matchObject { listOf(InspectionSpawner.truckTractor()) })
            inspectionTable.bulkInsertIgnore(matchObject { listOf(InspectionSpawner.straightTruck()) })
        }
        verifySequence {
            inspectionVehicleTable.bulkInsertIgnore(
                matchObject {
                    listOf(
                        InspectionVehicleTable.Model(InspectionSpawner.truckTractor().id, VehicleSpawner.truckTractor().id, 1),
                        InspectionVehicleTable.Model(InspectionSpawner.truckTractor().id, VehicleSpawner.semiTrailer().id, 2),
                    )
                },
            )
            inspectionVehicleTable.bulkInsertIgnore(
                matchObject {
                    listOf(
                        InspectionVehicleTable.Model(InspectionSpawner.straightTruck().id, VehicleSpawner.straightTruck().id, 1),
                    )
                },
            )
        }
        verifySequence {
            violationTable.bulkInsertIgnore(
                matchObject {
                    listOf(
                        ViolationTable.Model(InspectionSpawner.truckTractor().id, ViolationSpawner.inoperableTail()),
                        ViolationTable.Model(InspectionSpawner.truckTractor().id, ViolationSpawner.breakTubing()),
                    )
                },
            )
            violationTable.bulkInsertIgnore(
                matchObject {
                    listOf(
                        ViolationTable.Model(InspectionSpawner.straightTruck().id, ViolationSpawner.breakTubing()),
                    )
                },
            )
        }
    }

    @Test
    fun `given an id when table does not return an entry then throw not found`() {
        every { inspectionTable.loadById(any()) } returns null

        val exception = assertThrows<EntityNotFound> { repository.loadById("foo-bar") }

        Assertions.assertEquals("Inspection with id <foo-bar> not found", exception.message)
        verify(exactly = 1) { inspectionTable.loadById("foo-bar") }
    }

    @Test
    fun `given an id when table returns an entry then return the same entry`() {
        val inspection = InspectionSpawner.truckTractor()
        every { inspectionTable.loadById(any()) } returns inspection

        val loaded = repository.loadById("id-truck-tractor-inspection")

        Assertions.assertSame(inspection, loaded)
        verify(exactly = 1) { inspectionTable.loadById("id-truck-tractor-inspection") }
    }

    @Test
    fun `list entries without basic filter`() {
        every { connection.read(any(), any()) } returns sequenceOf(MapOrmResultSet(mapOf("total" to "2")))
        every { inspectionTable.findByQuery(any(), any(), any()) } returns sequenceOf(
            InspectionSpawner.truckTractor(),
            InspectionSpawner.straightTruck(),
        )

        val list = repository.list(
            InspectionFilter(
                basic = null,
                status = null,
                page = 1,
                entriesPerPage = 10,
                order = InspectionFilter.Order.ASC,
                orderBy = InspectionFilter.OrderBy.InspectionNumber,
            ),
        )

        assertObjects(
            EntryList(
                total = 2,
                entries = listOf(InspectionSpawner.truckTractor(), InspectionSpawner.straightTruck()),
            ),
            list,
        )
        verify(exactly = 1) {
            connection.read(
                "SELECT count(*) as total FROM inspections",
                emptyList(),
            )
        }
        verify(exactly = 1) {
            inspectionTable.findByQuery(
                "SELECT * FROM inspections ORDER BY id ASC LIMIT ? OFFSET ?",
                param(10),
                param(0),
            )
        }
    }

    @Test
    fun `list entries with basic filter`() {
        every { connection.read(any(), any()) } returns sequenceOf(MapOrmResultSet(mapOf("total" to "2")))
        every { inspectionTable.findByQuery(any(), any(), any(), any(), any()) } returns sequenceOf(
            InspectionSpawner.truckTractor(),
            InspectionSpawner.straightTruck(),
        )

        val list = repository.list(
            InspectionFilter(
                basic = "Vehicle Maint.",
                status = Inspection.Status.Unresolved,
                page = 4,
                entriesPerPage = 10,
                order = InspectionFilter.Order.ASC,
                orderBy = InspectionFilter.OrderBy.InspectionNumber,
            ),
        )

        assertObjects(
            EntryList(
                total = 2,
                entries = listOf(InspectionSpawner.truckTractor(), InspectionSpawner.straightTruck()),
            ),
            list,
        )
        verify(exactly = 1) {
            connection.read(
                """
                SELECT count(*) as total FROM inspections WHERE id IN (
                SELECT inspection_id FROM violations WHERE inspection_id = inspections.id AND basic LIKE ?
                ) AND status = ?
                """.trimIndent().replace("\n", ""),
                listOf(
                    param("Vehicle Maint."),
                    param(Inspection.Status.Unresolved, "status"),
                ),
            )
        }
        verify(exactly = 1) {
            inspectionTable.findByQuery(
                """
                SELECT * FROM inspections WHERE id IN (
                SELECT inspection_id FROM violations WHERE inspection_id = inspections.id AND basic LIKE ?
                ) AND status = ? ORDER BY id ASC LIMIT ? OFFSET ?
                """.trimIndent().replace("\n", ""),
                param("Vehicle Maint."),
                param(Inspection.Status.Unresolved, "status"),
                param(10),
                param(30),
            )
        }
    }
}
