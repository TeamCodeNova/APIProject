package com.example.apiproject

import androidx.compose.runtime.Composable
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
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
    NavHost(navController, startDestination = NavigationDestinations.SEARCH_SCREEN) {
        composable(route = NavigationDestinations.SEARCH_SCREEN) {
            SearchScreen(onNavigate = {
                navController.navigate(NavigationDestinations.PODCAST_DETAIL)
            })
        }
        composable(route = NavigationDestinations.PODCAST_DETAIL) {
            PodcastDetailScreen()
        }
    }
}
