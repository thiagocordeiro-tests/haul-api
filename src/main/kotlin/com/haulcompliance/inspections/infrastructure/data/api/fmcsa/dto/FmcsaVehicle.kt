package com.haulcompliance.inspections.infrastructure.data.api.fmcsa.dto

import com.haulcompliance.inspections.domain.vehicle.Vehicle

@Suppress("PropertyName")
data class FmcsaVehicle(
    val unit: Int? = null,
    val vehicle_id_number: String? = null,
    val unit_type: String? = null,
    val license_state: String? = null,
    val license_number: String? = null,
) {

    fun toVehicle() = Vehicle(
        id = id(),
        type = unit_type ?: "",
        license = Vehicle.License(
            number = reference() ?: "",
            state = license_state ?: "",
        ),
    )

    private fun id() = vehicle_id_number ?: reference() ?: "unknown"

    private fun reference() = (license_number ?: "").let {
        when (it.length) {
            // do not use license numbers smaller than 3 digits (should be invalid)
            // this might be extended to a more extensive validation
            in 0..3 -> null
            else -> it
        }
    }
}
