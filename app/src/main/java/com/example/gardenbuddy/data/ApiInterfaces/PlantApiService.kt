package com.example.gardenbuddy.data.ApiInterfaces

import com.example.gardenbuddy.data.Dtos.requests.SearchPhotoRequest
import com.example.gardenbuddy.data.Dtos.responses.ApiResponse

import com.example.gardenbuddy.data.models.Plant
import retrofit2.http.*
import retrofit2.Response

interface PlantApiService {
    @GET("plants/searchByName")
    suspend fun searchPlantByName(@Query("name") name: String, @Query("limit") limit: Int = 1) : Response<List<Plant>>

    @POST("plants/searchByPhoto")
    suspend fun searchPlantByPhoto(@Body request : SearchPhotoRequest): Response<List<Plant>>

    @GET("plants/{plantId}")
    suspend fun getPlantById(@Path("plantId") plantId: Long) : Response<Plant>

    @POST("plants")
    suspend fun createPlant(@Body plant: Plant): Response<ApiResponse<Plant>>

    @PUT("plants/{plantId}")
    suspend fun updatePlant(@Path("plantId") plantId: Long, @Body plant: Plant): Response<ApiResponse<Plant>> // TODO verify if not putting the user_id in the body req throws error

    @DELETE("plants/{plantId}")
    suspend fun deletePlant(@Path("plantId") plantId: Long): Response<ApiResponse<Plant>>
}