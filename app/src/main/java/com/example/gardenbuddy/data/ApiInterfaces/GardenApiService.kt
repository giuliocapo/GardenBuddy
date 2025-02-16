package com.example.gardenbuddy.data.ApiInterfaces

import com.example.gardenbuddy.data.Dtos.responses.ApiResponse
import com.example.gardenbuddy.data.models.Garden
import retrofit2.Response
import retrofit2.http.*

interface GardenApiService {

    @GET("gardens/garden/{userId}")
    suspend fun getGardenById(@Path("userId") userId: String): Response<Garden>

    @GET("gardens/garden/{gardenId}")
    suspend fun getGardenByGardenId(@Path("gardenId") gardenId: Long): Response<Garden>

    @POST("gardens")
    suspend fun createGarden(@Body garden: Garden): Response<ApiResponse<Garden>>

    @PUT("gardens/{gardenId}")
    suspend fun updateGarden(@Path("gardenId") gardenId: Long, @Body garden: Garden): Response<ApiResponse<Garden>>

    @DELETE("gardens/{gardenId}")
    suspend fun deleteGarden(@Path("gardenId") gardenId: Long): Response<ApiResponse<Garden>>

}