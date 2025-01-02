package com.haulcompliance.inspections.application.inspection.list

import com.haulcompliance.inspections.domain.EntryList
import com.haulcompliance.inspections.domain.inspection.Inspection
import com.haulcompliance.inspections.domain.inspection.InspectionFilter
import com.haulcompliance.inspections.domain.inspection.Inspections
import com.haulcompliance.inspections.testing.fixtures.InspectionSpawner
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.tcds.orm.testing.assertObjects
import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.time.LocalDateTime

class ListInspectionsQueryHandlerTest {
    private val inspections: Inspections = mockk()

    private val handler = ListInspectionsQueryHandler(inspections)
    private val query = ListInspectionsQuery(
        InspectionFilter(
            basic = "Vehicle Maint.",
            status = Inspection.Status.Unresolved,
            page = 10,
            entriesPerPage = 2,
            order = InspectionFilter.Order.ASC,
            orderBy = InspectionFilter.OrderBy.InspectionNumber,
        ),
    )

    @Test
    fun `list inspections from repository`() {
        every { inspections.list(any()) } returns EntryList(
            total = 2,
            entries = listOf(
                InspectionSpawner.truckTractor(),
                InspectionSpawner.straightTruck(),
            ),
        )

        val list = handler.handle(query)

        assertObjects(
            EntryList(
                total = 2,
                entries = listOf(
                    ListInspectionsQuery.Response(
                        id = "id-truck-tractor-inspection",
                        inspectedAt = LocalDate.parse("2023-08-25"),
                        status = Inspection.Status.Unresolved,
                        plate = "TRUCK-C12345",
                        violation = "Vehicle Maint.",
                        weight = 1,
                        importedAt = LocalDateTime.parse("2025-01-02T15:15:15"),
                    ),
                    ListInspectionsQuery.Response(
                        id = "id-straight-truck-inspection",
                        inspectedAt = LocalDate.parse("2023-10-01"),
                        status = Inspection.Status.Unresolved,
                        plate = "STRAIGHT-T981234",
                        violation = "Vehicle Maint.",
                        weight = 1,
                        importedAt = LocalDateTime.parse("2025-01-02T15:15:15"),
                    ),
                ),
            ),
            list,
        )
        verify(exactly = 1) {
            inspections.list(
                InspectionFilter(
                    basic = "Vehicle Maint.",
                    status = Inspection.Status.Unresolved,
                    page = 10,
                    entriesPerPage = 2,
                    order = InspectionFilter.Order.ASC,
                    orderBy = InspectionFilter.OrderBy.InspectionNumber,
                ),
            )
        }
    }
}
