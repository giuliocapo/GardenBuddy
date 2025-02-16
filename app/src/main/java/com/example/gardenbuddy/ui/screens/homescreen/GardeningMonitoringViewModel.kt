package com.example.gardenbuddy.ui.screens.homescreen

import android.app.Application
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.gardenbuddy.utils.GardeningMonitoringService
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import android.util.Log
import android.widget.Toast
import com.example.gardenbuddy.data.models.Activity
import com.example.gardenbuddy.data.models.User
import com.example.gardenbuddy.data.repositories.ActivityBoardRepository
import java.sql.Date
import java.time.LocalDate
import kotlin.math.floor


class GardeningMonitoringViewModel(application: Application, user: User) : AndroidViewModel(application) {

    private val loggedUser = user
    private val _steps = MutableLiveData<Int>(0)
    val steps: LiveData<Int> = _steps

    private val _kcalBurned = MutableLiveData<Double>(0.0)
    val kcalBurned: LiveData<Double> = _kcalBurned

    private val _sessionStartTime = MutableLiveData<Long?>(null)
    val sessionStartTime: LiveData<Long?> = _sessionStartTime

    private val _sessionSeconds = MutableLiveData<Int>(0)
    val sessionSeconds: LiveData<Int> = _sessionSeconds

    private val _sessionMinutes = MutableLiveData<Int>(0)
    val sessionMinutes: LiveData<Int> = _sessionMinutes

    private val _isMonitoringActive = MutableLiveData<Boolean>(false)
    val isMonitoringActive: LiveData<Boolean> = _isMonitoringActive

    private var gardeningService: GardeningMonitoringService? = null
    private var isBound = false

    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, binder: IBinder?) {
            val localBinder = binder as GardeningMonitoringService.LocalBinder
            gardeningService = localBinder.getService()
            isBound = true
            _sessionStartTime.postValue(gardeningService?.sessionStartTime)
            Log.d("GardeningViewModel", "Service connesso: sessionStartTime=${gardeningService?.sessionStartTime}")
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            isBound = false
            gardeningService = null
            Log.d("GardeningViewModel", "Service disconnesso")
        }
    }

    /**
     * Avvia il monitoraggio avviando e collegando il service.
     */
    fun startMonitoring() {
        if (_isMonitoringActive.value == true) return
        Log.d("GardeningViewModel", "Monitoraggio avviato")
        _isMonitoringActive.value = true
        val intent = Intent(getApplication(), GardeningMonitoringService::class.java)
        getApplication<Application>().startForegroundService(intent)
        getApplication<Application>().bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)

        // Polling periodico per aggiornare i dati dal service
        viewModelScope.launch {
            Log.d("GardeningViewModel", "Polling coroutine avviata")
            while (_isMonitoringActive.value == true) {
                delay(1000)
                Log.d("GardeningViewModel", "isBound=$isBound, gardeningService=$gardeningService")
                if (isBound && gardeningService != null) {
                    val secondsElapsed = (System.currentTimeMillis() - _sessionStartTime.value!!) / 1000
                    _sessionMinutes.postValue(floor((secondsElapsed / 60.0)).toInt())
                    _sessionSeconds.postValue((secondsElapsed % 60).toInt())
                    _steps.postValue(gardeningService!!.steps)
                    _kcalBurned.postValue(gardeningService!!.kcalBurned)
                    Log.d("GardeningViewModel", "Polling: secondsElapsed=$secondsElapsed")
                    Log.d("GardeningViewModel", "Polling: steps=${gardeningService!!.steps}, kcal=${gardeningService!!.kcalBurned}")
                }
            }
        }
    }

    /**
     * Ferma il monitoraggio scollegando e fermando il service.
     */
     fun stopMonitoring() {
        if (_isMonitoringActive.value != true) return

        val intent = Intent(getApplication(), GardeningMonitoringService::class.java)
        val newActivity = Activity(
            id = null,
            userId = loggedUser.userId,
            username = loggedUser.name,
            steps = _steps.value!!,
            calories = _kcalBurned.value!!.toInt(),
            minutes = _sessionMinutes.value!!,
            timestamp = System.currentTimeMillis() - _sessionSeconds.value!!)
        Log.d("GardeningViewModel", "Salvo sessione 1: $newActivity")
        saveSession(loggedUser.userId, newActivity)
        Toast.makeText(getApplication(), "Session saved", Toast.LENGTH_SHORT).show()
        getApplication<Application>().unbindService(serviceConnection)
                isBound = false
                _isMonitoringActive . value = false
        if (getApplication<Application>().stopService(intent)) {

        }
        Log . d ("GardeningViewModel", "Monitoraggio fermato"
        )
    }

    override fun onCleared() {
        super.onCleared()
        if (isBound) {
            getApplication<Application>().unbindService(serviceConnection)
            isBound = false
        }
    }

    private fun saveSession(userId: String, activity: Activity) {
        viewModelScope.launch {
            try {
                val newActivity = ActivityBoardRepository.addUserActivity(userId, activity)
                Log.d("GardeningViewModel", "Salvo sessione 2: $activity")
                Log.d("GardeningViewModel", "Sessione salvata: $activity")

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}


