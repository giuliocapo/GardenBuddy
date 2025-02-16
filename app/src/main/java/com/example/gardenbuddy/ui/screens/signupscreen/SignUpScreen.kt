package com.example.gardenbuddy.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.gardenbuddy.utils.toDateOrNull
import java.util.Date

@Composable
fun SignUpScreen(
    navController: NavController,
    signUpViewModel: SignUpScreenViewModel = viewModel(),
    sharedUserViewModel : SharedUserViewModel
) {
    val errorMessage by signUpViewModel.errorMessage.collectAsState()
    val isLoading by signUpViewModel.isLoading.collectAsState()
    val signUpSuccess by signUpViewModel.signUpSuccess.collectAsState()

    // Se la registrazione ha successo, navighiamo al profilo
    LaunchedEffect(signUpSuccess) {
        signUpSuccess?.let { user ->
            sharedUserViewModel.setUser(user)
            navController.navigate("userProfile/${user.userId}") {
                // Rimuove la SignUpScreen dallo stack
                popUpTo("signUp") { inclusive = true }
            }
        }
    }

    // Stati per i vari campi
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var weight by remember { mutableStateOf("") }
    var birthdate by remember { mutableStateOf("") }

    Column (Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center){

        Text(text = "Sign Up", color = Color.Black, fontSize = 24.sp, fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Name") }
        )

        Spacer(modifier = Modifier.height(8.dp))

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

        OutlinedTextField(
            value = weight,
            onValueChange = { weight = it },
            label = { Text("Weight") }
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = birthdate,
            onValueChange = { birthdate = it },
            label = { Text("Birthdate (yyyy-MM-dd)") }
        )

        if (errorMessage.isNotEmpty()) {
            Text(text = "Error: $errorMessage")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                val parsedWeight = weight.toDoubleOrNull() ?: 0.0
                val parsedBirthdate = birthdate.toDateOrNull() ?: Date()
                signUpViewModel.signUp(email, password, name, parsedWeight, parsedBirthdate)
            },
            enabled = !isLoading
        ) {
            Text("Sign Up")
        }
    }
}
