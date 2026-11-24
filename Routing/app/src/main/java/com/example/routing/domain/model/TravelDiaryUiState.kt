package com.example.routing.domain.model

data class TravelDiaryUiState(
    val routeName: String = "",
    val destination: String = "",
    val difficulty: Difficulty = Difficulty.FACIL,
    val budgetInput: String = "",
    val date: String = "",
    val isSolo: Boolean = false,
    val notes: String = "",
    val routeNameError: String? = null,
    val destinationError: String? = null,
    val budgetError: String? = null,
    val saveError: String? = null,
    val savedRoutes: List<RouteEntry> = emptyList(),
    val generatedRouteId: String? = null,
    val isLoading: Boolean = true
) {
    val isFormValid: Boolean
        get() = routeName.isNotBlank() &&
            destination.isNotBlank() &&
            date.isNotBlank() &&
            (budgetInput.toDoubleOrNull() ?: -1.0) >= 0.0
}
