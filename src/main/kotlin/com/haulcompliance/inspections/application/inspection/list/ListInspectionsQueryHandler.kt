package com.haulcompliance.inspections.application.inspection.list

import com.haulcompliance.inspections.domain.EntryList
import com.haulcompliance.inspections.domain.inspection.Inspections

class ListInspectionsQueryHandler(
    private val inspections: Inspections,
) {
    fun handle(query: ListInspectionsQuery): EntryList<ListInspectionsQuery.Response> {
        val inspections = inspections.list(query.filter)

        return EntryList(
            total = inspections.total,
            entries = inspections.entries.map { inspection ->
                ListInspectionsQuery.Response(
                    id = inspection.id,
                    inspectedAt = inspection.inspectedAt,
                    status = inspection.status,
                    plate = inspection.vehicles.first().vehicle.license.number,
                    violation = inspection.violations.firstOrNull()?.basic,
                    weight = inspection.timeWeight,
                    importedAt = inspection.importedAt,
                )
            }.toList(),
        )
    }
}
