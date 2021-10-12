package com.usati.spacex.api

import com.usati.spacex.models.launch.LaunchesResponse
import com.usati.spacex.models.rocket.RocketResponse
import retrofit2.Response
import retrofit2.http.GET

interface SpacexAPI {
    @GET("v4/rockets")
    suspend fun getRockets(): Response<RocketResponse>

    @GET("v5/launches")
    suspend fun getLaunches(): Response<LaunchesResponse>
}