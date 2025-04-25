package com.nasahapps.sampleflickrapp

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nasahapps.sampleflickrapp.api.FlickrPhoto

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PhotosView(
    viewModel: PhotosViewModel = hiltViewModel(),
    contentPadding: PaddingValues,
    snackbarHostState: SnackbarHostState,
    onPhotoClick: (FlickrPhoto) -> Unit
) {
    val viewState by viewModel.viewState.collectAsStateWithLifecycle()
    val searchViewState by viewModel.searchViewState.collectAsStateWithLifecycle()
    val paginationHasError by viewModel.paginationHasError.collectAsStateWithLifecycle()
    val textFieldState = rememberTextFieldState()
    var searchBarExpanded by rememberSaveable { mutableStateOf(false) }
    var isSearching by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        if (!viewModel.didInit) {
            viewModel.getRecentPhotos()
        }
    }

    LaunchedEffect(paginationHasError) {
        if (paginationHasError) {
            val result = snackbarHostState.showSnackbar(
                message = "Pagination error",
                duration = SnackbarDuration.Short
            )
            when (result) {
                SnackbarResult.Dismissed -> {
                    viewModel.resetPaginationError()
                }

                else -> {}
            }
        }
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(24.dp),
    ) {
        SearchBar(
            inputField = {
                SearchBarDefaults.InputField(
                    query = textFieldState.text.toString(),
                    onQueryChange = { textFieldState.edit { replace(0, length, it) } },
                    onSearch = {
                        viewModel.search(textFieldState.text.toString())
                        searchBarExpanded = false
                        isSearching =
                            textFieldState.text.isNotBlank() && textFieldState.text.isNotEmpty()
                    },
                    expanded = searchBarExpanded,
                    onExpandedChange = { searchBarExpanded = it },
                    placeholder = { Text("Search") },
                    trailingIcon = {
                        if (searchBarExpanded) {
                            TextButton(onClick = {
                                textFieldState.edit { replace(0, length, "") }
                                searchBarExpanded = false
                                isSearching = false
                            }) {
                                Icon(
                                    Icons.Default.Close,
                                    contentDescription = null
                                )
                            }
                        }
                    }
                )
            },
            expanded = searchBarExpanded,
            onExpandedChange = { searchBarExpanded = it }
        ) {
            Content(
                viewState = searchViewState,
                contentPadding = null,
                onClick = { onPhotoClick(it) },
                onErrorButtonClick = { viewModel.search(textFieldState.text.toString()) },
                onPagination = { viewModel.searchForMore(textFieldState.text.toString()) }
            )
        }

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Content(
                viewState = if (isSearching) searchViewState else viewState,
                contentPadding = contentPadding,
                onClick = { onPhotoClick(it) },
                onErrorButtonClick = {
                    if (isSearching) {
                        viewModel.search(textFieldState.text.toString())
                    } else {
                        viewModel.getRecentPhotos()
                    }
                },
                onPagination = {
                    if (isSearching) {
                        viewModel.searchForMore(textFieldState.text.toString())
                    } else {
                        viewModel.getMoreRecentPhotos()
                    }
                }
            )
        }
    }
}

@Composable
private fun Content(
    viewState: PhotosViewModel.ViewState?,
    contentPadding: PaddingValues?,
    onClick: (FlickrPhoto) -> Unit,
    onErrorButtonClick: () -> Unit,
    onPagination: () -> Unit
) {
    Box(Modifier.fillMaxSize()) {
        when (viewState) {
            is PhotosViewModel.ViewState.Loading -> {
                CircularProgressIndicator(Modifier.align(Alignment.Center))
            }

            is PhotosViewModel.ViewState.Content -> {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    contentPadding = contentPadding?.copy(top = 0.dp) ?: PaddingValues(0.dp)
                ) {
                    itemsIndexed(viewState.photos) { index, photo ->
                        Box(Modifier.aspectRatio(1f)) {
                            Surface(onClick = { onClick(photo) }) {
                                PhotoImage(
                                    photo = photo,
                                    modifier = Modifier.fillMaxSize()
                                )
                            }
                        }

                        LaunchedEffect(index) {
                            if (index >= (viewState.photos.size - 5)) {
                                onPagination()
                            }
                        }
                    }
                }
            }

            is PhotosViewModel.ViewState.Error -> {
                Column(
                    modifier = Modifier.align(Alignment.Center),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Error fetching photos")
                    Button(onClick = onErrorButtonClick) {
                        Text("Retry")
                    }
                }
            }

            else -> {}
        }
    }
}