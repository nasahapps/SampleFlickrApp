package com.nasahapps.sampleflickrapp.api

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

interface FlickrApi {
    suspend fun getRecentPhotos(): PhotosResponse
    suspend fun searchForPhotos(query: String): PhotosResponse
}

@Singleton
class FlickrApiImpl @Inject constructor(
    private val apiClient: ApiClient
) : FlickrApi {

    companion object {
        const val API_KEY = "a0222db495999c951dc33702500fdc4d"
        const val BASE_API_URL = "https://www.flickr.com/services/rest/"
    }

    private sealed interface Method {
        val value: String
        fun getExtraParams(): String {
            return ""
        }

        data object RecentPhotos : Method {
            override val value = "flickr.photos.getRecent"
        }
        class Search(val query: String) : Method {
            override val value = "flickr.photos.search"
            override fun getExtraParams(): String {
                return "text=$query"
            }
        }
    }

    private fun buildUrlForMethod(method: Method): String {
        var url = "$BASE_API_URL?method=${method.value}&api_key=$API_KEY&format=json&nojsoncallback=1"
        val extraParams = method.getExtraParams()
        if (extraParams.isNotEmpty()) {
            url += "&$extraParams"
        }
        return url
    }

    override suspend fun getRecentPhotos(): PhotosResponse = withContext(Dispatchers.IO) {
        apiClient.request(
            url = buildUrlForMethod(Method.RecentPhotos),
            responseType = PhotosResponse::class
        )
    }

    override suspend fun searchForPhotos(query: String): PhotosResponse = withContext(Dispatchers.IO) {
        apiClient.request(
            url = buildUrlForMethod(Method.Search(query)),
            responseType = PhotosResponse::class
        )
    }

}