package com.example.gardenbuddy.ui.screens.homescreen


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


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
            containerColor = Color(0xFF5fd6e8)
        )
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = modifier.weight(1f)){
//                AnimatedSunWithClouds(modifier)
              SunIcon(modifier)
                //CloudsAndRainIcon()
            }

            // Sezione Informazioni Meteo
            Column(
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = location,
                    fontSize = 20.sp,
                    color = Color.Black,
                    textAlign = TextAlign.Start
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = temperature,
                    fontSize = 24.sp,
                    color = Color.Blue,
                    textAlign = TextAlign.Start
                )

                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = summary,
                    fontSize = 16.sp,
                    color = Color.Gray,
                    textAlign = TextAlign.Start
                )
            }
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
