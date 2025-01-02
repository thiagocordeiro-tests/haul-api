package com.haulcompliance.inspections.application.inspection.get

import com.haulcompliance.inspections.domain.inspection.Inspections
import com.haulcompliance.inspections.testing.fixtures.InspectionSpawner
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.tcds.orm.testing.assertObjects
import org.junit.jupiter.api.Test

class GetInspectionsQueryHandlerTest {
    private val inspections: Inspections = mockk()

    private val handler = GetInspectionsQueryHandler(inspections)
    private val query = GetInspectionsQuery(id = "id-truck-tractor-inspection")

    @Test
    fun `load inspection by id`() {
        every { inspections.loadById(any()) } returns InspectionSpawner.truckTractor()

        val inspection = handler.handle(query)

        assertObjects(InspectionSpawner.truckTractor(), inspection)
        verify(exactly = 1) { inspections.loadById("id-truck-tractor-inspection") }
    }
}
