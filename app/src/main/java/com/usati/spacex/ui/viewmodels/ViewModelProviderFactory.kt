package com.usati.spacex.ui.viewmodels

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.usati.spacex.SpacexRepository

class ViewModelProviderFactory {
    class RocketViewModelProviderFactory(
    val app: Application,
    private val spacexRepository: SpacexRepository
    ) : ViewModelProvider.Factory{
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            //return RocketViewModel(app, spacexRepository) as T
            return RocketViewModel(app, spacexRepository) as T
        }
    }
    class LaunchViewModelProviderFactory(
        val app: Application,
        private val spacexRepository: SpacexRepository
    ) : ViewModelProvider.Factory{
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            //return RocketViewModel(app, spacexRepository) as T
            return LaunchViewModel(app, spacexRepository) as T
        }
    }
}