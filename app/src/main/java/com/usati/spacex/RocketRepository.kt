package com.usati.spacex

import com.usati.spacex.api.RetrofitInstance
import com.usati.spacex.db.SpacexDatabase
import com.usati.spacex.models.rocket.Rocket

class RocketRepository(
    private val db: SpacexDatabase
) {
    suspend fun getRocketsAPI() =
        RetrofitInstance.api.getRockets()

    fun getRocketsRoom() = db.getRocketDao().getAllRockets()

    suspend fun upsert(rocket: Rocket) = db.getRocketDao().upsert(rocket)

}