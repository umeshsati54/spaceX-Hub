package com.usati.spacex.models.rocket

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "rockets"
)
data class Rocket(
    val active: Boolean?,
    val boosters: Int?,
    val company: String?,
    val cost_per_launch: Int?,
    val country: String?,
    val description: String?,
    @Embedded(prefix = "diameter") val diameter: LineSize,
    @Embedded(prefix = "engines") val engines: Engines,
    val first_flight: String?,
    @Embedded(prefix = "firstStage") val first_stage: FirstStage,
    val flickr_images: List<String>?,
    @Embedded(prefix = "height") val height: LineSize,
    @PrimaryKey val id: String,
    @Embedded val landing_legs: LandingLegs,
    @Embedded val mass: Mass?,
    val name: String?,
    val payload_weights: List<PayloadWeight>?,
    @Embedded(prefix = "secondStage") val second_stage: SecondStage,
    val stages: Int?,
    val success_rate_pct: Int?,
    val type: RocketType?,
    val wikipedia: String?
)