package com.haulcompliance.inspections.testing.fixtures

import com.haulcompliance.inspections.domain.inspection.Inspection
import java.time.LocalDate
import java.time.LocalDateTime

object InspectionSpawner {
    fun truckTractor() = Inspection(
        id = "id-truck-tractor-inspection",
        state = "TX",
        level = 1,
        timeWeight = 1,
        placarableHazmatInspection = false,
        hazmatInspection = false,
        inspectedAt = LocalDate.parse("2023-08-25"),
        status = Inspection.Status.Unresolved,
        vehicles = lazy {
            listOf(
                Inspection.InspectedVehicle(1, VehicleSpawner.truckTractor()),
                Inspection.InspectedVehicle(2, VehicleSpawner.semiTrailer()),
            )
        },
        violations = lazy {
            listOf(
                ViolationSpawner.inoperableTail(),
                ViolationSpawner.breakTubing(),
            )
        },
        importedAt = LocalDateTime.parse("2025-01-02T15:15:15"),
    )

    fun straightTruck() = Inspection(
        id = "id-straight-truck-inspection",
        state = "PA",
        level = 1,
        timeWeight = 1,
        placarableHazmatInspection = false,
        hazmatInspection = false,
        inspectedAt = LocalDate.parse("2023-10-01"),
        status = Inspection.Status.Unresolved,
        vehicles = lazy {
            listOf(
                Inspection.InspectedVehicle(1, VehicleSpawner.straightTruck()),
            )
        },
        violations = lazy {
            listOf(
                ViolationSpawner.breakTubing(),
            )
        },
        importedAt = LocalDateTime.parse("2025-01-02T15:15:15"),
    )

    fun with(vehicles: List<Inspection.InspectedVehicle> = emptyList()) = Inspection(
        id = "id-truck-tractor-inspection",
        state = "TX",
        level = 1,
        timeWeight = 1,
        placarableHazmatInspection = false,
        hazmatInspection = false,
        inspectedAt = LocalDate.parse("2023-08-25"),
        status = Inspection.Status.NoViolations,
        vehicles = lazy { vehicles },
        violations = lazyOf(emptyList()),
        importedAt = LocalDateTime.parse("2025-01-02T15:15:15"),
    )
}
