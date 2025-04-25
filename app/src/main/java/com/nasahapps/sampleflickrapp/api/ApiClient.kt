package com.nasahapps.sampleflickrapp.api

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.request
import io.ktor.serialization.kotlinx.json.json
import io.ktor.util.reflect.TypeInfo
import kotlinx.serialization.json.Json
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.reflect.KClass

interface ApiClient {
    suspend fun <T> request(url: String, responseType: KClass<*>): T
}

@Singleton
class ApiClientImpl @Inject constructor() : ApiClient {

    private val httpClient = HttpClient(OkHttp) {
        expectSuccess = true
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
            })
        }
    }

    override suspend fun <T> request(url: String, responseType: KClass<*>): T {
        val response = httpClient.request(url)
        return response.body(TypeInfo(responseType))
    }

}