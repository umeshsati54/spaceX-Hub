package com.usati.spacex.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.usati.spacex.models.rocket.Rocket

@Dao
interface RocketDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(rockets: Rocket)

    @Query("SELECT * FROM rockets")
    fun getAllRockets() : LiveData<List<Rocket>>
}