package com.example.gardenbuddy.ui.screens.activityboardscreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gardenbuddy.data.models.Activity
import com.example.gardenbuddy.data.repositories.ActivityBoardRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class ActivityBoardViewModel : ViewModel() {

    // Stato delle attività (recuperate dal backend)
    private val _activities = MutableStateFlow<List<Activity>>(emptyList())
    val activities: StateFlow<List<Activity>> = _activities

    // Stato per la barra di ricerca
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> get() = _searchQuery

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
                val fetchedActivities = ActivityBoardRepository.fetchActivities()
                _activities.value = fetchedActivities
            } catch (e: Exception) {
                // Gestione dell'errore
                e.printStackTrace()
            }
        }
    }

    // Aggiunge una nuova attività
    fun addActivity(activity: Activity) {
        viewModelScope.launch {
            try {
                val newActivity = ActivityBoardRepository.addActivity(activity)
                _activities.value = _activities.value + newActivity // Aggiorna localmente
            } catch (e: Exception) {
                // Gestione dell'errore
                e.printStackTrace()
            }
        }
    }

    // Elimina un'attività per ID
    fun deleteActivity(activityId: String) {
        viewModelScope.launch {
            try {
                ActivityBoardRepository.deleteActivity(activityId)
                _activities.value = _activities.value.filter { it.id != activityId }
            } catch (e: Exception) {
                // Gestione dell'errore
                e.printStackTrace()
            }
        }
    }
}
