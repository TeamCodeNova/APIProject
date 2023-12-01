package com.example.apiproject.Screens

import androidx.compose.runtime.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.example.apiproject.Database.DBHandler
import com.example.apiproject.Navigation.NavigationDestinations
import com.example.apiproject.Sessions.UserSessionManager

@Composable
fun AccountScreen(navController: NavController) {
    val context = LocalContext.current
    val userSessionManager = UserSessionManager(context)
    val dbHandler = DBHandler(context)

    // Redirect to login if not logged in
    LaunchedEffect(Unit) {
        if (!userSessionManager.isUserLoggedIn()) {
            Home(navController)
        }
    }

    val userDetails = dbHandler.getUserDetails()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        userDetails?.let {
            Text(
                text = "Username: ${it.first}",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Password: ${it.second}",  // Note: Generally not recommended to display passwords
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
        } ?: run {
            Text(
                text = "No user information available",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
        }
    }
}

fun Home(navController: NavController) {
    navController.navigate(NavigationDestinations.LOGIN_SCREEN)
}