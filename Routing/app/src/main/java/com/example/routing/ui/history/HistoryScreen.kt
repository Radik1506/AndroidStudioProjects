package com.example.routing.ui.history

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.routing.di.LocalTravelDiaryViewModel

@Composable
fun HistoryScreen(
    onBack: () -> Unit,
    onOpenRoute: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val viewModel = LocalTravelDiaryViewModel.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Button(onClick = onBack) {
            Text("Enrere")
        }

        Text(
            text = "Historial de rutes",
            style = MaterialTheme.typography.headlineMedium
        )

        when {
            uiState.isLoading -> CircularProgressIndicator()

            uiState.savedRoutes.isEmpty() -> Text("Encara no hi ha rutes desades.")

            else -> LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(items = uiState.savedRoutes, key = { it.id }) { route ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onOpenRoute(route.id) }
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = route.destination,
                                style = MaterialTheme.typography.titleMedium
                            )
                            Text(
                                text = "${route.date} · ${route.difficulty.label}",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }
            }
        }
    }
}
