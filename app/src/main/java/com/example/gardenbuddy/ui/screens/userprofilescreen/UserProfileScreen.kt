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

import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.gardenbuddy.R
import com.example.gardenbuddy.data.models.User
import com.example.gardenbuddy.ui.theme.GardenBuddyTheme
import com.example.gardenbuddy.utils.toFormattedString
import java.text.SimpleDateFormat
import java.util.*
import android.util.Log
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.graphics.Color

@Composable
fun UserProfileScreen(
    sharedUserViewModel: SharedUserViewModel,
    viewModel: UserProfileScreenViewModel = viewModel()
) {
    val userId = sharedUserViewModel.user.value?.userId ?: ""
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
            birthDate = user.birthdate?.toFormattedString().toString() // Formatta la data
        }
    }

    // Scaffold con una BottomAppBar di esempio
    Scaffold(
        content = { innerPadding ->
            UserProfileContent(
                userId = userId,
                name = name,
                onNameChange = { name = it },
                email = email,
                weight = weight,
                onWeightChange = { weight = it },
                birthDate = birthDate,
                onBirthDateChange = { birthDate = it },
                modifier = Modifier.padding(innerPadding)
            )
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
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
    viewModel: UserProfileScreenViewModel = viewModel(),
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Barra in alto con pulsante "Salva"
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .background(MaterialTheme.colorScheme.primary),
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
                    color = MaterialTheme.colorScheme.onPrimary
                )

                // Pulsante Salva
                TextButton(
                    onClick = {
                        try {
                            val parsedWeight = weight.toDoubleOrNull()
                            val parsedBirthDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(birthDate)
                            if (parsedWeight == null || parsedBirthDate == null) {
                                // Gestisci l'errore, es: messaggio di errore
                                return@TextButton
                            }
                            viewModel.updateUserProfile(
                                User(
                                    userId = userId,
                                    name = name,
                                    weight = parsedWeight,
                                    email = email,
                                    birthdate = parsedBirthDate
                                )
                            )
                        } catch (e: Exception) {
                            Log.e("UserProfileContent", "Error parsing input fields", e)
                        }
                    }
                ) {
                    Text(
                        text = "Salva",
                        color = MaterialTheme.colorScheme.onPrimary,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }

        // Contenuto del profilo
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .background(MaterialTheme.colorScheme.background),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Immagine del profilo
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surfaceVariant)
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
                label = { Text("Nome") },
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    // Colore del testo quando è in focus o no
                    focusedTextColor = MaterialTheme.colorScheme.onPrimary,
                    unfocusedTextColor = MaterialTheme.colorScheme.onPrimary,

                    // Bordo quando è in focus o no
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = Color.Gray,

                    // Label quando è in focus o no
                    focusedLabelColor = MaterialTheme.colorScheme.primary,
                    unfocusedLabelColor = Color.Gray,

                    )
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Email (non modificabile)
            OutlinedTextField(
                value = email,
                onValueChange = {},
                label = { Text("Email") },
                enabled = false,
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    // Colore del testo quando è in focus o no
                    focusedTextColor = MaterialTheme.colorScheme.onPrimary,
                    unfocusedTextColor = MaterialTheme.colorScheme.onPrimary,

                    // Bordo quando è in focus o no
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = Color.Gray,

                    // Label quando è in focus o no
                    focusedLabelColor = MaterialTheme.colorScheme.primary,
                    unfocusedLabelColor = Color.Gray,

                    )
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Peso
            OutlinedTextField(
                value = weight,
                onValueChange = onWeightChange,
                label = { Text("Peso (kg)") },
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    // Colore del testo quando è in focus o no
                    focusedTextColor = MaterialTheme.colorScheme.onPrimary,
                    unfocusedTextColor = MaterialTheme.colorScheme.onPrimary,

                    // Bordo quando è in focus o no
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = Color.Gray,

                    // Label quando è in focus o no
                    focusedLabelColor = MaterialTheme.colorScheme.primary,
                    unfocusedLabelColor = Color.Gray,

                    )
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Compleanno
            OutlinedTextField(
                value = birthDate,
                onValueChange = onBirthDateChange,
                label = { Text("Compleanno (gg/mm/aaaa)") },
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    // Colore del testo quando è in focus o no
                    focusedTextColor = MaterialTheme.colorScheme.onPrimary,
                    unfocusedTextColor = MaterialTheme.colorScheme.onPrimary,

                    // Bordo quando è in focus o no
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = Color.Gray,

                    // Label quando è in focus o no
                    focusedLabelColor = MaterialTheme.colorScheme.primary,
                    unfocusedLabelColor = Color.Gray,

                    )
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Spazio per Milestones
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .background(MaterialTheme.colorScheme.surfaceVariant),
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
                    .background(MaterialTheme.colorScheme.surfaceVariant),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "Le tue attività")
            }
        }

        // Navigation Bar (Placeholder) - se vuoi mantenere una barra separata, la definisci qui
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .background(MaterialTheme.colorScheme.primary),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Navigation Bar",
                color = MaterialTheme.colorScheme.onPrimary
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun UserProfileScreenPreview() {
    GardenBuddyTheme(darkTheme = true) {
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
}
