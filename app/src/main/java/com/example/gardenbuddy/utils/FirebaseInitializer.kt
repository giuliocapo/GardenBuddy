package com.example.gardenbuddy.utils

import android.content.Context
import com.google.firebase.FirebaseApp

object FirebaseInitializer {
    fun init(context: Context) {
        FirebaseApp.initializeApp(context)
    }
}
