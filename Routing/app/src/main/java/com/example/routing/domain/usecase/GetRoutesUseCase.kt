package com.example.routing.domain.usecase

import com.example.routing.domain.model.RouteEntry
import com.example.routing.domain.repository.DiaryRepository

class GetRoutesUseCase(private val repository: DiaryRepository) {
    suspend operator fun invoke(): List<RouteEntry> = repository.getRoutes()
}
