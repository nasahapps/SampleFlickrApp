package com.nasahapps.sampleflickrapp

import androidx.lifecycle.ViewModel
import com.nasahapps.sampleflickrapp.api.FlickrApi
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val flickrApi: FlickrApi
) : ViewModel() {



}