package com.nasahapps.sampleflickrapp.api

import kotlinx.serialization.Serializable

@Serializable
data class PhotosResponse(
    val photos: FlickrPhotoCollection
)

@Serializable
data class FlickrPhotoCollection(
    val page: Int,
    val pages: Int,
    val photo: List<FlickrPhoto>
)