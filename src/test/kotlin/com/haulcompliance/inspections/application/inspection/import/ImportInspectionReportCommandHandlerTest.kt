package com.haulcompliance.inspections.application.inspection.import

import com.haulcompliance.inspections.domain.inspection.Inspections
import com.haulcompliance.inspections.domain.inspection.ParsedReport
import com.haulcompliance.inspections.domain.inspection.ReportParser
import com.haulcompliance.inspections.domain.vehicle.Vehicles
import com.haulcompliance.inspections.domain.vehicle.crash.Crashes
import com.haulcompliance.inspections.testing.fixtures.CrashSpawner
import com.haulcompliance.inspections.testing.fixtures.InspectionSpawner
import com.haulcompliance.inspections.testing.fixtures.VehicleSpawner
import io.mockk.*
import io.tcds.orm.testing.matchObject
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test

class ImportInspectionReportCommandHandlerTest {
    private val parser: ReportParser = mockk()
    private val vehicles: Vehicles = mockk()
    private val inspections: Inspections = mockk()
    private val crashes: Crashes = mockk()

    private val handler = ImportInspectionReportCommandHandler(parser, vehicles, inspections, crashes)

    @Test
    fun `given a report content then parse and store its data`() = runBlocking {
        every { parser.parse(any()) } returns ParsedReport(
            inspections = listOf(InspectionSpawner.truckTractor(), InspectionSpawner.straightTruck()),
            crashes = listOf(CrashSpawner.snowCrash(), CrashSpawner.sunLightCrash()),
        )
        every { vehicles.store(any()) } just runs
        every { inspections.store(any()) } just runs
        every { crashes.store(any()) } just runs

        val command = ImportInspectionReportCommand("<xml/>")

        handler.handle(command)

        verify(exactly = 1) {
            parser.parse("<xml/>")
        }
        verify(exactly = 1) {
            vehicles.store(
                matchObject { listOf(VehicleSpawner.truckTractor(), VehicleSpawner.semiTrailer(), VehicleSpawner.straightTruck()) },
            )
        }
        verify(exactly = 1) {
            inspections.store(matchObject { listOf(InspectionSpawner.truckTractor(), InspectionSpawner.straightTruck()) })
        }
        verify(exactly = 1) {
            crashes.store(matchObject { listOf(CrashSpawner.snowCrash(), CrashSpawner.sunLightCrash()) })
        }
    }
}
