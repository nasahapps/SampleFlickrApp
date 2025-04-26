package com.nasahapps.sampleflickrapp.api

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class FlickrPhoto(
    val id: String,
    val secret: String,
    val server: String,
    val title: String,
) : Parcelable