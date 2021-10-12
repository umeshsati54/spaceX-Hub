package com.usati.spacex.models.rocket

import androidx.room.Embedded

data class FirstStage(
    val burn_time_sec: Float?,
    val engines: Int?,
    val fuel_amount_tons: Float?,
    val reusable: Boolean?,
    @Embedded(prefix = "seaLevel") val thrust_sea_level: Thrust,
    @Embedded(prefix = "vacuumLevel") val thrust_vacuum: Thrust
)