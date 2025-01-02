package com.haulcompliance.inspections.application.inspection.get

import com.haulcompliance.inspections.domain.inspection.Inspection
import com.haulcompliance.inspections.domain.inspection.Inspections

class GetInspectionsQueryHandler(
    private val inspections: Inspections,
) {
    fun handle(query: GetInspectionsQuery): Inspection {
        return inspections.loadById(query.id)
    }
}
