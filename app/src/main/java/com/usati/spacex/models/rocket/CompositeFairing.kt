package com.usati.spacex.models.rocket

import androidx.room.Embedded

data class CompositeFairing(
    @Embedded(prefix = "diameter") val diameter: LineSize?,
    @Embedded(prefix = "height") val height: LineSize?
)