package com.cubancore.pianotyping.data

data class Recording (
    val title: String,
    val compositor: String?,
    val records: List<Record>,
)