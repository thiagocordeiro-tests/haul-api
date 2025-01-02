package com.haulcompliance.inspections.domain.inspection

interface ReportParser {
    fun parse(content: String): ParsedReport
}
