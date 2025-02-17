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
import android.widget.Toast
import androidx.activity.result.launch
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Cancel
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.Stop
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavHostController
import com.example.gardenbuddy.data.models.Activity
import com.example.gardenbuddy.ui.screens.activityboardscreen.ActivityBoardViewModel
import com.example.gardenbuddy.ui.screens.activityboardscreen.ActivityCard
import com.example.gardenbuddy.ui.screens.homescreen.BottomNavigationBar

@Composable
fun UserProfileScreen(
    navController: NavHostController,
    sharedUserViewModel: SharedUserViewModel,
    displayedUserId: String,
    viewModel: UserProfileScreenViewModel = viewModel()
) {
    val selectedTab = remember { mutableStateOf("userProfile") }
    val isEditable by viewModel.isEditable.collectAsState()
    val userActivities by viewModel.userActivities.collectAsState()

    // userId dell'utente loggato
    val loggedUserId = sharedUserViewModel.user.value?.userId ?: "" // ?: Elvis operator per settare a "" qualora fosse null userId

    // se coincide, è il MIO profilo
    val isOwner = (loggedUserId == displayedUserId)

    // debbugging
    Log.d("UserProfileScreen", "userId: $loggedUserId, displayedUserId: $displayedUserId, isOwner: $isOwner")

    // Carica i dati dell'utente al primo avvio di questa schermata
    LaunchedEffect(loggedUserId) {
        viewModel.loadUserProfile(displayedUserId)
        viewModel.loadUserActivities(displayedUserId)
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
                isOwner = isOwner,
                userId = loggedUserId,
                name = name,
                onNameChange = { name = it },
                email = email,
                weight = weight,
                onWeightChange = { weight = it },
                birthDate = birthDate,
                onBirthDateChange = { birthDate = it },
                modifier = Modifier.padding(innerPadding),
                userActivities = userActivities,
                navController = navController,
                isEditable = isEditable
            )
        },

        bottomBar = {
            BottomNavigationBar(navController = navController,sharedUserViewModel = sharedUserViewModel, selectedTab = selectedTab.value) {
                selectedTab.value = it
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserProfileContent(
    isOwner: Boolean,
    userId: String,
    name: String,
    onNameChange: (String) -> Unit,
    email: String,
    weight: String,
    onWeightChange: (String) -> Unit,
    birthDate: String,
    onBirthDateChange: (String) -> Unit,
    viewModel: UserProfileScreenViewModel = viewModel(),
    userActivities: List<Activity>,
    isEditable: Boolean,
    navController: NavHostController,
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
                .fillMaxWidth(),
            contentAlignment = Alignment.CenterStart
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                // Pulsante Salva
                if (isOwner) {

                    // Recupera il Context per poter mostrare il Toast
                    val context = LocalContext.current
                    if (!isEditable) {
                        Row(Modifier.fillMaxWidth()) {
                            Text(
                                text = "Your Profile",
                                style = MaterialTheme.typography.headlineLarge,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 16.dp)
                            )
                            Spacer(Modifier.weight(1f))
                            IconButton(onClick = { viewModel.toggleEditable() }) {
                                Icon(
                                    Icons.Rounded.Edit,
                                    contentDescription = "Make profile editable"
                                )
                            }
                        }
                    } else {
                        Row(Modifier.fillMaxWidth()) {
                            Text(
                                text = "Your Profile",
                                style = MaterialTheme.typography.headlineLarge,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 16.dp)
                            )
                            Spacer(Modifier.weight(1f))
                            IconButton(onClick = { viewModel.toggleEditable() }) {
                                Icon(
                                    Icons.Rounded.Cancel,
                                    contentDescription = "Make profile not editable"
                                )
                            }
                            IconButton(onClick = {
                                try {
                                    val parsedWeight = weight.toDoubleOrNull()
                                    val parsedBirthDate =
                                        SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(
                                            birthDate
                                        )
                                    if (parsedWeight == null || parsedBirthDate == null) {
                                        Toast.makeText(context, "Invalid input", Toast.LENGTH_SHORT)
                                            .show()
                                    }
                                    parsedWeight?.let {
                                        User(
                                            userId = userId,
                                            name = name,
                                            weight = it,
                                            email = email,
                                            birthdate = parsedBirthDate
                                        )
                                    }?.let {
                                        viewModel.updateUserProfile(
                                            it
                                        )
                                    }
                                    viewModel.toggleEditable()
                                    // Mostra il Toast dopo il salvataggio
                                    Toast.makeText(
                                        context,
                                        "Profilo aggiornato con successo!",
                                        Toast.LENGTH_SHORT
                                    ).show()

                                } catch (e: Exception) {
                                    Log.e("UserProfileContent", "Error parsing input fields", e)
                                }
                            }) {
                                Icon(
                                    Icons.Rounded.Check,
                                    contentDescription = "Make profile not editable"
                                )
                            }
                        }
                    }
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
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .clickable {
                        // Aggiungi azione per selezionare l'immagine
                    },
                contentAlignment = Alignment.Center
            ) {
                // Mostra l'immagine corretta in base a isOwner
                if (isOwner) {
                    Image(
                        painter = painterResource(id = R.drawable.gardenbuddyicon), // Immagine per il proprietario
                        contentDescription = "Profile Image",
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Image(
                        painter = painterResource(id = R.drawable.ic_launcher_foreground), // Immagine per gli altri utenti
                        contentDescription = "Profile Image",
                        contentScale = ContentScale.Crop
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Nome
            OutlinedTextField(
                value = name,
                onValueChange = onNameChange,
                label = { Text("Nome") },
                modifier = Modifier.fillMaxWidth(),
                enabled = isOwner && isEditable,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF4CAF50),    // Green when focused
                    unfocusedBorderColor = Color.Black,
                    errorBorderColor = Color.Red,
                    unfocusedTextColor = Color.Black,
                    focusedTextColor = Color.Black,
                    disabledBorderColor = Color.DarkGray,
                    disabledTextColor = Color.DarkGray
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
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF4CAF50),
                    unfocusedBorderColor = Color.Gray,
                    errorBorderColor = Color.Red,
                    unfocusedTextColor = Color.Gray,
                    focusedTextColor = Color.Black,
                    disabledBorderColor = Color.DarkGray,
                    disabledTextColor = Color.DarkGray

                )
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Peso
            OutlinedTextField(
                value = weight,
                onValueChange = onWeightChange,
                label = { Text("Peso (kg)") },
                modifier = Modifier.fillMaxWidth(),
                enabled = isOwner && isEditable,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF4CAF50),    // Green when focused
                    unfocusedBorderColor = Color.Black,
                    errorBorderColor = Color.Red,
                    unfocusedTextColor = Color.Black,
                    focusedTextColor = Color.Black,
                    disabledBorderColor = Color.DarkGray,
                    disabledTextColor = Color.DarkGray
                )
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Compleanno
            OutlinedTextField(
                value = birthDate,
                onValueChange = onBirthDateChange,
                label = { Text("Compleanno (gg/mm/aaaa)") },
                modifier = Modifier.fillMaxWidth(),
                enabled = isOwner && isEditable,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF4CAF50),    // Green when focused
                    unfocusedBorderColor = Color.Black,
                    errorBorderColor = Color.Red,
                    unfocusedTextColor = Color.Black,
                    focusedTextColor = Color.Black,
                    disabledBorderColor = Color.DarkGray,
                    disabledTextColor = Color.DarkGray
                )
            )

            Row(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
                Text(
                    text = "Le tue attività",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }

            if (userActivities.isNotEmpty()) {
                LazyColumn(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    items(userActivities.size) { index ->
                        val activity = userActivities[index]
                        ActivityCard(activity = activity, navController)
                    }
                }

            }else{
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
                    Text(text = "You have no activities")
                }
            }
        }
    }
}

//@Preview(showBackground = true)
//@Composable
//fun UserProfileScreenPreview() {
//    GardenBuddyTheme(darkTheme = true) {
//        UserProfileContent(
//            isOwner = true,
//            userId = "123",
//            name = "Mario Rossi",
//            onNameChange = {},
//            email = "mario.rossi@example.com",
//            weight = "75",
//            onWeightChange = {},
//            birthDate = "15/04/1985",
//            onBirthDateChange = {},
//            userActivities = List<Activity>(
//                size = TODO(),
//                init = TODO()
//            ),
//            isEditable = true
//        )
//    }
//}
