package com.example.routing.ui.form

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.routing.di.LocalTravelDiaryViewModel
import com.example.routing.domain.model.Difficulty
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun FormScreen(
    onRouteGenerated: (String) -> Unit,
    onOpenHistory: () -> Unit,
    modifier: Modifier = Modifier
) {
    val viewModel = LocalTravelDiaryViewModel.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    var showDatePicker by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(uiState.generatedRouteId) {
        uiState.generatedRouteId?.let { routeId ->
            onRouteGenerated(routeId)
            viewModel.clearGeneratedRoute()
        }
    }

    if (showDatePicker) {
        val datePickerState = rememberDatePickerState()
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        datePickerState.selectedDateMillis?.let { millis ->
                            val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                            viewModel.onDateChange(formatter.format(Date(millis)))
                        }
                        showDatePicker = false
                    }
                ) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("Cancel·la")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Registra una nova ruta",
            style = MaterialTheme.typography.headlineMedium
        )

        OutlinedTextField(
            value = uiState.routeName,
            onValueChange = viewModel::onRouteNameChange,
            label = { Text("Nom de la ruta") },
            isError = uiState.routeNameError != null,
            supportingText = { uiState.routeNameError?.let { Text(it) } },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = uiState.destination,
            onValueChange = viewModel::onDestinationChange,
            label = { Text("Destí") },
            isError = uiState.destinationError != null,
            supportingText = { uiState.destinationError?.let { Text(it) } },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Text(
            text = "Nivell de dificultat",
            style = MaterialTheme.typography.titleMedium
        )
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Difficulty.entries.forEach { difficulty ->
                FilterChip(
                    selected = uiState.difficulty == difficulty,
                    onClick = { viewModel.onDifficultyChange(difficulty) },
                    label = { Text(difficulty.label) }
                )
            }
        }

        OutlinedTextField(
            value = uiState.budgetInput,
            onValueChange = viewModel::onBudgetChange,
            label = { Text("Pressupost estimat (€)") },
            isError = uiState.budgetError != null,
            supportingText = { uiState.budgetError?.let { Text(it) } },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            modifier = Modifier.fillMaxWidth()
        )

        Button(onClick = { showDatePicker = true }) {
            Text(
                if (uiState.date.isBlank()) {
                    "Seleccionar data"
                } else {
                    "Data: ${uiState.date}"
                }
            )
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            Switch(
                checked = uiState.isSolo,
                onCheckedChange = viewModel::onSoloChange
            )
            Spacer(modifier = Modifier.padding(horizontal = 8.dp))
            Text("Ruta feta en solitari")
        }

        OutlinedTextField(
            value = uiState.notes,
            onValueChange = viewModel::onNotesChange,
            label = { Text("Notes de l'experiència") },
            minLines = 4,
            maxLines = 6,
            modifier = Modifier.fillMaxWidth()
        )

        uiState.saveError?.let { error ->
            Text(text = error, color = MaterialTheme.colorScheme.error)
        }

        Button(
            onClick = viewModel::generateRoute,
            enabled = uiState.isFormValid,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Generar Postal / Crònica")
        }

        TextButton(
            onClick = onOpenHistory,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Consultar historial")
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}
