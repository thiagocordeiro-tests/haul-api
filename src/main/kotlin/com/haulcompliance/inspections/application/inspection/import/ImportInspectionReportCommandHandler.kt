package com.haulcompliance.inspections.application.inspection.import

import com.haulcompliance.inspections.domain.inspection.Inspections
import com.haulcompliance.inspections.domain.inspection.ReportParser
import com.haulcompliance.inspections.domain.vehicle.Vehicles
import com.haulcompliance.inspections.domain.vehicle.crash.Crashes
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ImportInspectionReportCommandHandler(
    private val parser: ReportParser,
    private val vehicles: Vehicles,
    private val inspections: Inspections,
    private val crashes: Crashes,
) {
    suspend fun handle(command: ImportInspectionReportCommand) {
        val parsed = parser.parse(command.content)

        withContext(Dispatchers.IO) { inspections.store(parsed.inspections) }
        withContext(Dispatchers.IO) { crashes.store(parsed.crashes) }
        withContext(Dispatchers.IO) { vehicles.store(parsed.vehicles) }
    }
}
