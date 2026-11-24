package com.example.routing.data.model

import kotlinx.serialization.Serializable

@Serializable
data class RouteEntryDto(
    val id: String,
    val routeName: String,
    val destination: String,
    val difficulty: String,
    val estimatedBudget: Double,
    val date: String,
    val isSolo: Boolean,
    val notes: String
)
