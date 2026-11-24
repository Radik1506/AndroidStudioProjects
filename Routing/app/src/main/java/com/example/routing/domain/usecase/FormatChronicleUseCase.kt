package com.example.routing.domain.usecase

import com.example.routing.domain.model.RouteEntry
import java.util.Locale

class FormatChronicleUseCase {
    operator fun invoke(route: RouteEntry): String = buildString {
        appendLine("Crònica de viatge")
        appendLine("================")
        appendLine()
        appendLine("Data: ${route.date}")
        appendLine("Destí: ${route.destination}")
        appendLine("Ruta: ${route.routeName}")
        appendLine("Dificultat: ${route.difficulty.label}")
        appendLine("Pressupost estimat: ${String.format(Locale.getDefault(), "%.2f", route.estimatedBudget)} €")
        appendLine("Viatge en solitari: ${if (route.isSolo) "Sí" else "No"}")
        appendLine()
        appendLine("Relat")
        appendLine("-----")
        appendLine(route.notes.ifBlank { "Sense notes" })
    }
}
