package com.example.apiproject

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.compose.material3.Text

/// <summary>
/// The constructor of this class
/// </summary>
@Composable
fun AccountScreen() {
    ScreenPlaceholder(title = "Account Screen")
}

/// <summary>
/// Displays the Account Screen, State of screen can be changed
/// </summary>
/// <param="title">Title of the Page</title>
@Composable
private fun ScreenPlaceholder(title: String) {
    Text(
        text = title,
        fontSize = 24.sp,
        fontWeight = FontWeight.Bold,
        color = Color.Black,
        textAlign = TextAlign.Center
    )
}