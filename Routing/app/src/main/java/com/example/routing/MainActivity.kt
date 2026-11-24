package com.example.routing

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import com.example.routing.di.AppContainer
import com.example.routing.di.LocalTravelDiaryViewModel
import com.example.routing.ui.TravelDiaryViewModel
import com.example.routing.ui.navigation.AppNavigation
import com.example.routing.ui.theme.RoutingTheme

class MainActivity : ComponentActivity() {
    private val container: AppContainer by lazy {
        (application as RoutingApplication).container
    }

    private val viewModel: TravelDiaryViewModel by lazy {
        ViewModelProvider(this, container.travelDiaryViewModelFactory)
            .get(TravelDiaryViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            RoutingTheme {
                CompositionLocalProvider(LocalTravelDiaryViewModel provides viewModel) {
                    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                        AppNavigation(modifier = Modifier.padding(innerPadding))
                    }
                }
            }
        }
    }
}
