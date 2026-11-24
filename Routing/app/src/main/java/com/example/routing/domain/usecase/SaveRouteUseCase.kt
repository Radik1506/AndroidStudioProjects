package com.example.routing.domain.usecase

import com.example.routing.domain.model.RouteEntry
import com.example.routing.domain.repository.DiaryRepository

class SaveRouteUseCase(private val repository: DiaryRepository) {
    suspend operator fun invoke(route: RouteEntry) = repository.saveRoute(route)
}
