package com.haulcompliance.inspections.application.inspection.list

import com.haulcompliance.inspections.domain.inspection.Inspection
import com.haulcompliance.inspections.domain.inspection.InspectionFilter
import java.time.LocalDate
import java.time.LocalDateTime

data class ListInspectionsQuery(
    val filter: InspectionFilter,
) {
    data class Response(
        val id: String,
        val inspectedAt: LocalDate,
        val status: Inspection.Status,
        val plate: String,
        val violation: String?,
        val weight: Int?,
        val importedAt: LocalDateTime,
    )
}
