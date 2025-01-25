package com.example.gardenbuddy.data.Dtos.requests

import kotlinx.serialization.Serializable

@Serializable
data class CreategardenplantDTO (
    val photos : List<String>,
    val gardenId : Long,
    val plantId : Long
)