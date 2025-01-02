package com.haulcompliance.inspections.infrastructure.data.orm

import com.haulcompliance.inspections.domain.vehicle.crash.Crash
import com.haulcompliance.inspections.domain.vehicle.crash.Crashes
import com.haulcompliance.inspections.infrastructure.data.orm.table.CrashTable

class OrmCrashes(
    private val chunkSize: Int,
    private val table: CrashTable,
) : Crashes {
    override fun store(crashes: Iterable<Crash>) {
        crashes
            .chunked(chunkSize)
            .forEach { chunk -> table.bulkInsertIgnore(chunk) }
    }
}
