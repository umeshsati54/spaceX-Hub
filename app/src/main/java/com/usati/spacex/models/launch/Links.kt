package com.usati.spacex.models.launch

import androidx.room.Embedded

data class Links(
    val article: String?,
    @Embedded(prefix = "flickr") val flickr: Flickr?,
    @Embedded(prefix = "patch") val patch: Patch?,
    val presskit: String?,
    @Embedded val reddit: Reddit?,
    val webcast: String?,
    val wikipedia: String?,
    val youtube_id: String?
)