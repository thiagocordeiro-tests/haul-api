package com.haulcompliance.inspections.infrastructure.data.orm

import com.haulcompliance.inspections.domain.EntityNotFound
import com.haulcompliance.inspections.infrastructure.data.orm.table.VehicleTable
import com.haulcompliance.inspections.testing.fixtures.VehicleSpawner
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.mockk.verifySequence
import io.tcds.orm.testing.assertObjects
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class OrmVehiclesTest {
    private val table: VehicleTable = mockk()
    private val vehicles = OrmVehicles(chunkSize = 1, table = table)

    @Test
    fun `given a list of vehicles then insert in chunks`() {
        every { table.bulkInsertIgnore(any()) } returns mockk(relaxed = true)

        vehicles.store(
            listOf(
                VehicleSpawner.truckTractor(),
                VehicleSpawner.semiTrailer(),
            ),
        )

        verifySequence {
            table.bulkInsertIgnore(listOf(VehicleSpawner.truckTractor()))
            table.bulkInsertIgnore(listOf(VehicleSpawner.semiTrailer()))
        }
    }

    @Test
    fun `given an id when table returns null then throw entity not found`() {
        every { table.loadById(any()) } returns null

        val exception = assertThrows<EntityNotFound> { vehicles.loadById("id-another-truck-tractor") }

        Assertions.assertEquals("Vehicles with id <id-another-truck-tractor> not found", exception.message)
        verify(exactly = 1) { table.loadById("id-another-truck-tractor") }
    }

    @Test
    fun `given an id when table returns an entry then return the same entry`() {
        every { table.loadById(any()) } returns VehicleSpawner.semiTrailer()

        val entry = vehicles.loadById("id-another-truck-tractor")

        assertObjects(VehicleSpawner.semiTrailer(), entry)
    }
}
