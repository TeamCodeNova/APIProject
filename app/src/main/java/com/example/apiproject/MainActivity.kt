package com.example.apiproject

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import com.example.apiproject.ui.theme.APIProjectTheme
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

/// <summary>
/// The main screen of the app
/// </summary>
class MainActivity : ComponentActivity() {
    /// <summary>
    /// On startup will activate this function and set the theme and basic look of the app
    /// </summary>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            APIProjectTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    MainNavHost()
                }
            }
        }
    }
}

/// <summary>
/// The Basic navigation controller of the app
/// </summary>
@Composable
private fun MainNavHost() {
    val navController = rememberNavController()

    Column {
        AppHeader(navController = navController)

        Box(modifier = Modifier.weight(1f)) {
            NavHost(navController, startDestination = NavigationDestinations.SEARCH_SCREEN) {
                composable(route = NavigationDestinations.SEARCH_SCREEN) {
                    SearchScreen(onNavigate = { selectedPodcast ->
                        navController.navigate("${NavigationDestinations.PODCAST_DETAIL}/${selectedPodcast.uuid}")
                    })
                }
                composable(
                    route = NavigationDestinations.PODCAST_DETAIL + "/{podcastUUID}",
                    arguments = listOf(navArgument("podcastUUID") { type = NavType.StringType })
                ) { backStackEntry ->
                    val podcastUUID = backStackEntry.arguments?.getString("podcastUUID")
                    println("podcastUUID: $podcastUUID")

                    val podcast = SharedPodcastRepository.podcasts?.find { it?.uuid == podcastUUID }

                    println("Retrieved podcast: $podcast")

                    if (podcast != null) {
                        PodcastDetailScreen(podcast)
                    }
                }
                composable(route = NavigationDestinations.ABOUT_SCREEN) {
                    AboutScreen()
                }
                composable(route = NavigationDestinations.FAVORITES_SCREEN) {
                    Favorites()
                }
                composable(route = NavigationDestinations.ACCOUNT_SCREEN) {
                    AccountScreen()
                }
            }
        }
    }
}
