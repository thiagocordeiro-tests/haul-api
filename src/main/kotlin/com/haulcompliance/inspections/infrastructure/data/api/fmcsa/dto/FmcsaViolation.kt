package com.haulcompliance.inspections.infrastructure.data.api.fmcsa.dto

import com.haulcompliance.inspections.domain.inspection.violation.Violation
import com.haulcompliance.inspections.infrastructure.data.api.fmcsa.yesNoToBoolean

@Suppress("PropertyName")
data class FmcsaViolation(
    val convicted_of_dif_charge: String,
    val code: String? = null,
    val description: String? = null,
    val oos: String? = null,
    val time_severity_weight: Int? = null,
    val BASIC: String? = null,
    val unit: String? = null,
) {
    fun toViolation() = Violation(
        convictedOfDifCharge = convicted_of_dif_charge.yesNoToBoolean(),
        code = code,
        description = description,
        oos = oos.yesNoToBoolean(),
        timeSeverityWeight = time_severity_weight,
        basic = BASIC,
        unit = unit?.toIntOrNull(),
    )
}
