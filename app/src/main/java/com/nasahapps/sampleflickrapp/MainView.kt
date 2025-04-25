package com.nasahapps.sampleflickrapp

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
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
    val searchPhotos by viewModel.searchPhotos.collectAsStateWithLifecycle()
    val textFieldState = rememberTextFieldState()
    var searchBarExpanded by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        if (!viewModel.didInit) {
            viewModel.getRecentPhotos()
        }
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
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
                    },
                    expanded = searchBarExpanded,
                    onExpandedChange = { searchBarExpanded = it },
                    placeholder = { Text("Search") },
                    trailingIcon = {
                        if (searchBarExpanded) {
                            TextButton(onClick = { searchBarExpanded = false }) {
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
            LazyVerticalGrid(
                columns = GridCells.Fixed(3)
            ) {
                items(searchPhotos) { photo ->
                    Box(Modifier.aspectRatio(1f)) {
                        AsyncImage(
                            model = "https://live.staticflickr.com/${photo.server}/${photo.id}_${photo.secret}.jpg",
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    }
                }
            }
        }

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            when (val state = viewState) {
                is MainViewModel.ViewState.Loading -> {
                    CircularProgressIndicator()
                }

                is MainViewModel.ViewState.Content -> {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(3),
                        contentPadding = contentPadding
                    ) {
                        items(state.photos) { photo ->
                            Box(Modifier.aspectRatio(1f)) {
                                AsyncImage(
                                    model = "https://live.staticflickr.com/${photo.server}/${photo.id}_${photo.secret}.jpg",
                                    contentDescription = null,
                                    modifier = Modifier.fillMaxSize(),
                                    contentScale = ContentScale.Crop
                                )
                            }
                        }
                    }
                }

                is MainViewModel.ViewState.Error -> {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("Error fetching recent photos")
                        Button(onClick = { viewModel.getRecentPhotos() }) {
                            Text("Retry")
                        }
                    }
                }
            }
        }
    }
}