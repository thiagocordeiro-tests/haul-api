package com.haulcompliance.inspections.domain

import java.util.UUID

object Uuid {
    fun create() = UUID.randomUUID().toString()
}
