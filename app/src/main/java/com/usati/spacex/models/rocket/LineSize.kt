package com.usati.spacex.models.rocket

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LineSize(
    val feet: Float?,
    val meters: Float?
)