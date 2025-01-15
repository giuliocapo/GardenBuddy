package com.example.gardenbuddy.ui.screens.homescreen

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun WeatherCard(
    location: String,
    temperature: String,
    summary: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .padding(16.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = androidx.compose.material3.CardDefaults.cardColors(
            containerColor = Color.Cyan
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            SunIcon(modifier)

            // Sezione Informazioni Meteo
            Column(
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = location,
                    fontSize = 20.sp,
                    color = androidx.compose.ui.graphics.Color.Black,
                    textAlign = TextAlign.Start
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = temperature,
                    fontSize = 24.sp,
                    color = androidx.compose.ui.graphics.Color.Blue,
                    textAlign = TextAlign.Start
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = summary,
                    fontSize = 16.sp,
                    color = androidx.compose.ui.graphics.Color.Gray,
                    textAlign = TextAlign.Start
                )
            }
        }
    }
}


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

@Preview
@Composable
fun WeatherCardPreview() {
    WeatherCard(
        location = "Rome, Italy",
        temperature = "18°C",
        summary = "Sunny",
        modifier = Modifier
    )
}
