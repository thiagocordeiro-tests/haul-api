package com.haulcompliance.inspections.domain.inspection

import com.haulcompliance.inspections.domain.inspection.violation.Violation
import com.haulcompliance.inspections.domain.vehicle.Vehicle
import java.time.LocalDate
import java.time.LocalDateTime

class Inspection(
    val id: String,
    val state: String,
    val level: Int,
    val timeWeight: Int,
    val placarableHazmatInspection: Boolean,
    val hazmatInspection: Boolean,
    val inspectedAt: LocalDate,
    status: Status,
    vehicles: Lazy<List<InspectedVehicle>>,
    violations: Lazy<List<Violation>>,
    val importedAt: LocalDateTime,
) {
    data class InspectedVehicle(val unit: Int, val vehicle: Vehicle)
    enum class Status { Unresolved, NoViolations }

    val vehicles by vehicles
    val violations by violations

    var status = status
        private set
}
