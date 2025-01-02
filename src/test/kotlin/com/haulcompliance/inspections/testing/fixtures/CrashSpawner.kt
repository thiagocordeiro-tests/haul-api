package com.haulcompliance.inspections.testing.fixtures

import com.haulcompliance.inspections.domain.vehicle.Vehicle
import com.haulcompliance.inspections.domain.vehicle.crash.Crash
import java.time.LocalDate
import java.time.LocalDateTime

object CrashSpawner {
    fun snowCrash() = Crash(
        id = "id-snow-crash",
        reportedDate = LocalDate.parse("2022-11-29"),
        state = "WA",
        fatalities = 0,
        injuries = 0,
        towAway = true,
        hazmatReleased = false,
        severityWeight = 1,
        timeWeight = 1,
        severityTimeWeight = 1,
        trafficway = "Two-Way Trafficway, Divided, Positive Barrier",
        accessControl = "No Control",
        roadSurfaceCondition = "Wet",
        lightCondition = "Dark - Not Lighted",
        weatherCondition = "Snow",
        notPreventableDet = true,
        vehicle = lazy { VehicleSpawner.semiTrailer() },
        importedAt = LocalDateTime.parse("2025-01-02T15:15:15"),
    )

    fun sunLightCrash() = Crash(
        id = "id-rain-crash",
        reportedDate = LocalDate.parse("2022-12-09"),
        state = "DE",
        fatalities = 0,
        injuries = 0,
        towAway = true,
        hazmatReleased = false,
        severityWeight = 1,
        timeWeight = 1,
        severityTimeWeight = 1,
        trafficway = "Two-Way Trafficway, Not Divided",
        accessControl = "No Control",
        roadSurfaceCondition = "Wet",
        lightCondition = "Dawn",
        weatherCondition = "Rain",
        notPreventableDet = true,
        vehicle = lazy { VehicleSpawner.semiTrailer() },
        importedAt = LocalDateTime.parse("2025-01-02T15:15:15"),
    )

    fun with(vehicle: Vehicle) = Crash(
        id = "id-snow-crash",
        reportedDate = LocalDate.parse("2022-11-29"),
        state = "WA",
        fatalities = 0,
        injuries = 0,
        towAway = true,
        hazmatReleased = false,
        severityWeight = 1,
        timeWeight = 1,
        severityTimeWeight = 1,
        trafficway = "Two-Way Trafficway, Divided, Positive Barrier",
        accessControl = "No Control",
        roadSurfaceCondition = "Wet",
        lightCondition = "Dark - Not Lighted",
        weatherCondition = "Snow",
        notPreventableDet = true,
        vehicle = lazy { vehicle },
        importedAt = LocalDateTime.parse("2025-01-02T15:15:15"),
    )
}
