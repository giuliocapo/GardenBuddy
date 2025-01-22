package com.example.gardenbuddy.ui.screens.activityboardscreen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gardenbuddy.data.models.Activity
import com.example.gardenbuddy.data.repositories.ActivityBoardRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch


class ActivityBoardViewModel : ViewModel() {

    private val TAG = "ActivityBoardViewModel"

    // Stato delle attività (recuperate dal backend)
    private val _activities = MutableStateFlow<List<Activity>>(emptyList())
    val activities: StateFlow<List<Activity>> = _activities

    // Stato per la barra di ricerca
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> get() = _searchQuery

    // Stato per messaggi di errore
    private val _errorMessage = MutableStateFlow("")
    val errorMessage: StateFlow<String> = _errorMessage

    // Stato filtrato combinando attività e query
    val filteredActivities = combine(activities, searchQuery) { activityList, query ->
        activityList.filter { it.username.lowercase().contains(query.lowercase()) }
            .sortedByDescending { it.steps }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Lazily,
        initialValue = emptyList()
    )

    // Aggiorna la query di ricerca
    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    // Recupera le attività dal repository
    fun fetchActivities() {
        viewModelScope.launch {
            try {
                val fetchedActivities = ActivityBoardRepository.fetchAllActivities()
                _activities.value = fetchedActivities
                Log.d(TAG, "Fetched activities: $fetchedActivities")
            } catch (e: Exception) {
                // Gestione dell'errore
                e.printStackTrace()
                _errorMessage.value = "Errore nel recupero delle attività: ${e.message}"
                Log.e(TAG, "Error fetching activities", e)
            }
        }
    }

    // Aggiunge una nuova attività
    fun addActivity(userId: String, activity: Activity) {
        viewModelScope.launch {
            try {
                val newActivity = ActivityBoardRepository.addUserActivity(userId, activity)
                _activities.value = _activities.value + newActivity // Aggiorna localmente
            } catch (e: Exception) {
                // Gestione dell'errore
                e.printStackTrace()
            }
        }
    }

    // Elimina un'attività per ID
    fun deleteActivity(userId: String, activityId: String) {
        viewModelScope.launch {
            try {
                ActivityBoardRepository.deleteUserActivity(userId , activityId)
                _activities.value = _activities.value.filter { it.id != activityId }
            } catch (e: Exception) {
                // Gestione dell'errore
                e.printStackTrace()
            }
        }
    }
}
