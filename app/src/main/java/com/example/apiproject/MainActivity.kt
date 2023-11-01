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
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

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
        // Add the AppHeader at the top of the navigation host
        AppHeader(navController = navController)

        // Use the available space below the header for the NavHost
        Box(modifier = Modifier.weight(1f)) {
            NavHost(navController, startDestination = NavigationDestinations.SEARCH_SCREEN) {
                composable(route = NavigationDestinations.SEARCH_SCREEN) {
                    SearchScreen(onNavigate = {
                        navController.navigate(NavigationDestinations.PODCAST_DETAIL)
                    })
                }
                composable(route = NavigationDestinations.PODCAST_DETAIL) {
                    PodcastDetailScreen()
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
