package com.example.apiproject

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.material3.Text

@Composable
fun AppHeader(navController: NavController) {
    Row(modifier = Modifier.padding(16.dp)) {
        Button(onClick = { navController.navigate(NavigationDestinations.SEARCH_SCREEN) }) {
            Text(text = "Home")
        }

        Spacer(modifier = Modifier.width(8.dp))

        Button(onClick = { navController.navigate(NavigationDestinations.FAVORITES_SCREEN) }) {
            Text(text = "Favorites")
        }

        Spacer(modifier = Modifier.width(8.dp))

        Button(onClick = { navController.navigate(NavigationDestinations.ABOUT_SCREEN) }) {
            Text(text = "About")
        }

        Spacer(modifier = Modifier.width(8.dp))

        Button(onClick = { navController.navigate(NavigationDestinations.ACCOUNT_SCREEN) }) {
            Text(text = "Account")
        }
    }
}
