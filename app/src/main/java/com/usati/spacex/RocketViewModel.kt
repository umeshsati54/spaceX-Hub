package com.usati.spacex

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.usati.spacex.models.rocket.Rocket
import com.usati.spacex.models.rocket.RocketResponse
import kotlinx.coroutines.launch
import okio.IOException
import retrofit2.Response

class RocketViewModel(
    app: Application,
    private val rocketRepository: RocketRepository
) : AndroidViewModel(app) {
    val rockets: MutableLiveData<Resource<RocketResponse>> = MutableLiveData()
    var rocketResponse: RocketResponse? = null

    init {
        getRocketsAPI()
    }

    fun getRocketsAPI() = viewModelScope.launch {
        safeRocketsCall()
    }

    fun getRocketFromRoom() = rocketRepository.getRocketsRoom()

    suspend fun saveRocketDataIntoRoom(rocket: Rocket) = viewModelScope.launch {
        rocketRepository.upsert(rocket)
    }


    private fun handleRocketResponse(response: Response<RocketResponse>): Resource<RocketResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                if (rocketResponse == null) {
                    rocketResponse = resultResponse
                } else {
                    val old = rocketResponse
                    val new = resultResponse
                    old?.addAll(new)
                }
                return Resource.Success(rocketResponse ?: resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

    private suspend fun safeRocketsCall() {
        rockets.postValue(Resource.Loading())
        try {
            if (isConnected()) {
                val response = rocketRepository.getRocketsAPI()
                rockets.postValue(handleRocketResponse(response))
                response.body()?.forEach {
                    saveRocketDataIntoRoom(it)
                }
            } else {
                rockets.postValue(Resource.Error("No internet connection"))
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> rockets.postValue(Resource.Error("Network Failure"))
                else -> rockets.postValue(Resource.Error(t.message.toString()))
            }
        }
    }

    private fun isConnected(): Boolean {
        val connectivityManager = getApplication<SpacexApplication>()
            .getSystemService(
                Context.CONNECTIVITY_SERVICE
            ) as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
        return when {
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
    }

}