package com.example.routing.di

import androidx.compose.runtime.compositionLocalOf
import com.example.routing.ui.TravelDiaryViewModel

val LocalTravelDiaryViewModel = compositionLocalOf<TravelDiaryViewModel> {
    error("TravelDiaryViewModel is not provided")
}
