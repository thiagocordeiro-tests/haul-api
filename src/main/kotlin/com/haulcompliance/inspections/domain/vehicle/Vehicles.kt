package com.haulcompliance.inspections.domain.vehicle

interface Vehicles {
    fun store(vehicles: Iterable<Vehicle>)
    fun loadById(id: String): Vehicle
}
