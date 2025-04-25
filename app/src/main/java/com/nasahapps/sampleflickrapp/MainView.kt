package com.nasahapps.sampleflickrapp

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.nasahapps.sampleflickrapp.api.FlickrPhoto
import kotlin.reflect.typeOf

@Composable
fun MainView(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues,
    snackbarHostState: SnackbarHostState
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = NavDestinations.Photos,
        modifier = modifier,
    ) {
        composable<NavDestinations.Photos> {
            PhotosView(
                contentPadding = contentPadding,
                snackbarHostState = snackbarHostState,
                onPhotoClick = {
                    navController.navigate(route = NavDestinations.PhotoDetails(it))
                }
            )
        }
        composable<NavDestinations.PhotoDetails>(
            typeMap = mapOf(typeOf<FlickrPhoto>() to FlickrPhotoType)
        ) { backstackEntry ->
            val photo = backstackEntry.toRoute<NavDestinations.PhotoDetails>()
            PhotoDetailsView(
                photo = photo.photo,
                contentPadding = contentPadding
            )
        }
    }

}