package com.example.gardenbuddy.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gardenbuddy.data.models.User
import com.example.gardenbuddy.data.repositories.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Date

class SignUpScreenViewModel : ViewModel() {

    private val _errorMessage = MutableStateFlow("")
    val errorMessage = _errorMessage.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _signUpSuccess = MutableStateFlow<User?>(null)
    val signUpSuccess = _signUpSuccess.asStateFlow()

    fun signUp(email: String, password: String, name: String, weight: Double, birthdate: Date) {
        viewModelScope.launch {
            _isLoading.value = true
            val result = AuthRepository.signUp(email, password, name, weight, birthdate)
            _isLoading.value = false

            result.onSuccess { user ->
                _signUpSuccess.value = user
            }.onFailure { error ->
                _errorMessage.value = error.localizedMessage ?: "Unknown error"
            }
        }
    }
}
