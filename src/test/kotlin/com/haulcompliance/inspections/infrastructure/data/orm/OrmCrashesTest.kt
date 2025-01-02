package com.haulcompliance.inspections.infrastructure.data.orm

import com.haulcompliance.inspections.infrastructure.data.orm.table.CrashTable
import com.haulcompliance.inspections.testing.fixtures.CrashSpawner
import io.mockk.every
import io.mockk.mockk
import io.mockk.verifySequence
import io.tcds.orm.testing.matchObject
import org.junit.jupiter.api.Test

class OrmCrashesTest {
    private val table: CrashTable = mockk()
    private val crashes = OrmCrashes(chunkSize = 1, table = table)

    @Test
    fun `given a list of crashes then insert in chunks`() {
        every { table.bulkInsertIgnore(any()) } returns mockk(relaxed = true)

        crashes.store(listOf(CrashSpawner.snowCrash(), CrashSpawner.sunLightCrash()))

        verifySequence {
            table.bulkInsertIgnore(matchObject { listOf(CrashSpawner.snowCrash()) })
            table.bulkInsertIgnore(matchObject { listOf(CrashSpawner.sunLightCrash()) })
        }
    }
}
