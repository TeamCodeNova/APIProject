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

class MainActivity : ComponentActivity() {
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
                composable(route = NavigationDestinations.FAVORITES_SCREEN) {
                    FavoritesScreen()
                }
                composable(route = NavigationDestinations.ABOUT_SCREEN) {
                    AboutScreen()
                }
                composable(route = NavigationDestinations.ACCOUNT_SCREEN) {
                    AccountScreen()
                }
            }
        }
    }
}
