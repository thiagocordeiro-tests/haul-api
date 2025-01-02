package com.haulcompliance.inspections.domain.inspection

import com.haulcompliance.inspections.testing.fixtures.CrashSpawner
import com.haulcompliance.inspections.testing.fixtures.InspectionSpawner
import com.haulcompliance.inspections.testing.fixtures.VehicleSpawner
import io.tcds.orm.testing.assertObjects
import org.junit.jupiter.api.Test

class ParsedReportTest {
    @Test
    fun `given inspections and crashes then get non repeating vehicles from them`() {
        val parsed = ParsedReport(
            inspections = listOf(
                InspectionSpawner.with(
                    listOf(
                        Inspection.InspectedVehicle(1, VehicleSpawner.truckTractor()),
                        Inspection.InspectedVehicle(2, VehicleSpawner.semiTrailer()),
                    ),
                ),
                InspectionSpawner.with(
                    listOf(
                        Inspection.InspectedVehicle(1, VehicleSpawner.straightTruck()),
                    ),
                ),
            ),
            crashes = listOf(
                CrashSpawner.with(VehicleSpawner.truckTractor()),
                CrashSpawner.with(VehicleSpawner.semiTrailer()),
                CrashSpawner.with(VehicleSpawner.straightTruck()),
            ),
        )

        val vehicles = parsed.vehicles

        assertObjects(
            vehicles,
            listOf(
                VehicleSpawner.truckTractor(),
                VehicleSpawner.semiTrailer(),
                VehicleSpawner.straightTruck(),
            ),
        )
    }
}
