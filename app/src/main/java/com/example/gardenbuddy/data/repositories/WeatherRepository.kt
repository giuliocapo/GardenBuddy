package com.example.gardenbuddy.data.repositories

import com.example.gardenbuddy.data.models.Activity
import com.example.gardenbuddy.data.models.WeatherInfo
import com.example.gardenbuddy.data.repositories.ActivityBoardRepository.ActivityApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*


object WeatherRepository {

    interface WeatherApi {

        // GET /activities/{userId} -> Ritorna le attività di uno specifico utente
        @GET("weather/{userId}")
        suspend fun getWeatherByUserId(@Path("userId") userId: String): WeatherInfo

    }

    // 1. Crea l'istanza di Retrofit, specificando la baseUrl del tuo server
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://lawrenzo.pythonanywhere.com/") // Sostituisci con il TUO dominio
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    // 2. Crea l'istanza dell'interfaccia che definisce le rotte
    private val api = retrofit.create(WeatherApi::class.java)

    // 3. Qui sotto definisci le funzioni "Repository" che userai nel tuo codice Kotlin/Android:

    suspend fun fetchWeatherInfo(userId: String): WeatherInfo {
        return withContext(Dispatchers.IO) {
            WeatherRepository.api.getWeatherByUserId(userId)
        }
    }
}