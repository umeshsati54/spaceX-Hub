package com.usati.spacex.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.usati.spacex.models.launch.Launch

@Dao
interface LaunchDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(launch: Launch)

    @Query("SELECT * FROM launches WHERE upcoming = 1")
    fun getUpcomingLaunches(): LiveData<List<Launch>>

    @Query("SELECT * FROM launches WHERE upcoming = 0")
    fun getPastLaunches(): LiveData<List<Launch>>

    @Query("SELECT * FROM launches WHERE details LIKE :detail")
    fun getSearchLaunches(detail: String): LiveData<List<Launch>>
}