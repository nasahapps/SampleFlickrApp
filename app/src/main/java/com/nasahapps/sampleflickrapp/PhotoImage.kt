package com.nasahapps.sampleflickrapp

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import coil3.compose.AsyncImage
import com.nasahapps.sampleflickrapp.api.FlickrPhoto

@Composable
fun PhotoImage(
    photo: FlickrPhoto,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Crop
) {
    AsyncImage(
        model = "https://live.staticflickr.com/${photo.server}/${photo.id}_${photo.secret}.jpg",
        contentDescription = null,
        modifier = modifier,
        contentScale = contentScale
    )
}