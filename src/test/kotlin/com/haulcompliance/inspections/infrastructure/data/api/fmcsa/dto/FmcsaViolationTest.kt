package com.haulcompliance.inspections.infrastructure.data.api.fmcsa.dto

import com.haulcompliance.inspections.testing.fixtures.ViolationSpawner
import com.haulcompliance.inspections.testing.freeze
import io.tcds.orm.testing.assertObjects
import org.junit.jupiter.api.Test

class FmcsaViolationTest {
    @Test
    fun `given a fmcsa vehicle then convert into domain vehicle entity`() = freeze {
        val fmcsaViolation = FmcsaViolation(
            convicted_of_dif_charge = "Y",
            code = "393.9T",
            description = "Inoperable tail lamp",
            oos = "No",
            time_severity_weight = 6,
            BASIC = "Vehicle Maint.",
            unit = "1",
        )

        val violation = fmcsaViolation.toViolation()

        assertObjects(ViolationSpawner.inoperableTail(), violation)
    }
}
