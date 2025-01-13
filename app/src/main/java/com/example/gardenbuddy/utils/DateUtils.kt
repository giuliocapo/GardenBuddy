package com.example.gardenbuddy.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun String.toDateOrNull(): Date? {
    return try {
        val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()) // Specifica il formato desiderato
        formatter.parse(this) // Prova a fare il parsing della stringa
    } catch (e: Exception) {
        null // Ritorna null se la stringa non è valida
    }
}

fun Date.toFormattedString(): String {
    val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()) // Converte Date in stringa leggibile
    return formatter.format(this)
}
