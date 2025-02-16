package com.example.gardenbuddy.data.models
import kotlinx.serialization.Serializable

@Serializable
data class Plant(
    val plantId : Long,
    val scientificName : String,
    val species : String = "not specified",
    val family : String = "not specified",
    val parent : String = "not specified",
    val kingdom : String= "not specified"
)

