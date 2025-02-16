package com.example.gardenbuddy.ui.screens

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gardenbuddy.data.models.Activity
import com.example.gardenbuddy.data.models.User
import com.example.gardenbuddy.data.repositories.ActivityBoardRepository
import com.example.gardenbuddy.data.repositories.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class UserProfileScreenViewModel : ViewModel() {


    private val _userState = MutableStateFlow<User?>(null)
    val userState = _userState.asStateFlow()

    private val _errorMessage = MutableStateFlow("")
    val errorMessage = _errorMessage.asStateFlow()

    private val _isEditable = MutableStateFlow(false)
    val isEditable = _isEditable.asStateFlow()

    private val _userActivities = MutableStateFlow<List<Activity>>(emptyList())
    val userActivities = _userActivities.asStateFlow()

    fun loadUserProfile(userId: String) {
        viewModelScope.launch {
            val result = AuthRepository.getUserProfile(userId)
            result.onSuccess { user ->
                _userState.value = user
            }.onFailure { error ->
                _errorMessage.value = error.message ?: "An error occurred"
            }
        }
    }

    fun updateUserProfile(user: User) {
        Log.d("UserProfileViewModel", "updateUserProfile called with user: $user")
        viewModelScope.launch {
            val result = AuthRepository.updateUserProfile(user)
            result.onSuccess {
                Log.d("UserProfileViewModel", "User profile updated successfully")
            }.onFailure { error ->
                _errorMessage.value = error.message ?: "An error occurred while updating user"
                Log.e("UserProfileViewModel", "Error updating user profile", error)
            }
        }
    }

    fun loadUserActivities(userId: String) {
        viewModelScope.launch {
            try {
                val result = ActivityBoardRepository.fetchUserActivities(userId)
                _userActivities.value = result
                Log.d("UserProfileViewModel", "User activities loaded successfully")
            }catch (e: Exception) {
                _errorMessage.value = e.message ?: "An error occurred while loading user activities"
                Log.e("UserProfileViewModel", "Error loading user activities", e)
            }
        }

    }

    fun toggleEditable() {
        _isEditable.value = !_isEditable.value
    }
}
