package com.usati.spacex.models.rocket

import androidx.room.Embedded

data class SecondStage(
    val burn_time_sec: Float?,
    val engines: Int?,
    val fuel_amount_tons: Float?,
    @Embedded val payloads: Payloads?,
    val reusable: Boolean?,
    @Embedded val thrust: Thrust
)