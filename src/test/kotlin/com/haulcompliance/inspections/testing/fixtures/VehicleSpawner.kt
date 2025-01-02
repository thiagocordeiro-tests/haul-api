package com.haulcompliance.inspections.testing.fixtures

import com.haulcompliance.inspections.domain.vehicle.Vehicle

object VehicleSpawner {
    fun truckTractor() = Vehicle(
        id = "TC12345",
        type = "Truck Tractor",
        license = Vehicle.License(number = "TRUCK-C12345", state = "NY"),
    )

    fun semiTrailer() = Vehicle(
        id = "TK45678",
        type = "Semi-Trailer",
        license = Vehicle.License(number = "TRAILER-K12345", state = "CA"),
    )

    fun straightTruck() = Vehicle(
        id = "ST98765",
        type = "Straight Truck",
        license = Vehicle.License(number = "STRAIGHT-T981234", state = "PA"),
    )
}
