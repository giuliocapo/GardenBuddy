package com.example.gardenbuddy.ui.screens.homescreen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gardenbuddy.data.models.WeatherInfo
import com.example.gardenbuddy.data.repositories.WeatherRepository
import com.example.gardenbuddy.utils.UserPreferences
import com.example.gardenbuddy.utils.WeatherIconCode
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val userPreferences: UserPreferences
): ViewModel () {

    init {
        viewModelScope.launch {
            val userId = userPreferences.getUserIdOnce()
            Log.d("WeatherViewModel", "User ID: $userId")
            userPreferences.getUserIdOnce()?.let { getWeatherInfo(it) }
        }
    }

    private val _isGardenCreated = MutableStateFlow(false)
    val isGardenCreated: StateFlow<Boolean> = _isGardenCreated

    private val _isLoading = MutableStateFlow(false)
    val isLoading : StateFlow<Boolean> = _isLoading

    private val _weatherInfo = MutableStateFlow<WeatherInfo?>(null)
    val weatherInfo : StateFlow<WeatherInfo?> = _weatherInfo

    private val _weatherIconCode = MutableStateFlow<WeatherIconCode?>(null)
    val weatherIconCode : StateFlow<WeatherIconCode?> = _weatherIconCode

     private fun getWeatherInfo(userId : String) {
        viewModelScope.launch{
            _weatherInfo.value = WeatherRepository.fetchWeatherInfo(userId)
            Log.d("Weather code VM", _weatherInfo.value!!.weather_id.toString())
            _weatherIconCode.value = mapWeatherCode(_weatherInfo.value!!.weather_id)
            Log.d("WeatherViewModel", "Weather Info: ${_weatherInfo.value}")
            Log.d("WeatherViewModel", "Weather Icon Code: ${_weatherIconCode.value}")

        }
    }

    private fun mapWeatherCode(code: Int): WeatherIconCode{
        return when (code) {
            200, 201, 202, 210, 211, 212, 221, 230, 231, 232, 300, 301, 302, 310, 311, 312, 313, 314, 321, 500, 501, 502, 503, 504, 511, 520, 521, 522, 531 -> WeatherIconCode.RAINY
            800 -> WeatherIconCode.SUNNY
            801, 802, 803, 804 -> WeatherIconCode.CLOUDY
            else -> WeatherIconCode.SUNNY
        }
    }
}