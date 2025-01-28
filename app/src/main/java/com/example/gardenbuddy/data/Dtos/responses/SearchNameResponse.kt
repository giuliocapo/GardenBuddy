package com.example.gardenbuddy.data.Dtos.responses

import com.example.gardenbuddy.data.models.Plant

import kotlinx.serialization.Serializable
import kotlinx.serialization.Polymorphic
import kotlinx.serialization.SerialName
import com.google.gson.*
import com.google.gson.annotations.JsonAdapter
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

@Serializable
data class SearchNameSuccessResponse(
    val results: List<Plant>
)

@Serializable
data class SearchNameErrorResponse(
    val error: String,
)


@Serializable
@JsonAdapter(ResponseDtoAdapter::class) // Gson uses this adapter for deserialization
sealed class ResponseDto {
    @Serializable
    @SerialName("success")
    data class Success(val results: SearchNameSuccessResponse) : ResponseDto()

    @Serializable
    @SerialName("error")
    data class Error(val errorResponse: SearchNameErrorResponse) : ResponseDto()
}
class ResponseDtoAdapter : JsonDeserializer<ResponseDto> {
    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): ResponseDto {
        // Print out the JSON for debugging
        println(json.toString())

        return when {
            json.isJsonArray -> {
                // If the response is a JSON array, deserialize the array of plants
                val plantListType = object : TypeToken<List<Plant>>() {}.type
                val plantList: List<Plant> = context.deserialize(json, plantListType)

                // Wrap the plant list into SearchNameSuccessResponse and return Success
                val successResponse = SearchNameSuccessResponse(plantList)
                ResponseDto.Success(successResponse)
            }
            json.asJsonObject.has("error") -> {
                // If the response contains an error, deserialize it
                val errorResponse = context.deserialize<SearchNameErrorResponse>(json.asJsonObject, SearchNameErrorResponse::class.java)
                ResponseDto.Error(errorResponse)
            }
            else -> {
                // If the response doesn't match either case, throw an exception
                throw JsonParseException("Unexpected response format")
            }
        }
    }
}
