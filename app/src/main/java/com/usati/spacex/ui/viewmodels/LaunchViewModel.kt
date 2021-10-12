package com.usati.spacex.ui.viewmodels

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.usati.spacex.Resource
import com.usati.spacex.SpacexRepository
import com.usati.spacex.models.launch.Launch
import com.usati.spacex.models.launch.LaunchesResponse
import kotlinx.coroutines.launch
import okio.IOException
import retrofit2.Response

class LaunchViewModel(
    app: Application,
    private val spacexRepository: SpacexRepository
) : BaseViewModel(app) {
    val launches: MutableLiveData<Resource<LaunchesResponse>> = MutableLiveData()
    var launchesResponse: LaunchesResponse? = null

    init {
        getLaunchesAPI()
    }

    fun getLaunchesAPI() = viewModelScope.launch {
        safeLaunchesCall()
    }

    fun getUpcomingLaunchesFromRoom() = spacexRepository.getUpcomingLaunchesRoom()

    fun getPastLaunchesFromRoom() = spacexRepository.getPastLaunchesRoom()

    suspend fun saveLaunchesDataIntoRoom(launch: Launch) = viewModelScope.launch {
        spacexRepository.upsertLaunch(launch)
    }

    fun searchLaunches(detail: String) = spacexRepository.searchLaunches(detail)


    private fun handleLaunchResponse(response: Response<LaunchesResponse>): Resource<LaunchesResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                if (launchesResponse == null) {
                    launchesResponse = resultResponse
                } else {
                    val old = launchesResponse
                    val new = resultResponse
                    old?.addAll(new)
                }
                return Resource.Success(launchesResponse ?: resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

    private suspend fun safeLaunchesCall() {
        launches.postValue(Resource.Loading())
        try {
            if (isConnected()) {
                val response = spacexRepository.getLaunchesAPI()
                launches.postValue(handleLaunchResponse(response))
                response.body()?.forEach {
                    saveLaunchesDataIntoRoom(it)
                }
            } else {
                launches.postValue(Resource.Error("No internet connection"))
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> launches.postValue(Resource.Error("Network Failure"))
                else -> launches.postValue(Resource.Error(t.message.toString()))
            }
        }
    }

//    private fun isConnected(): Boolean {
//        val connectivityManager = getApplication<SpacexApplication>()
//            .getSystemService(
//                Context.CONNECTIVITY_SERVICE
//            ) as ConnectivityManager
//        val activeNetwork = connectivityManager.activeNetwork ?: return false
//        val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
//        return when {
//            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
//            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
//            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
//            else -> false
//        }
//    }

}