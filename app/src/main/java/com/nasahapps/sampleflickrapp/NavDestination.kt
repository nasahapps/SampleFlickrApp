package com.nasahapps.sampleflickrapp

import com.nasahapps.sampleflickrapp.api.FlickrPhoto

sealed class NavDestination {
    object Photos : NavDestination() {
        val route = "photos"
    }

    data class PhotoDetails(val photo: FlickrPhoto) : NavDestination() {
        companion object {
            val argPhoto = "photo"
            val route = "photo/{${argPhoto}}"
        }
    }
}