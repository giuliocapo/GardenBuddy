package com.example.gardenbuddy.data.models
import kotlinx.serialization.Serializable

@Serializable
data class Plant(
    val plantId : Long,
    val scientificName : String,
    val species : String,
    val family : String,
    val parent : String,
    val kingdom : String
)

