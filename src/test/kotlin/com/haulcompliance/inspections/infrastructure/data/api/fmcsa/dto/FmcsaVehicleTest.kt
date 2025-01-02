package com.haulcompliance.inspections.infrastructure.data.api.fmcsa.dto

import com.haulcompliance.inspections.testing.fixtures.VehicleSpawner
import com.haulcompliance.inspections.testing.freeze
import io.tcds.orm.testing.assertObjects
import org.junit.jupiter.api.Test

class FmcsaVehicleTest {
    @Test
    fun `given a fmcsa vehicle then convert into domain vehicle entity`() = freeze {
        val fmcsaVehicle = FmcsaVehicle(
            unit = null,
            vehicle_id_number = "ST98765",
            unit_type = "Straight Truck",
            license_state = "PA",
            license_number = "STRAIGHT-T981234",
        )

        val vehicle = fmcsaVehicle.toVehicle()

        assertObjects(VehicleSpawner.straightTruck(), vehicle)
    }
}
