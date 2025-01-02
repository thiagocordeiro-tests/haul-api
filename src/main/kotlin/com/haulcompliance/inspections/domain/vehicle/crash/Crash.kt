package com.haulcompliance.inspections.domain.vehicle.crash

import com.haulcompliance.inspections.domain.vehicle.Vehicle
import java.time.LocalDate
import java.time.LocalDateTime

class Crash(
    val id: String,
    val reportedDate: LocalDate,
    val state: String,
    val fatalities: Int,
    val injuries: Int,
    val towAway: Boolean,
    val hazmatReleased: Boolean,
    val severityWeight: Int,
    val timeWeight: Int,
    val severityTimeWeight: Int,
    val trafficway: String,
    val accessControl: String,
    val roadSurfaceCondition: String,
    val lightCondition: String,
    val weatherCondition: String,
    val notPreventableDet: Boolean,
    vehicle: Lazy<Vehicle>,
    val importedAt: LocalDateTime,
) {
    val vehicle by vehicle
}
