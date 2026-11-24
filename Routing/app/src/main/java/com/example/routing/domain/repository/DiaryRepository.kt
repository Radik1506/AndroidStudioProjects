package com.example.routing.domain.repository

import com.example.routing.domain.model.RouteEntry

interface DiaryRepository {
    suspend fun getRoutes(): List<RouteEntry>
    suspend fun saveRoute(route: RouteEntry)
}
