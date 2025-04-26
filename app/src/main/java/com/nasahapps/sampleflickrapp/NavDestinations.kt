package com.nasahapps.sampleflickrapp

import android.os.Bundle
import androidx.core.os.BundleCompat
import androidx.navigation.NavType
import com.nasahapps.sampleflickrapp.api.FlickrPhoto
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

object NavDestinations {
    @Serializable
    object Photos

    @Serializable
    data class PhotoDetails(val photo: FlickrPhoto)
}

val FlickrPhotoType = object : NavType<FlickrPhoto>(false) {
    override fun get(bundle: Bundle, key: String): FlickrPhoto? {
        return BundleCompat.getParcelable(bundle, key, FlickrPhoto::class.java)
    }

    override fun parseValue(value: String): FlickrPhoto {
        return Json.decodeFromString(value)
    }

    override fun serializeAsValue(value: FlickrPhoto): String {
        return Json.encodeToString(value)
    }

    override fun put(bundle: Bundle, key: String, value: FlickrPhoto) {
        bundle.putParcelable(key, value)
    }
}