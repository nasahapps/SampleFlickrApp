package com.nasahapps.sampleflickrapp.di

import com.nasahapps.sampleflickrapp.api.ApiClient
import com.nasahapps.sampleflickrapp.api.ApiClientImpl
import com.nasahapps.sampleflickrapp.api.FlickrApi
import com.nasahapps.sampleflickrapp.api.FlickrApiImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class DiModule {

    @Binds
    abstract fun bindFlickrApi(flickrApiImpl: FlickrApiImpl): FlickrApi

    @Binds
    abstract fun bindApiClient(apiClientImpl: ApiClientImpl): ApiClient

}