package com.haulcompliance.inspections.domain.inspection

import com.haulcompliance.inspections.domain.EntryList

interface Inspections {
    fun store(inspections: Iterable<Inspection>)
    fun loadById(id: String): Inspection
    fun list(filter: InspectionFilter): EntryList<Inspection>
}
