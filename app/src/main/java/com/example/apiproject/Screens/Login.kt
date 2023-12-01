package com.example.apiproject.Screens

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.runtime.Composable
import com.example.apiproject.Database.DBHandler
import com.example.apiproject.Navigation.NavigationDestinations
import com.example.apiproject.Sessions.UserSessionManager


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(navController: NavController) {
    val context = LocalContext.current
    val dbHandler = remember { DBHandler(context) }
    val userSessionManager = remember { UserSessionManager(context) }

    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column {
        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Username") },
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next)
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = { performLogin(navController, username, password, dbHandler, userSessionManager, context) })
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = { performLogin(navController, username, password, dbHandler, userSessionManager, context) }) {
            Text(text = "Login")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { navController.navigate(NavigationDestinations.REGISTRATION_SCREEN) },
        ) {
            Text(text = "Register")
        }
    }

}

private fun performLogin(navController: NavController, username: String, password: String, dbHandler: DBHandler, userSessionManager: UserSessionManager, context: Context) {
    if (username.isNotEmpty() && password.isNotEmpty()) {
        if (dbHandler.checkUser(username, password)) {
            userSessionManager.saveUserLogin(username)
            Toast.makeText(context, "Login successful", Toast.LENGTH_SHORT).show()
            // Navigate to another screen or update UI state
            navController.navigate(NavigationDestinations.SEARCH_SCREEN)
        } else {
            Toast.makeText(context, "Invalid username or password", Toast.LENGTH_SHORT).show()
        }
    } else {
        Toast.makeText(context, "Username and password cannot be empty", Toast.LENGTH_SHORT).show()
    }
}
