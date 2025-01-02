package com.haulcompliance.inspections.domain.vehicle

data class Vehicle(
    val id: String,
    val type: String,
    val license: License,
) {
    data class License(val number: String, val state: String)
}
