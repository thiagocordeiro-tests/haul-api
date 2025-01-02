package com.haulcompliance.inspections.infrastructure.data.api.fmcsa

import com.haulcompliance.inspections.infrastructure.data.api.fmcsa.dto.FmcsaCrash
import com.haulcompliance.inspections.infrastructure.data.api.fmcsa.dto.FmcsaInspection

data class FmcsaCarrierData(
    val inspections: List<FmcsaInspection> = emptyList(),
    val crashes: List<FmcsaCrash> = emptyList(),
)
