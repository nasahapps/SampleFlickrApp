package com.nasahapps.sampleflickrapp

import android.util.Log
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

    sealed class ViewState {
        data object Loading : ViewState()
        data class Content(val photos: List<FlickrPhoto>) : ViewState()
        data object Error : ViewState()
    }

    var didInit = false
    private val _viewState = MutableStateFlow<ViewState>(ViewState.Loading)
    val viewState: StateFlow<ViewState> = _viewState.asStateFlow()
    private val _searchPhotos = MutableStateFlow<List<FlickrPhoto>>(emptyList())
    val searchPhotos: StateFlow<List<FlickrPhoto>> = _searchPhotos.asStateFlow()

    fun getRecentPhotos() {
        didInit = true
        _viewState.update { ViewState.Loading }
        viewModelScope.launch {
            val resp = flickrApi.getRecentPhotos().photos.photo
            _viewState.update { ViewState.Content(resp) }
        }
    }

    fun search(query: String) {
        Log.d("hasan", "Searching $query")
        viewModelScope.launch {
            val resp = flickrApi.searchForPhotos(query).photos.photo
            _searchPhotos.update { resp }
        }
    }

}