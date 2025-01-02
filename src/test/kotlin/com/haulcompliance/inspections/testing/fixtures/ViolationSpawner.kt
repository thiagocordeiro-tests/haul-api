package com.haulcompliance.inspections.testing.fixtures

import com.haulcompliance.inspections.domain.inspection.violation.Violation

object ViolationSpawner {
    fun inoperableTail() = Violation(
        convictedOfDifCharge = true,
        code = "393.9T",
        description = "Inoperable tail lamp",
        oos = false,
        timeSeverityWeight = 6,
        basic = "Vehicle Maint.",
        unit = 1,
    )

    fun breakTubing() = Violation(
        convictedOfDifCharge = true,
        code = "393.45",
        description = "Brake tubing and hose adequacy",
        oos = true,
        timeSeverityWeight = 6,
        basic = "Vehicle Maint.",
        unit = 1,
    )
}
