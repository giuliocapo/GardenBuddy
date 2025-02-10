package com.example.gardenbuddy.ui.screens.loginscreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.gardenbuddy.ui.screens.SharedUserViewModel
import com.example.gardenbuddy.ui.theme.GardenBuddyTheme
import javax.inject.Inject


@Composable
fun LogInScreen(
    navController: NavController,
    logInViewModel: LogInScreenViewModel = hiltViewModel(),
    sharedUserViewModel : SharedUserViewModel
) {
    val errorMessage by logInViewModel.errorMessage.collectAsState()
    val isLoading by logInViewModel.isLoading.collectAsState()
    val logInSuccess by logInViewModel.logInSuccess.collectAsState()

    // Se login ha successo, navighiamo al profilo
    LaunchedEffect(logInSuccess) {
        logInSuccess?.let { user ->
            sharedUserViewModel.setUser(user)
            navController.navigate("home") {
                // Rimuove LogInScreen dallo stack
                popUpTo("logIn") { inclusive = true }
            }
        }
    }

    // Stati per email e password
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }


    Column(
        Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Log In",
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            textStyle = MaterialTheme.typography.bodyLarge.copy(color = Color.Black),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFF4CAF50),    // Green when focused
                unfocusedBorderColor = Color(0xFF4CAF50),  // Green when unfocused
                errorBorderColor = Color.Red
            )
//                    colors = TextFieldDefaults.outlinedTextFieldColors(
//                        // Colore del testo quando è in focus o no
//                        focusedTextColor = MaterialTheme.colorScheme.onPrimary,
//                        unfocusedTextColor = MaterialTheme.colorScheme.onPrimary,
//
//                        // Bordo quando è in focus o no
//                        focusedBorderColor = MaterialTheme.colorScheme.primary,
//                        unfocusedBorderColor = Color.Gray,
//
//                        // Label quando è in focus o no
//                        focusedLabelColor = MaterialTheme.colorScheme.primary,
//                        unfocusedLabelColor = Color.Gray,
//                    )
        )

        Spacer(modifier = Modifier.height(8.dp))

        PasswordTextField(
            value = password,
            onValueChange = { password = it },
            label = "Password"
        )

        Spacer(modifier = Modifier.height(8.dp))

        if (errorMessage.isNotEmpty()) {
            Text(text = "Error: $errorMessage", color = Color.Red)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { logInViewModel.logIn(email, password) },
            enabled = !isLoading
        ) {
            Text("Log In")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            HorizontalDivider(Modifier.width(50.dp))
            Box(Modifier.padding(10.dp, 0.dp)) {
                Text("or")
            }
            HorizontalDivider(Modifier.width(50.dp))
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



