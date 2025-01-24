package com.example.gardenbuddy.data.ApiInterfaces

import com.example.gardenbuddy.data.Dtos.requests.CreategardenplantDTO
import com.example.gardenbuddy.data.Dtos.responses.ApiResponse
import com.example.gardenbuddy.data.models.Garden
import com.example.gardenbuddy.data.models.GardenPlant
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface GardenPlantApiService {
    @GET("gardenPlants/garden/{gardenId}")
    suspend fun getGardenPlantsByGardenId(@Path("gardenId") gardenId: Long): Response<List<GardenPlant>>

    @GET("gardenPlants/garden/{gardenId}/{plantId}")
    suspend fun getGardenPlantByGardenIdAndPlantId(@Path("gardenId") gardenId: Long, @Path("plantId") plantId: Long): Response<GardenPlant>

    @GET("gardenPlants/garden/{plantId}")
    suspend fun getGardenPlantsByPlantId(@Path("plantId") plantId: Long): Response<List<GardenPlant>>

    @POST("gardenPlants")
    suspend fun createGardenPlant(@Body gardenPlant: CreategardenplantDTO): Response<ApiResponse<GardenPlant>>

    @PUT("gardenPlants/{gardenId}/{plantId}")
    suspend fun updateGardenPlant(@Path("gardenId") gardenId: Long, @Path("plantId") plantId: Long, @Body photos: List<String>): Response<ApiResponse<GardenPlant>>

    @DELETE("gardenPlants/{gardenId}/{plantId}")
    suspend fun deleteGardenPlant(@Path("gardenId") gardenId: Long, @Path("plantId") plantId: Long): Response<String>
}