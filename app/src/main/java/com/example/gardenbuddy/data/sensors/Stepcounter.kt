package com.example.gardenbuddy.data.sensors

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class StepCounter(context: Context) : SensorEventListener {

    private val sensorManager: SensorManager =
        context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private val stepSensor: Sensor? = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)

    // StateFlow per monitorare i passi
    private val _steps = MutableStateFlow(0)
    val steps: StateFlow<Int> = _steps

    // Conteggio iniziale per normalizzare i passi
    private var initialSteps: Int? = null

    init {
        if (stepSensor != null) {
            sensorManager.registerListener(this, stepSensor, SensorManager.SENSOR_DELAY_UI)
        } else {
            throw UnsupportedOperationException("Sensore Step Counter non disponibile")
        }
    }

    override fun onSensorChanged(event: SensorEvent?) {
        event?.let {
            if (initialSteps == null) {
                initialSteps = event.values[0].toInt() // Primo valore registrato
            }
            val currentSteps = event.values[0].toInt()
            _steps.value = currentSteps - (initialSteps ?: currentSteps)
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // Non richiesto per il conteggio dei passi
    }

    // Ferma l'ascolto del sensore per risparmiare batteria
    fun stop() {
        sensorManager.unregisterListener(this)
    }
}
