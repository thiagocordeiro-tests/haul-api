package com.haulcompliance.inspections.infrastructure.data.api.fmcsa

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class YesNoToBooleanTest {
    @Test
    fun `given a Y or Yes string then value is true`() {
        Assertions.assertTrue("Y".yesNoToBoolean())
        Assertions.assertTrue("Yes".yesNoToBoolean())
    }

    @Test
    fun `given a N or No string then value is false`() {
        Assertions.assertFalse("N".yesNoToBoolean())
        Assertions.assertFalse("No".yesNoToBoolean())
    }
}
