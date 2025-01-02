package com.haulcompliance.inspections.infrastructure.data.api.fmcsa.dto

import com.haulcompliance.inspections.domain.vehicle.crash.Crash
import com.haulcompliance.inspections.infrastructure.data.api.fmcsa.yesNoToBoolean
import java.time.LocalDate
import java.time.LocalDateTime

@Suppress("PropertyName")
data class FmcsaCrash(
    val report_date: LocalDate,
    val report_state: String,
    val report_number: String,
    val fatalities: Int,
    val injuries: Int,
    val tow_away: String,
    val hazmat_released: String? = null,
    val severity_weight: Int,
    val time_weight: Int,
    val severity_time_weight: Int,
    val trafficway: String? = null,
    val access_control: String? = null,
    val road_surface_condition: String? = null,
    val light_condition: String? = null,
    val weather_condition: String? = null,
    val not_preventable_det: String? = null,
    val vehicle: FmcsaVehicle,
) {
    fun toCrash() = Crash(
        id = report_number,
        reportedDate = report_date,
        state = report_state,
        fatalities = fatalities,
        injuries = injuries,
        towAway = tow_away.yesNoToBoolean(),
        hazmatReleased = hazmat_released.yesNoToBoolean(),
        severityWeight = severity_weight,
        timeWeight = time_weight,
        severityTimeWeight = severity_time_weight,
        trafficway = trafficway ?: "",
        accessControl = access_control ?: "",
        roadSurfaceCondition = road_surface_condition ?: "",
        lightCondition = light_condition ?: "",
        weatherCondition = weather_condition ?: "",
        notPreventableDet = not_preventable_det.yesNoToBoolean(),
        importedAt = LocalDateTime.now(),
        vehicle = lazy { vehicle.toVehicle() },
    )
}
