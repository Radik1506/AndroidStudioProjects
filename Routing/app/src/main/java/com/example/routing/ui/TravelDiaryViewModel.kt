package com.example.routing.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.routing.domain.model.Difficulty
import com.example.routing.domain.model.RouteEntry
import com.example.routing.domain.model.TravelDiaryUiState
import com.example.routing.domain.usecase.FormatChronicleUseCase
import com.example.routing.domain.usecase.GetRoutesUseCase
import com.example.routing.domain.usecase.SaveRouteUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID

class TravelDiaryViewModel(
    private val getRoutesUseCase: GetRoutesUseCase,
    private val saveRouteUseCase: SaveRouteUseCase,
    private val formatChronicleUseCase: FormatChronicleUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(TravelDiaryUiState())
    val uiState: StateFlow<TravelDiaryUiState> = _uiState.asStateFlow()

    init {
        loadRoutes()
    }

    private fun loadRoutes() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            runCatching { getRoutesUseCase() }
                .onSuccess { routes -> _uiState.update { it.copy(isLoading = false, savedRoutes = routes) } }
                .onFailure { _uiState.update { it.copy(isLoading = false) } }
        }
    }

    fun onRouteNameChange(value: String) {
        _uiState.update {
            it.copy(
                routeName = value,
                routeNameError = if (value.isBlank()) ROUTE_NAME_REQUIRED else null
            )
        }
    }

    fun onDestinationChange(value: String) {
        _uiState.update {
            it.copy(
                destination = value,
                destinationError = if (value.isBlank()) DESTINATION_REQUIRED else null
            )
        }
    }

    fun onDifficultyChange(value: Difficulty) {
        _uiState.update { it.copy(difficulty = value) }
    }

    fun onBudgetChange(value: String) {
        _uiState.update {
            it.copy(
                budgetInput = value,
                budgetError = when {
                    value.isBlank() -> BUDGET_REQUIRED
                    (value.toDoubleOrNull() ?: -1.0) < 0.0 -> BUDGET_INVALID
                    else -> null
                }
            )
        }
    }

    fun onDateChange(value: String) {
        _uiState.update { it.copy(date = value) }
    }

    fun onSoloChange(value: Boolean) {
        _uiState.update { it.copy(isSolo = value) }
    }

    fun onNotesChange(value: String) {
        _uiState.update { it.copy(notes = value) }
    }

    fun generateRoute() {
        val state = _uiState.value
        if (!state.isFormValid) return

        val route = RouteEntry(
            id = UUID.randomUUID().toString(),
            routeName = state.routeName.trim(),
            destination = state.destination.trim(),
            difficulty = state.difficulty,
            estimatedBudget = state.budgetInput.toDouble(),
            date = state.date,
            isSolo = state.isSolo,
            notes = state.notes
        )

        viewModelScope.launch {
            runCatching { saveRouteUseCase(route) }
                .onSuccess {
                    _uiState.update { current ->
                        current.copy(
                            savedRoutes = listOf(route) + current.savedRoutes,
                            generatedRouteId = route.id,
                            routeName = "",
                            destination = "",
                            difficulty = Difficulty.FACIL,
                            budgetInput = "",
                            date = "",
                            isSolo = false,
                            notes = "",
                            routeNameError = null,
                            destinationError = null,
                            budgetError = null,
                            saveError = null
                        )
                    }
                }
                .onFailure { throwable ->
                    _uiState.update {
                        it.copy(saveError = throwable.message ?: "No s'ha pogut desar la ruta")
                    }
                }
        }
    }

    fun clearGeneratedRoute() {
        _uiState.update { it.copy(generatedRouteId = null) }
    }

    fun getRoute(routeId: String): RouteEntry? =
        _uiState.value.savedRoutes.firstOrNull { it.id == routeId }

    fun formatChronicle(route: RouteEntry): String = formatChronicleUseCase(route)

    companion object {
        private const val ROUTE_NAME_REQUIRED = "El nom de la ruta és obligatori"
        private const val DESTINATION_REQUIRED = "El destí és obligatori"
        private const val BUDGET_REQUIRED = "El pressupost és obligatori"
        private const val BUDGET_INVALID = "Introdueix un pressupost vàlid"

        fun factory(
            getRoutesUseCase: GetRoutesUseCase,
            saveRouteUseCase: SaveRouteUseCase,
            formatChronicleUseCase: FormatChronicleUseCase
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T =
                TravelDiaryViewModel(
                    getRoutesUseCase,
                    saveRouteUseCase,
                    formatChronicleUseCase
                ) as T
        }
    }
}
