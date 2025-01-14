package com.example.gardenbuddy.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.gardenbuddy.R
import com.example.gardenbuddy.data.models.User
import com.example.gardenbuddy.ui.theme.darkGreen
import com.example.gardenbuddy.ui.theme.softGreen
import com.example.gardenbuddy.utils.toFormattedString
import java.text.SimpleDateFormat
import java.util.*


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
            birthDate = user.birthdate?.toFormattedString().toString()// Formatta la data
        }
    }

    UserProfileContent(
        userId = userId,
        name = name,
        onNameChange = { name = it },
        email = email,
        weight = weight,
        onWeightChange = { weight = it },
        birthDate = birthDate,
        onBirthDateChange = { birthDate = it }
    )
}

@Composable
fun UserProfileContent(
    userId: String,
    name: String,
    onNameChange: (String) -> Unit,
    email: String,
    weight: String,
    onWeightChange: (String) -> Unit,
    birthDate: String,
    onBirthDateChange: (String) -> Unit,
    viewModel: UserProfileScreenViewModel = viewModel()) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(softGreen) // Verde tenue
    ) {
        // Barra in alto
        // Barra in alto con pulsante Salva
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .background(darkGreen), // Verde più scuro
            contentAlignment = Alignment.CenterStart
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "User Profile",
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.White
                )

                // Pulsante Salva
                TextButton(onClick = { viewModel.updateUserProfile(User(userId = userId, name = name, weight = weight.toDouble(), email = email, birthdate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(birthDate))) }) {
                    Text(
                        text = "Salva",
                        color = Color.White,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }

        // Contenuto del profilo
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Immagine del profilo (luna)
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .background(Color.Gray)
                    .clickable {
                        // Aggiungi azione per selezionare l'immagine
                    },
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_launcher_foreground), // Placeholder
                    contentDescription = "Profile Image",
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Nome
            OutlinedTextField(
                value = name,
                onValueChange = onNameChange,
                label = { Text("Nome") }
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Email (non modificabile)
            OutlinedTextField(
                value = email,
                onValueChange = {},
                label = { Text("Email") },
                enabled = false // Disabilitato
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Peso
            OutlinedTextField(
                value = weight,
                onValueChange = onWeightChange,
                label = { Text("Peso (kg)") }
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Compleanno
            OutlinedTextField(
                value = birthDate,
                onValueChange = onBirthDateChange,
                label = { Text("Compleanno (gg/mm/aaaa)") }
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Spazio per Milestones
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .background(Color.LightGray),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "Milestones")
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Spazio per Attività
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .background(Color.LightGray),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "Le tue attività")
            }
        }

        // Navigation Bar (Placeholder)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .background(darkGreen), // Verde più scuro
            contentAlignment = Alignment.Center
        ) {
            Text(text = "Navigation Bar", color = Color.White)
        }
    }
}

//test UI
@Preview(showBackground = true)
@Composable
fun UserProfileScreenPreview() {
    UserProfileContent(
        userId = "123",
        name = "Mario Rossi",
        onNameChange = {},
        email = "mario.rossi@example.com",
        weight = "75",
        onWeightChange = {},
        birthDate = "15/04/1985",
        onBirthDateChange = {}
    )
}
