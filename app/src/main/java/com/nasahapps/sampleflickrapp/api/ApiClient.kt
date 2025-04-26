package com.nasahapps.sampleflickrapp.api

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.request
import io.ktor.util.reflect.TypeInfo
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.reflect.KClass

interface ApiClient {
    suspend fun <T> request(url: String, responseType: KClass<*>): T
}

@Singleton
class ApiClientImpl @Inject constructor(
    private val httpClient: HttpClient
) : ApiClient {

    override suspend fun <T> request(url: String, responseType: KClass<*>): T {
        val response = httpClient.request(url)
        return response.body(TypeInfo(responseType))
    }

}