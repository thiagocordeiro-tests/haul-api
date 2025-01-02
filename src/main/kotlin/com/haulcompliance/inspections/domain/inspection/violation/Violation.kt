package com.haulcompliance.inspections.domain.inspection.violation

data class Violation(
    val convictedOfDifCharge: Boolean,
    val code: String?,
    val description: String?,
    val oos: Boolean,
    val timeSeverityWeight: Int?,
    val basic: String?,
    val unit: Int?,
)
