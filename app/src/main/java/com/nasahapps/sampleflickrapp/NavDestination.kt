package com.nasahapps.sampleflickrapp

import com.nasahapps.sampleflickrapp.api.FlickrPhoto

// Although the latest "stable" version of Compose Navigation allows using types for routes
// vs strings, that implementation had too many bugs to deal with, so I chose to go with
// string types for routes
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