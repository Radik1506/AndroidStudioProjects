package com.example.routing

import android.app.Application
import com.example.routing.di.AppContainer

class RoutingApplication : Application() {
    val container: AppContainer by lazy { AppContainer(this) }
}
