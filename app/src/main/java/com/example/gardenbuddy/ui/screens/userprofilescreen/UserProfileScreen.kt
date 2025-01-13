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
import com.example.gardenbuddy.data.models.User
import com.example.gardenbuddy.utils.toDateOrNull
import java.util.Date

@Composable
fun UserProfileScreen(
    userId: String,
    viewModel: UserProfileScreenViewModel = viewModel()
) {
    // Carica i dati dell'utente al primo avvio di questa schermata
    LaunchedEffect(userId) {
        viewModel.loadUserProfile(userId)
    }

    val userState by viewModel.userState.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    // Variabili locali per gestire input editabili
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var weight by remember { mutableStateOf("") }
    var birthDate by remember { mutableStateOf("") }

    // Quando userState cambia, aggiorno i campi di input
    LaunchedEffect(userState) {
        userState?.let { user ->
            name = user.name
            email = user.email
            weight = user.weight.toString()
            birthDate = user.birthdate.toString()
        }
    }

    Column(modifier = Modifier) {
        Text(text = "User Profile")

        // Mostra eventuali errori
        if (errorMessage.isNotEmpty()) {
            Text(text = "Error: $errorMessage")
        }

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Name") }
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            enabled = false // se non vuoi che l'utente modifichi l'email
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = weight,
            onValueChange = { weight = it },
            label = { Text("Weight") }
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = birthDate,
            onValueChange = { birthDate = it },
            label = { Text("BirthDate") }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            val currentUserId = userState?.userId ?: return@Button
            // Crea un oggetto user da salvare
            val updatedUser = User(
                userId = currentUserId,
                name = name,
                email = email,
                weight = weight.toDoubleOrNull() ?: 0.0,
                birthdate = birthDate.toDateOrNull() ?: Date()
            )
            viewModel.updateUserProfile(updatedUser)
        }) {
            Text("Save Changes")
        }
    }
}
