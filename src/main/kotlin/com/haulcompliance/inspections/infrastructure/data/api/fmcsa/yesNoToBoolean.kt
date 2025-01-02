package com.haulcompliance.inspections.infrastructure.data.api.fmcsa

fun String?.yesNoToBoolean() = this == "Yes" || this == "Y"
