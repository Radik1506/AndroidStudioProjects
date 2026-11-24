package com.example.routing.ui.postal

import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.routing.di.LocalTravelDiaryViewModel

@Composable
fun PostalScreen(
    routeId: String,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val viewModel = LocalTravelDiaryViewModel.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    val route = uiState.savedRoutes.firstOrNull { it.id == routeId }

    val exportLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CreateDocument("text/plain")
    ) { uri ->
        if (uri != null && route != null) {
            val chronicle = viewModel.formatChronicle(route)
            context.contentResolver.openOutputStream(uri)?.use { stream ->
                stream.write(chronicle.toByteArray())
            }
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Button(onClick = onBack) {
            Text("Enrere")
        }

        if (route == null) {
            Text("No s'ha trobat la ruta.")
        } else {
            Text(
                text = "Postal del viatge",
                style = MaterialTheme.typography.headlineMedium
            )

            Card(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = route.destination,
                        style = MaterialTheme.typography.headlineLarge,
                        color = MaterialTheme.colorScheme.primary
                    )

                    Text(
                        text = route.date,
                        style = MaterialTheme.typography.titleMedium
                    )

                    AssistChip(
                        onClick = {},
                        label = { Text(route.difficulty.label) }
                    )

                    Text(
                        text = "Ruta: ${route.routeName}",
                        style = MaterialTheme.typography.bodyLarge
                    )

                    Text(
                        text = "Pressupost: ${String.format("%.2f", route.estimatedBudget)} €",
                        style = MaterialTheme.typography.bodyLarge
                    )

                    Text(
                        text = if (route.isSolo) "Viatge en solitari" else "Viatge acompanyat",
                        style = MaterialTheme.typography.bodyLarge
                    )

                    Text(
                        text = route.notes.ifBlank { "Sense notes" },
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            Button(
                onClick = {
                    val safeName = route.routeName
                        .filter { it.isLetterOrDigit() }
                        .ifBlank { "ruta" }
                    exportLauncher.launch("cronica_${safeName}.md")
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Exportar Crònica (.md / .txt)")
            }

            Button(
                onClick = {
                    val intent = Intent(Intent.ACTION_SEND).apply {
                        type = "text/plain"
                        putExtra(Intent.EXTRA_SUBJECT, "Crònica de viatge: ${route.destination}")
                        putExtra(Intent.EXTRA_TEXT, viewModel.formatChronicle(route))
                    }
                    context.startActivity(Intent.createChooser(intent, "Comparteix la crònica"))
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Compartir")
            }
        }
    }
}
