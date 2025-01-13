package com.example.gardenbuddy.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gardenbuddy.data.models.User
import com.example.gardenbuddy.data.repositories.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class UserProfileScreenViewModel : ViewModel() {

    private val _userState = MutableStateFlow<User?>(null)
    val userState = _userState.asStateFlow()

    private val _errorMessage = MutableStateFlow("")
    val errorMessage = _errorMessage.asStateFlow()

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
        viewModelScope.launch {
            val result = AuthRepository.updateUserProfile(user)
            result.onFailure { error ->
                _errorMessage.value = error.message ?: "An error occurred while updating user"
            }
        }
    }
}
