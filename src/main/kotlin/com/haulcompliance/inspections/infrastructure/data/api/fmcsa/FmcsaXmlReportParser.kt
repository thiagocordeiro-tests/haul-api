package com.haulcompliance.inspections.infrastructure.data.api.fmcsa

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.haulcompliance.inspections.domain.inspection.ParsedReport
import com.haulcompliance.inspections.domain.inspection.ReportParser

class FmcsaXmlReportParser(
    private val mapper: ObjectMapper,
) : ReportParser {
    override fun parse(content: String): ParsedReport {
        val data = mapper.readValue<FmcsaCarrierData>(content)

        return ParsedReport(
            inspections = data.inspections.map { it.toInspection() },
            crashes = data.crashes.map { it.toCrash() },
        )
    }
}
