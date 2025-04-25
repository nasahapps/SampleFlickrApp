package com.nasahapps.sampleflickrapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nasahapps.sampleflickrapp.api.FlickrApi
import com.nasahapps.sampleflickrapp.api.FlickrPhoto
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val flickrApi: FlickrApi
) : ViewModel() {

    private val _photos = MutableStateFlow<List<FlickrPhoto>>(emptyList())
    val photos: StateFlow<List<FlickrPhoto>> = _photos.asStateFlow()

    fun getData() {
        viewModelScope.launch {
            val resp = flickrApi.getRecentPhotos().photos.photo
            _photos.update { resp }
        }
    }

}