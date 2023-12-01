package com.example.apiproject.Screens

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import android.widget.Toast
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.ui.Alignment
import androidx.navigation.NavController
import com.example.apiproject.Database.DBHandler
import com.example.apiproject.Navigation.NavigationDestinations

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistrationScreen(navController: NavController) {
    val context = LocalContext.current
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val dbHandler = DBHandler(context)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Username") },
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next)
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = { handleRegistration(navController, username, password, dbHandler, context) })
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { handleRegistration(navController, username, password, dbHandler, context) }
        ) {
            Text("Register")
        }
    }
}



private fun handleRegistration(navController: NavController, username: String, password: String, dbHandler: DBHandler, context: Context) {

    if (username.isNotBlank() && password.isNotBlank()) {
        val result = dbHandler.registerUser(username, password)
        if (result > 0) {
            Toast.makeText(context, "Registration successful", Toast.LENGTH_LONG).show()
            LoginNow(navController) // Corrected function name
        } else {
            Toast.makeText(context, "Registration failed", Toast.LENGTH_LONG).show()
        }
    } else {
        Toast.makeText(context, "Please fill all fields", Toast.LENGTH_LONG).show()
    }
}

fun LoginNow(navController: NavController) {
    navController.navigate(NavigationDestinations.LOGIN_SCREEN)
}

