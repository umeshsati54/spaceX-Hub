package com.usati.spacex.models.rocket

import androidx.room.Embedded

data class Payloads(
    @Embedded val composite_fairing: CompositeFairing?,
    val option_1: String?
)