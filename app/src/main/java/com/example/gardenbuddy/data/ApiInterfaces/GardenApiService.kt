package com.example.gardenbuddy.data.ApiInterfaces

import com.example.gardenbuddy.data.Dtos.responses.ApiResponse
import com.example.gardenbuddy.data.models.Garden
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface GardenApiService {

    @GET("gardens/garden/{gardenId}")
    suspend fun getGardenById(@Path("gardenId") gardenId: Long): Response<Garden>

    @POST("gardens")
    suspend fun createGarden(@Body garden: Garden): Response<ApiResponse<Garden>>

    @PUT("gardens/{gardenId}")
    suspend fun updateGarden(@Path("gardenId") gardenId: Long, @Body garden: Garden): Response<ApiResponse<Garden>> // TODO verify if not putting the user_id in the body req throws error

    @DELETE("gardens/{gardenId}")
    suspend fun deleteGarden(@Path("gardenId") gardenId: Long): Response<String>


}