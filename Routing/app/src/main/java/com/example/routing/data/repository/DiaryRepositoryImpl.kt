package com.example.routing.data.repository

import com.example.routing.data.local.DiaryFileDataSource
import com.example.routing.data.model.RouteEntryDto
import com.example.routing.domain.model.Difficulty
import com.example.routing.domain.model.RouteEntry
import com.example.routing.domain.repository.DiaryRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DiaryRepositoryImpl(private val dataSource: DiaryFileDataSource) : DiaryRepository {
    override suspend fun getRoutes(): List<RouteEntry> = withContext(Dispatchers.IO) {
        dataSource.readRoutes().map { it.toDomain() }
    }

    override suspend fun saveRoute(route: RouteEntry) = withContext(Dispatchers.IO) {
        val routes = dataSource.readRoutes().toMutableList()
        routes.add(route.toDto())
        dataSource.writeRoutes(routes)
    }

    private fun RouteEntryDto.toDomain(): RouteEntry = RouteEntry(
        id = id,
        routeName = routeName,
        destination = destination,
        difficulty = runCatching { Difficulty.valueOf(difficulty) }
            .getOrDefault(Difficulty.FACIL),
        estimatedBudget = estimatedBudget,
        date = date,
        isSolo = isSolo,
        notes = notes
    )

    private fun RouteEntry.toDto(): RouteEntryDto = RouteEntryDto(
        id = id,
        routeName = routeName,
        destination = destination,
        difficulty = difficulty.name,
        estimatedBudget = estimatedBudget,
        date = date,
        isSolo = isSolo,
        notes = notes
    )
}
