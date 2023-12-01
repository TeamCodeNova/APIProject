package com.example.apiproject.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.example.apiproject.navigation.NavigationDestinations
import com.example.apiproject.sessions.UserSessionManager

@Composable
fun AboutScreen(navController: NavController) {
    val context = LocalContext.current
    val userSessionManager = UserSessionManager(context)

    // Redirect to login if not logged in
    LaunchedEffect(Unit) {
        if (!userSessionManager.isUserLoggedIn()) {
            BackHome(navController)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "About CastNova",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Text(
            text = "Welcome to CastNova, the podcast application brought to you by CodeNove!",
            fontSize = 16.sp,
            color = Color.Gray,
            textAlign = TextAlign.Center,
            lineHeight = 24.sp,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Text(
            text = "Our Team:",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            textAlign = TextAlign.Left,
            lineHeight = 28.sp,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Text(
            text = "- Kyle",
            fontSize = 16.sp,
            color = Color.Black,
            textAlign = TextAlign.Left,
            lineHeight = 24.sp,
            modifier = Modifier.padding(start = 16.dp, bottom = 8.dp)
        )

        Text(
            text = "- Miguel",
            fontSize = 16.sp,
            color = Color.Black,
            textAlign = TextAlign.Left,
            lineHeight = 24.sp,
            modifier = Modifier.padding(start = 16.dp, bottom = 8.dp)
        )

        Text(
            text = "- Michael",
            fontSize = 16.sp,
            color = Color.Black,
            textAlign = TextAlign.Left,
            lineHeight = 24.sp,
            modifier = Modifier.padding(start = 16.dp, bottom = 16.dp)
        )

        Text(
            text = "About CastNova:",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            textAlign = TextAlign.Left,
            lineHeight = 28.sp,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Text(
            text = "CastNova is a podcast application that allows users to browse a curated list of podcasts and save their favorite ones to their collection, making it easy to access and enjoy your favorite shows anytime, anywhere.",
            fontSize = 16.sp,
            color = Color.Black,
            textAlign = TextAlign.Left,
            lineHeight = 24.sp,
            modifier = Modifier.padding(start = 16.dp, end = 16.dp)
        )
    }
}

fun BackHome(navController: NavController) {
    navController.navigate(NavigationDestinations.LOGIN_SCREEN)
}
