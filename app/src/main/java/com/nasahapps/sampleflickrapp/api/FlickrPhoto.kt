package com.nasahapps.sampleflickrapp.api

import kotlinx.serialization.Serializable

@Serializable
data class FlickrPhoto(
    val id: String,
    val secret: String,
    val server: String
)
