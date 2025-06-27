package com.cubancore.pianotyping.data

import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
data class Recording (
    val uuid: Uuid,
    var title: String,
    var compositor: String?,
    val records: List<Record>,
)