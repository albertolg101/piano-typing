package com.cubancore.pianotyping.data

import kotlinx.serialization.Serializable

@Serializable
data class Record (
    val note: Int,
    val velocity: Int,
    val timestamp: Long,
)