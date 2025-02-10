package com.example.gardenbuddy.ui.screens.loginscreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gardenbuddy.data.repositories.AuthRepository
import com.example.gardenbuddy.data.models.User
import com.example.gardenbuddy.utils.UserPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LogInScreenViewModel @Inject constructor(
    private val userPreferences: UserPreferences
) : ViewModel() {


    private val _errorMessage = MutableStateFlow("")
    val errorMessage = _errorMessage.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _logInSuccess = MutableStateFlow<User?>(null)
    val logInSuccess = _logInSuccess.asStateFlow()

    fun logIn(email: String, password: String) {
        viewModelScope.launch {
            _isLoading.value = true
            val result = AuthRepository.logIn(email, password)
            _isLoading.value = false

            result.onSuccess { user ->
                _logInSuccess.value = user
                userPreferences.saveUser(user.userId, user.name)
            }.onFailure { error ->
                _errorMessage.value = error.localizedMessage ?: "Unknown error"
            }
        }
    }
}
