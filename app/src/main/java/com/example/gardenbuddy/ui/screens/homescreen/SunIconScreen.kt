package com.example.gardenbuddy.ui.screens.homescreen

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlin.math.cos
import kotlin.math.sin


@Composable
fun SunIcon(modifier: Modifier){
    Column {

        val infiniteTransition = rememberInfiniteTransition()
        val rotationAngle by infiniteTransition.animateFloat(
            initialValue = 0f,
            targetValue = 360f,
            animationSpec = infiniteRepeatable(
                animation = tween(durationMillis = 400000, easing = LinearEasing),
                repeatMode = RepeatMode.Restart
            )
        )

        Canvas(
            modifier.size(64.dp)
                .padding(end = 16.dp)
        ) {

            val centerX = size.width/2
            val centerY = size.height/2
            val radius = size.minDimension / 4

            val rayLength = radius * 1.5f
            for (i in 0 until 12) {
                val angle = Math.toRadians((i*30).toDouble()) + rotationAngle.toDouble()
                val startX = centerX + radius * cos(angle).toFloat()
                val startY = centerY + radius * sin(angle).toFloat()
                val endX = centerX + rayLength * cos(angle).toFloat()
                val endY = centerY + rayLength * sin(angle).toFloat()
                drawLine(Color.Yellow, Offset(startX, startY), Offset(endX, endY), strokeWidth = 5f)
            }


            drawCircle(Color.Yellow, radius = radius, center = Offset(centerX, centerY))
        }
    }
}
