package com.example.routing.di

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import com.example.routing.data.local.DiaryFileDataSource
import com.example.routing.data.repository.DiaryRepositoryImpl
import com.example.routing.domain.repository.DiaryRepository
import com.example.routing.domain.usecase.FormatChronicleUseCase
import com.example.routing.domain.usecase.GetRoutesUseCase
import com.example.routing.domain.usecase.SaveRouteUseCase
import com.example.routing.ui.TravelDiaryViewModel

class AppContainer(context: Context) {
    private val applicationContext = context.applicationContext

    private val dataSource by lazy { DiaryFileDataSource(applicationContext) }
    private val repository: DiaryRepository by lazy { DiaryRepositoryImpl(dataSource) }

    private val getRoutesUseCase by lazy { GetRoutesUseCase(repository) }
    private val saveRouteUseCase by lazy { SaveRouteUseCase(repository) }
    private val formatChronicleUseCase by lazy { FormatChronicleUseCase() }

    val travelDiaryViewModelFactory: ViewModelProvider.Factory by lazy {
        TravelDiaryViewModel.factory(
            getRoutesUseCase,
            saveRouteUseCase,
            formatChronicleUseCase
        )
    }
}
