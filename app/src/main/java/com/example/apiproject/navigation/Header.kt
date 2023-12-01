package com.example.apiproject.navigation

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import com.example.apiproject.R
import com.example.apiproject.sessions.UserSessionManager

fun logoutUser(context: Context, navController: NavController) {
    UserSessionManager(context).logoutUser()

    // Navigate to the login screen using NavController
    navController.navigate(NavigationDestinations.LOGIN_SCREEN)
}

@Composable
fun AppHeader(navController: NavController) {
    val context = LocalContext.current

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically // Align the content vertically
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

        // Adjust the size modifier for the Image to make it bigger
        Image(
            painter = painterResource(id = R.drawable.logout),
            contentDescription = "Logout Button",
            modifier = Modifier
                .size(25.dp) // Increase the size here as needed

                .clickable {
                    logoutUser(context, navController)
                }
        )
    }
}
