package com.usati.spacex

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class RocketViewModelProviderFactory(
    val app: Application,
    private val rocketRepository: RocketRepository
) :ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return RocketViewModel(app, rocketRepository) as T
    }
}