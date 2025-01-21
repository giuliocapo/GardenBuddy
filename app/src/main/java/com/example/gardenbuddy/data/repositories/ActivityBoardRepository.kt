package com.example.gardenbuddy.data.repositories

import com.example.gardenbuddy.data.models.Activity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

object ActivityBoardRepository {

    // Retrofit instance
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://your-pythonanywhere-url.com/") // Sostituisci con l'URL del tuo backend
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val api = retrofit.create(ActivityApi::class.java)

    // Recupera tutte le attività
    suspend fun fetchActivities(): List<Activity> {
        return withContext(Dispatchers.IO) {
            api.getActivities()
        }
    }

    // Aggiunge una nuova attività
    suspend fun addActivity(activity: Activity): Activity {
        return withContext(Dispatchers.IO) {
            api.postActivity(activity)
        }
    }

    // Elimina un'attività per ID
    suspend fun deleteActivity(activityId: String) {
        withContext(Dispatchers.IO) {
            api.deleteActivity(activityId)
        }
    }

    // Interfaccia API per Retrofit
    interface ActivityApi {
        @GET("activities") // Endpoint per ottenere tutte le attività
        suspend fun getActivities(): List<Activity>

        @POST("activities") // Endpoint per creare una nuova attività
        suspend fun postActivity(@Body activity: Activity): Activity

        @DELETE("activities/{id}") // Endpoint per eliminare un'attività
        suspend fun deleteActivity(@Path("id") id: String)
    }
}
