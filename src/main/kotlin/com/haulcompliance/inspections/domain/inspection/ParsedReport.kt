package com.haulcompliance.inspections.domain.inspection

import com.haulcompliance.inspections.domain.vehicle.Vehicle
import com.haulcompliance.inspections.domain.vehicle.crash.Crash

class ParsedReport(val inspections: List<Inspection>, val crashes: List<Crash>) {
    val vehicles: List<Vehicle> = mutableListOf<Vehicle>().apply {
        addAll(
            inspections.flatMap { inspection -> inspection.vehicles.map { it.vehicle } },
        )
        addAll(
            crashes.map { it.vehicle },
        )
    }.associateBy { it.id }.values.toList()
}
