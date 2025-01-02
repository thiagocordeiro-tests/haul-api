package com.haulcompliance.inspections.domain.inspection

data class InspectionFilter(
    val basic: String?,
    val status: Inspection.Status?,
    val page: Int,
    val entriesPerPage: Int,
    val orderBy: OrderBy,
    val order: Order,
) {
    enum class OrderBy(val value: String) {
        InspectedAt("inspected_at"),
        Status("status"),
        InspectionNumber("id")
    }

    enum class Order { ASC, DESC }
}
