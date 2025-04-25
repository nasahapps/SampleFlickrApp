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
        data class Content(val photos: List<FlickrPhoto>, val nextPage: Int?, val maxPages: Int) : ViewState()
        data object Error : ViewState()
    }

    private val _viewState = MutableStateFlow<ViewState>(ViewState.Loading)
    val viewState: StateFlow<ViewState> = _viewState.asStateFlow()
    private val _searchViewState = MutableStateFlow<ViewState?>(null)
    val searchViewState: StateFlow<ViewState?> = _searchViewState.asStateFlow()
    private val _paginationHasError = MutableStateFlow(false)
    val paginationHasError: StateFlow<Boolean> = _paginationHasError.asStateFlow()

    var didInit = false
    private var isLoadingMore = false
    private var isLoadingMoreSearch = false

    fun getRecentPhotos() {
        didInit = true
        _viewState.update { ViewState.Loading }
        viewModelScope.launch {
            try {
                Log.d("hasan", "getting page one of recent photos")
                val resp = flickrApi.getRecentPhotos().photos
                val maxPages = resp.pages
                val nextPage = if (maxPages > 1) 2 else null
                _viewState.update { ViewState.Content(resp.photo, nextPage, maxPages) }
            } catch (e: Throwable) {
                Log.e("hasan", "Error fetching recent photos", e)
                _viewState.update { ViewState.Error }
            }
        }
    }

    fun getMoreRecentPhotos() {
        if (!isLoadingMore) {
            (_viewState.value as? ViewState.Content)?.let { state ->
                state.nextPage?.let { nextPage ->
                    isLoadingMore = true
                    viewModelScope.launch {
                        try {
                            Log.d("hasan", "getting page $nextPage of recent photos")
                            val resp = flickrApi.getRecentPhotos(nextPage).photos
                            val newNextPage = if (nextPage + 1 > state.maxPages) null else nextPage + 1
                            val newPhotos = state.photos + resp.photo
                            _viewState.update {
                                ViewState.Content(newPhotos, newNextPage, state.maxPages)
                            }
                        } catch (e: Throwable) {
                            Log.e("hasan", "Error fetching more recent photos", e)
                            _paginationHasError.update { true }
                        } finally {
                            isLoadingMore = false
                        }
                    }
                }
            }
        }
    }

    fun search(query: String) {
        if (query.isNotBlank()) {
            _searchViewState.update { ViewState.Loading }
            viewModelScope.launch {
                Log.d("hasan", "getting page one of search ($query)")
                val resp = flickrApi.searchForPhotos(query).photos
                val maxPages = resp.pages
                val nextPage = if (maxPages > 1) 2 else null
                _searchViewState.update { ViewState.Content(resp.photo, nextPage, maxPages) }
            }
        }
    }

    fun searchForMore(query: String) {
        if (query.isNotBlank() && !isLoadingMoreSearch) {
            (_searchViewState.value as? ViewState.Content)?.let { state ->
                state.nextPage?.let { nextPage ->
                    isLoadingMoreSearch = true
                    viewModelScope.launch {
                        try {
                            Log.d("hasan", "getting page $nextPage of search ($query)")
                            val resp = flickrApi.searchForPhotos(query, nextPage).photos
                            val newNextPage = if (nextPage + 1 > state.maxPages) null else nextPage + 1
                            val newPhotos = state.photos + resp.photo
                            _searchViewState.update {
                                ViewState.Content(newPhotos, newNextPage, state.maxPages)
                            }
                        } catch (e: Throwable) {
                            Log.e("hasan", "Error fetching more for search", e)
                            _paginationHasError.update { true }
                        } finally {
                            isLoadingMoreSearch = false
                        }
                    }
                }
            }
        }
    }

    fun resetPaginationError() {
        _paginationHasError.update { false }
    }

}