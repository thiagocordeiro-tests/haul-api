package com.haulcompliance.inspections.infrastructure.data.orm.table

import com.haulcompliance.inspections.domain.vehicle.Vehicles
import com.haulcompliance.inspections.testing.fixtures.CrashSpawner
import com.haulcompliance.inspections.testing.fixtures.VehicleSpawner
import io.mockk.every
import io.mockk.mockk
import io.tcds.orm.MapOrmResultSet
import io.tcds.orm.testing.assertObjects
import org.junit.jupiter.api.Test

class CrashTableTest {
    private val vehicles: Vehicles = mockk()
    private val table = CrashTable(mockk(relaxed = true), vehicles)

    private val row = mapOf(
        "id" to "id-snow-crash",
        "reported_date" to "2022-11-29T00:00:00Z",
        "state" to "WA",
        "fatalities" to 0,
        "injuries" to 0,
        "tow_away" to true,
        "hazmat_released" to false,
        "severity_weight" to 1,
        "time_weight" to 1,
        "severity_time_weight" to 1,
        "trafficway" to "Two-Way Trafficway, Divided, Positive Barrier",
        "access_control" to "No Control",
        "road_surface_condition" to "Wet",
        "light_condition" to "Dark - Not Lighted",
        "weather_condition" to "Snow",
        "not_preventable_det" to true,
        "vehicle_id" to "TK45678",
        "imported_at" to "2025-01-02T15:15:15Z",
    )

    private val entity = CrashSpawner.snowCrash()

    @Test
    fun `given a row then map to entity`() {
        every { vehicles.loadById(any()) } returns VehicleSpawner.semiTrailer()

        val result = table.entry(MapOrmResultSet(row))

        assertObjects(entity.vehicle, result.vehicle)
    }

    @Test
    fun `given an entity then map to a row`() {
        assertObjects(row, table.values(entity))
    }
}
