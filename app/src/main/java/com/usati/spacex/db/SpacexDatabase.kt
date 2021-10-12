package com.usati.spacex.db

import android.content.Context
import androidx.room.*
import com.usati.spacex.models.rocket.Rocket

@Database(
    entities = [Rocket::class],
    version = 1
)
@TypeConverters(Converter::class)
abstract class SpacexDatabase : RoomDatabase() {
    abstract fun getRocketDao(): RocketDao

    companion object {
        @Volatile
        private var INSTANCE: SpacexDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = INSTANCE ?: synchronized(LOCK) {
            INSTANCE ?: createDatabase(context).also { INSTANCE = it }
        }

        private fun createDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                SpacexDatabase::class.java,
                "spacexDB.db"
            ).build()
    }
}