package com.usati.spacex

import com.usati.spacex.api.RetrofitInstance
import com.usati.spacex.db.SpacexDatabase
import com.usati.spacex.models.launch.Launch
import com.usati.spacex.models.rocket.Rocket

class SpacexRepository(
    private val db: SpacexDatabase
) {
    suspend fun getRocketsAPI() =
        RetrofitInstance.api.getRockets()

    suspend fun getLaunchesAPI() =
        RetrofitInstance.api.getLaunches()

    fun getRocketsRoom() = db.getRocketDao().getAllRockets()

    suspend fun upsertRocket(rocket: Rocket) = db.getRocketDao().upsert(rocket)

    fun getUpcomingLaunchesRoom() = db.getLaunchDao().getUpcomingLaunches()
    fun getPastLaunchesRoom() = db.getLaunchDao().getPastLaunches()

    fun searchLaunches(details: String) = db.getLaunchDao().getSearchLaunches(details)

    suspend fun upsertLaunch(launch: Launch) = db.getLaunchDao().upsert(launch)

}