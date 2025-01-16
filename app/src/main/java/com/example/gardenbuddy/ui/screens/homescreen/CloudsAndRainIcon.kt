package com.example.gardenbuddy.ui.screens.homescreen

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.dp

@Composable
fun CloudsAndRainIcon() {
    // Animazione per la pioggia
//    val infiniteTransition = rememberInfiniteTransition()
//    val rainOffset by infiniteTransition.animateFloat(
//        initialValue = 1f,
//        targetValue = 1.5f,
//        animationSpec = infiniteRepeatable(
//            animation = tween(durationMillis = 100000000, easing = LinearEasing),
//            repeatMode = RepeatMode.Restart
//        )
//    )

    Canvas(
        modifier = Modifier
            .size(64.dp)
            .padding(16.dp)
    ) {
        // Disegna le nuvole
        drawCloudRain(centerX = size.width / 3, centerY = size.height / 3)
        //drawCloudRain(centerX = 2 * size.width / 3, centerY = size.height / 5)

        // Disegna la pioggia
        drawRain(size.width/2, size.height/2)
    }
}

// Funzione per disegnare una nuvola
fun DrawScope.drawCloudRain(centerX: Float, centerY: Float) {
    val cloudRadius = 50f
    drawCircle(
        color = Color.Gray,
        radius = cloudRadius,
        center = Offset(centerX, centerY)
    )
    drawCircle(
        color = Color.Gray,
        radius = cloudRadius,
        center = Offset(centerX + 60, centerY)
    )
    drawCircle(
        color = Color.Gray,
        radius = cloudRadius,
        center = Offset(centerX + 30, centerY - 30)
    )
}

// Funzione per disegnare la pioggia
fun DrawScope.drawRain(rainOffsetX: Float, rainOffsetY: Float) {
    val rainLength = 30f
    val rainWidth = 6f
    val numDrops = 3 // Numero di gocce di pioggia
    val random = java.util.Random()

    val offsetsX = floatArrayOf(-30f, 10f, 50f)
    val offsetY = floatArrayOf(40f, 70f, 50f);

    for (i in 0 until numDrops) {
        val startX = rainOffsetX + offsetsX[i]
        val startY = rainOffsetY + offsetY[i]

        drawLine(
            color = Color.Blue,
            strokeWidth = rainWidth,
            start = Offset(startX, startY),
            end = Offset(startX, startY + rainLength)
        )
    }
}
