package com.example.gardenbuddy.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController

@Composable
fun LogInScreen(
    navController: NavController,
    logInViewModel: LogInScreenViewModel = viewModel()
) {
    val errorMessage by logInViewModel.errorMessage.collectAsState()
    val isLoading by logInViewModel.isLoading.collectAsState()
    val logInSuccess by logInViewModel.logInSuccess.collectAsState()

    // Se login ha successo, navighiamo al profilo
    LaunchedEffect(logInSuccess) {
        logInSuccess?.let { user ->
            navController.navigate("userProfile/${user.userId}") {
                // Rimuove LogInScreen dallo stack
                popUpTo("logIn") { inclusive = true }
            }
        }
    }

    // Stati per email e password
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column {
        Text(text = "Log In")

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") }
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") }
        )

        Spacer(modifier = Modifier.height(8.dp))

        if (errorMessage.isNotEmpty()) {
            Text(text = "Error: $errorMessage")
        }

        Button(
            onClick = { logInViewModel.logIn(email, password) },
            enabled = !isLoading
        ) {
            Text(if (isLoading) "Loading..." else "Log In")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Pulsante per navigare alla schermata Sign Up
        Button(onClick = {
            navController.navigate("signUp")
        }) {
            Text("Don't have an account? Sign Up")
        }
    }
}
