package com.example.routing.data.local

import android.content.Context
import com.example.routing.data.model.RouteEntryDto
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File

@Serializable
internal data class DiaryFileContent(val routes: List<RouteEntryDto>)

class DiaryFileDataSource(context: Context) {
    private val file = File(context.applicationContext.filesDir, FILE_NAME)
    private val json = Json { ignoreUnknownKeys = true }

    fun readRoutes(): List<RouteEntryDto> {
        if (!file.exists()) return emptyList()
        return runCatching {
            json.decodeFromString<DiaryFileContent>(file.readText()).routes
        }.getOrDefault(emptyList())
    }

    fun writeRoutes(routes: List<RouteEntryDto>) {
        file.writeText(json.encodeToString(DiaryFileContent(routes)))
    }

    private companion object {
        const val FILE_NAME = "diari_viatges.json"
    }
}
