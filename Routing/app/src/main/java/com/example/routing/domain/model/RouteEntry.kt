package com.example.routing.domain.model

data class RouteEntry(
    val id: String,
    val routeName: String,
    val destination: String,
    val difficulty: Difficulty,
    val estimatedBudget: Double,
    val date: String,
    val isSolo: Boolean,
    val notes: String
)
