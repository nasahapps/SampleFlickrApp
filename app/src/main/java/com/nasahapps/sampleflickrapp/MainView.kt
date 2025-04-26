package com.nasahapps.sampleflickrapp

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.nasahapps.sampleflickrapp.api.FlickrPhoto
import kotlinx.serialization.json.Json

@Composable
fun MainView(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues,
    snackbarHostState: SnackbarHostState
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = NavDestination.Photos.route,
        modifier = modifier,
    ) {
        composable(NavDestination.Photos.route) {
            PhotosView(
                contentPadding = contentPadding,
                snackbarHostState = snackbarHostState,
                onPhotoClick = {
                    val route = NavDestination.PhotoDetails.route
                        .replace(
                            "{${NavDestination.PhotoDetails.argPhoto}}",
                            Json.encodeToString(it)
                        )
                    navController.navigate(route)
                }
            )
        }
        composable(
            route = NavDestination.PhotoDetails.route,
            arguments = listOf(
                navArgument(NavDestination.PhotoDetails.argPhoto) {
                    type = NavType.StringType
                    nullable = false
                }
            )
        ) { backstackEntry ->
            val photo = Json.decodeFromString<FlickrPhoto>(
                backstackEntry.arguments?.getString(NavDestination.PhotoDetails.argPhoto) ?: "{}"
            )
            PhotoDetailsView(
                photo = photo!!,
                contentPadding = contentPadding
            )
        }
    }

}