package com.example.gardenbuddy.utils

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MyApplication : Application() {
    // Configurazioni globali
// val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_prefs")

//    override fun onCreate() {
//        super.onCreate()
//        UserPreferences.initialize(this)
//    }
}
