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
import java.net.URLEncoder

@Composable
fun MainView(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues,
    snackbarHostState: SnackbarHostState,
    jsonParser: Json
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
                    // Encoding the photo here ensures it'll be correctly passed along through
                    // the NavHost
                    val arg = URLEncoder.encode(jsonParser.encodeToString(it), "utf-8")
                    val route = NavDestination.PhotoDetails.route
                        .replace("{${NavDestination.PhotoDetails.argPhoto}}", arg)
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
            val arg =
                backstackEntry.arguments?.getString(NavDestination.PhotoDetails.argPhoto) ?: "{}"
            val photo = jsonParser.decodeFromString<FlickrPhoto>(arg)
            PhotoDetailsView(
                photo = photo,
                contentPadding = contentPadding
            )
        }
    }

}