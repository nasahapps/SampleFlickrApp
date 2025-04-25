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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainView(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel = viewModel(),
    contentPadding: PaddingValues
) {
    val viewState by viewModel.viewState.collectAsStateWithLifecycle()
    val searchViewState by viewModel.searchViewState.collectAsStateWithLifecycle()
    val textFieldState = rememberTextFieldState()
    var searchBarExpanded by rememberSaveable { mutableStateOf(false) }
    var isSearching by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        if (!viewModel.didInit) {
            viewModel.getRecentPhotos()
        }
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(24.dp),
        modifier = modifier
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
    viewState: MainViewModel.ViewState?,
    contentPadding: PaddingValues?,
    onErrorButtonClick: () -> Unit,
    onPagination: () -> Unit
) {
    Box(Modifier.fillMaxSize()) {
        when (viewState) {
            is MainViewModel.ViewState.Loading -> {
                CircularProgressIndicator(Modifier.align(Alignment.Center))
            }

            is MainViewModel.ViewState.Content -> {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    contentPadding = contentPadding?.copy(top = 0.dp) ?: PaddingValues(0.dp)
                ) {
                    itemsIndexed(viewState.photos) { index, photo ->
                        Box(Modifier.aspectRatio(1f)) {
                            AsyncImage(
                                model = "https://live.staticflickr.com/${photo.server}/${photo.id}_${photo.secret}.jpg",
                                contentDescription = null,
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        }

                        LaunchedEffect(index) {
                            if (index >= (viewState.photos.size - 5)) {
                                onPagination()
                            }
                        }
                    }
                }
            }

            is MainViewModel.ViewState.Error -> {
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