package com.example.apiproject

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton

/// <summary>
/// The global app header
/// </summary>
/// <param="navController">The navigation menu object</param>
@Composable
fun AppHeader(navController: NavController) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        TextButton(
            onClick = { navController.navigate(NavigationDestinations.SEARCH_SCREEN) },
            modifier = Modifier.weight(1f)
        ) {
            Text(text = "Home")
        }

        TextButton(
            onClick = { navController.navigate(NavigationDestinations.FAVORITES_SCREEN) },
            modifier = Modifier.weight(1f)
        ) {
            Text(text = "Favorites")
        }

        TextButton(
            onClick = { navController.navigate(NavigationDestinations.ABOUT_SCREEN) },
            modifier = Modifier.weight(1f)
        ) {
            Text(text = "About")
        }

        TextButton(
            onClick = { navController.navigate(NavigationDestinations.ACCOUNT_SCREEN) },
            modifier = Modifier.weight(1f)
        ) {
            Text(text = "Account")
        }
    }
}

