package com.example.asteroidradar.domain.models

import com.squareup.moshi.Json

data class PictureOfDay(
    var url: String,
    @Json(name = "media_type")
    val mediaType: String,
    val title: String
)
