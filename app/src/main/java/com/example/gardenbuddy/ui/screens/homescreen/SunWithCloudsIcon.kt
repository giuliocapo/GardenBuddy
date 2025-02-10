package com.example.gardenbuddy.ui.screens.homescreen

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.unit.dp


@Composable
fun SunWithCloudsIcon(modifier: Modifier) {
    // Animazioni per il movimento delle nuvole
//    val infiniteTransition = rememberInfiniteTransition()
//    val cloudOffset1 by infiniteTransition.animateFloat(
//        initialValue = -200f,
//        targetValue = 600f,
//        animationSpec = infiniteRepeatable(
//            animation = tween(durationMillis = 10000, easing = LinearEasing),
//            repeatMode = RepeatMode.Restart
//        )
//    )
//    val cloudOffset2 by infiniteTransition.animateFloat(
//        initialValue = -400f,
//        targetValue = 400f,
//        animationSpec = infiniteRepeatable(
//            animation = tween(durationMillis = 12000, easing = LinearEasing),
//            repeatMode = RepeatMode.Restart
//        )
//    )
    Column(){
        Canvas(
            modifier.size(64.dp)
                .padding(end = 16.dp)
        ) {
            // Disegna il sole
            drawSun(centerX = size.width / 2, centerY = size.height / 3, radius = 50f)

            // Disegna le nuvole
            //drawCloud(size.width / 2 + 50f, centerY = size.height / 3 + 40f)
            drawCloud(size.width / 2 + 20f, centerY = size.height / 3 + 50f)
        }
    }
}

// Funzione per disegnare il sole
fun DrawScope.drawSun(centerX: Float, centerY: Float, radius: Float) {
    drawCircle(
        color = Color(0xFFFBFF00),
        radius = radius,
        center = Offset(centerX, centerY)
    )
}

// Funzione per disegnare una nuvola
fun DrawScope.drawCloud(offsetX: Float, centerY: Float) {
    val cloudRadius = 40f
    drawCircle(
        color = Color(0xFFDED8D8),
        radius = cloudRadius,
        center = Offset(offsetX, centerY)
    )
    drawCircle(
        color = Color(0xFFDED8D8),
        radius = cloudRadius,
        center = Offset(offsetX + 60, centerY)
    )
    drawCircle(
        color = Color(0xFFDED8D8),
        radius = cloudRadius,
        center = Offset(offsetX + 30, centerY - 30)
    )
}
