package com.haulcompliance.inspections.infrastructure.data.api.fmcsa.dto

import com.haulcompliance.inspections.testing.fixtures.CrashSpawner
import com.haulcompliance.inspections.testing.freeze
import io.tcds.orm.testing.assertObjects
import org.junit.jupiter.api.Test
import java.time.LocalDate

class FmcsaCrashTest {
    @Test
    fun `given a fmcsa crash then convert into domain crash entity`() = freeze {
        val fmcsaCrash = FmcsaCrash(
            report_date = LocalDate.parse("2022-11-29"),
            report_state = "WA",
            report_number = "id-snow-crash",
            fatalities = 0,
            injuries = 0,
            tow_away = "Y",
            hazmat_released = "N",
            severity_weight = 1,
            time_weight = 1,
            severity_time_weight = 1,
            trafficway = "Two-Way Trafficway, Divided, Positive Barrier",
            access_control = "No Control",
            road_surface_condition = "Wet",
            light_condition = "Dark - Not Lighted",
            weather_condition = "Snow",
            not_preventable_det = "Y",
            vehicle = FmcsaVehicle(
                unit = null,
                vehicle_id_number = "TK45678",
                unit_type = "Semi-Trailer",
                license_state = "CA",
                license_number = "TRAILER-K12345",
            ),
        )

        val crash = fmcsaCrash.toCrash()

        assertObjects(CrashSpawner.snowCrash(), crash)
    }
}
