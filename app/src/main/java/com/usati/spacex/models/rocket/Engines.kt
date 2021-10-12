package com.usati.spacex.models.rocket

import androidx.room.Embedded

data class Engines(
    val engine_loss_max: String?,
    @Embedded val isp: Isp,
    val layout: String?,
    val number: Int?,
    val propellant_1: String?,
    val propellant_2: String?,
    @Embedded(prefix = "seaLevel") val thrust_sea_level: Thrust,
    val thrust_to_weight: Float?,
    @Embedded(prefix = "vacuumLevel") val thrust_vacuum: Thrust,
    val type: String?,
    val version: String?
)