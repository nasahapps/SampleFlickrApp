package com.nasahapps.sampleflickrapp.api

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

interface FlickrApi {
    suspend fun getRecentPhotos(page: Int = 1): PhotosResponse
    suspend fun searchForPhotos(query: String, page: Int = 1): PhotosResponse
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

        class RecentPhotos(val page: Int) : Method {
            override val value = "flickr.photos.getRecent"
            override fun getExtraParams(): String {
                return "page=$page"
            }
        }
        class Search(val query: String, val page: Int) : Method {
            override val value = "flickr.photos.search"
            override fun getExtraParams(): String {
                return "text=$query&page=$page"
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

    override suspend fun getRecentPhotos(page: Int): PhotosResponse = withContext(Dispatchers.IO) {
        apiClient.request(
            url = buildUrlForMethod(Method.RecentPhotos(page)),
            responseType = PhotosResponse::class
        )
    }

    override suspend fun searchForPhotos(query: String, page: Int): PhotosResponse = withContext(Dispatchers.IO) {
        apiClient.request(
            url = buildUrlForMethod(Method.Search(query, page)),
            responseType = PhotosResponse::class
        )
    }

}