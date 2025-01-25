package com.example.gardenbuddy.data.repositories

import com.example.gardenbuddy.data.models.Activity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

// Questo oggetto funge da "Repository" per le chiamate relative alle Attività,
// utilizzando Retrofit per chiamare le API del tuo backend Flask.
object ActivityBoardRepository {

    // 1. Crea l'istanza di Retrofit, specificando la baseUrl del tuo server
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://lawrenzo.pythonanywhere.com/") // Sostituisci con il TUO dominio
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    // 2. Crea l'istanza dell'interfaccia che definisce le rotte
    private val api = retrofit.create(ActivityApi::class.java)

    // 3. Qui sotto definisci le funzioni "Repository" che userai nel tuo codice Kotlin/Android:

    /**
     * Recupera tutte le attività presenti nel DB (rotta GET "/activities").
     */
    suspend fun fetchAllActivities(): List<Activity> {
        return withContext(Dispatchers.IO) {
            api.getAllActivities()
        }
    }

    /**
     * Recupera tutte le attività di uno specifico utente (rotta GET "/activities/{userId}").
     */
    suspend fun fetchUserActivities(userId: String): List<Activity> {
        return withContext(Dispatchers.IO) {
            api.getUserActivities(userId)
        }
    }

    /**
     * Aggiunge (POST) una nuova attività per uno specifico utente (rotta POST "/activities/{userId}").
     * Ritorna l'oggetto Activity creato (o modificato dal server).
     */
    suspend fun addUserActivity(userId: String, activity: Activity): Activity {
        return withContext(Dispatchers.IO) {
            api.postActivity(userId, activity)
        }
    }

    /**
     * Elimina (DELETE) una specifica attività di un utente (rotta DELETE "/activities/{userId}/{activityId}").
     */
    suspend fun deleteUserActivity(userId: String, activityId: String) {
        withContext(Dispatchers.IO) {
            api.deleteActivity(userId, activityId)
        }
    }

    // 4. Definizione dell'interfaccia Retrofit che descrive gli endpoint del backend Flask.
    interface ActivityApi {

        // GET /activities -> Ritorna tutte le attività
        @GET("activities")
        suspend fun getAllActivities(): List<Activity>

        // GET /activities/{userId} -> Ritorna le attività di uno specifico utente
        @GET("activities/{userId}")
        suspend fun getUserActivities(@Path("userId") userId: String): List<Activity>

        // POST /activities/{userId} -> Crea una nuova attività per uno specifico utente
        @POST("activities/{userId}")
        suspend fun postActivity(
            @Path("userId") userId: String,
            @Body activity: Activity
        ): Activity

        // DELETE /activities/{userId}/{activityId} -> Elimina una specifica attività di un utente
        @DELETE("activities/{userId}/{activityId}")
        suspend fun deleteActivity(
            @Path("userId") userId: String,
            @Path("activityId") activityId: String
        )
    }
}
