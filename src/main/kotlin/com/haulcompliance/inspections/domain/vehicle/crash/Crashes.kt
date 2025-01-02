package com.haulcompliance.inspections.domain.vehicle.crash

interface Crashes {
    fun store(crashes: Iterable<Crash>)
}
