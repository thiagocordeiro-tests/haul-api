package com.haulcompliance.inspections.infrastructure.data.api.fmcsa.dto

import com.haulcompliance.inspections.domain.inspection.Inspection
import com.haulcompliance.inspections.infrastructure.data.api.fmcsa.yesNoToBoolean
import java.time.LocalDate
import java.time.LocalDateTime

@Suppress("PropertyName")
data class FmcsaInspection(
    val inspection_date: LocalDate,
    val report_state: String,
    val report_number: String,
    val level: Int,
    val time_weight: Int,
    val Placarable_HM_Veh_Insp: String,
    val HM_inspection: String,
    val vehicles: List<FmcsaVehicle> = emptyList(),
    val violations: List<FmcsaViolation> = emptyList(),
) {
    private val violationsWithBasic = lazy {
        violations
            .filter { it.BASIC != null }
            .map { it.toViolation() }
    }

    fun toInspection(): Inspection = Inspection(
        id = report_number,
        state = report_state,
        level = level,
        timeWeight = time_weight,
        placarableHazmatInspection = Placarable_HM_Veh_Insp.yesNoToBoolean(),
        hazmatInspection = HM_inspection.yesNoToBoolean(),
        inspectedAt = inspection_date,
        status = if (violationsWithBasic.value.isEmpty()) Inspection.Status.NoViolations else Inspection.Status.Unresolved,
        vehicles = lazy { vehicles.map { Inspection.InspectedVehicle(unit = it.unit ?: 1, vehicle = it.toVehicle()) } },
        violations = violationsWithBasic,
        importedAt = LocalDateTime.now(),
    )
}
