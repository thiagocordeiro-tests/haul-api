package com.haulcompliance.inspections.infrastructure.data.api.fmcsa.dto

import com.haulcompliance.inspections.testing.fixtures.InspectionSpawner
import com.haulcompliance.inspections.testing.freeze
import io.tcds.orm.testing.assertObjects
import org.junit.jupiter.api.Test
import java.time.LocalDate

class FmcsaInspectionTest {
    @Test
    fun `given a fmcsa vehicle then convert into domain vehicle entity`() = freeze {
        val fmcsaInspection = FmcsaInspection(
            inspection_date = LocalDate.parse("2023-08-25"),
            report_state = "TX",
            report_number = "id-truck-tractor-inspection",
            level = 1,
            time_weight = 1,
            Placarable_HM_Veh_Insp = "N",
            HM_inspection = "N",
            vehicles = listOf(
                FmcsaVehicle(
                    unit = 1,
                    vehicle_id_number = "TC12345",
                    unit_type = "Truck Tractor",
                    license_state = "NY",
                    license_number = "TRUCK-C12345",
                ),
                FmcsaVehicle(
                    unit = 2,
                    vehicle_id_number = "TK45678",
                    unit_type = "Semi-Trailer",
                    license_state = "CA",
                    license_number = "TRAILER-K12345",
                ),
            ),
            violations = listOf(
                FmcsaViolation(
                    convicted_of_dif_charge = "Y",
                    code = "393.9T",
                    description = "Inoperable tail lamp",
                    oos = "No",
                    time_severity_weight = 6,
                    BASIC = "Vehicle Maint.",
                    unit = "1",
                ),
                FmcsaViolation(
                    convicted_of_dif_charge = "Y",
                    code = "393.45",
                    description = "Brake tubing and hose adequacy",
                    oos = "Y",
                    time_severity_weight = 6,
                    BASIC = "Vehicle Maint.",
                    unit = "1",
                ),
            ),
        )

        val inspection = fmcsaInspection.toInspection()

        assertObjects(InspectionSpawner.truckTractor(), inspection)
    }
}
