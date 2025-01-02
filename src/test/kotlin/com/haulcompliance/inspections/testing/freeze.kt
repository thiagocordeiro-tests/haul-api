package com.haulcompliance.inspections.testing

import io.mockk.every
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import kotlinx.coroutines.runBlocking
import java.time.Clock
import java.time.Instant
import java.time.ZoneOffset

fun <T> freeze(
    clock: String = "2025-01-02T15:15:15Z",
    block: suspend () -> T,
): T {
    freezeClock(clock)
    val response = runBlocking { block() }

    unfreezeClock()

    return response
}

private fun freezeClock(at: String) {
    mockkStatic(Clock::class)
    every { Clock.systemDefaultZone() } returns Clock.fixed(Instant.parse(at), ZoneOffset.UTC)
}

private fun unfreezeClock() = unmockkStatic(Clock::class)
