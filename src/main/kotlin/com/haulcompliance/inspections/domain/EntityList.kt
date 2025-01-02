package com.haulcompliance.inspections.domain

data class EntryList<T>(
    val total: Int,
    val entries: Iterable<T>,
)
