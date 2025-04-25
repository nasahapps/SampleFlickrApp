package com.nasahapps.sampleflickrapp

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.nasahapps.sampleflickrapp.api.FlickrPhoto

@Composable
fun PhotoDetailsView(
    photo: FlickrPhoto,
    contentPadding: PaddingValues
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
        contentAlignment = Alignment.Center
    ) {
        PhotoImage(
            photo = photo,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Fit
        )

        Text(
            text = photo.title,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(contentPadding.adding(start = 16.dp, bottom = 16.dp, end = 16.dp)),
            textAlign = TextAlign.Start,
            color = Color.White,
            maxLines = 4,
            overflow = TextOverflow.Ellipsis,
        )
    }
}