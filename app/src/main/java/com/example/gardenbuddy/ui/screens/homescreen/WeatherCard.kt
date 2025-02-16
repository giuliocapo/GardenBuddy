package com.example.gardenbuddy.ui.screens.homescreen


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gardenbuddy.utils.WeatherIconCode


@Composable
fun WeatherCard(
    location: String,
    temperature: String,
    summary: String,
    weatherIcon: WeatherIconCode,
    modifier: Modifier = Modifier
) {
    val gradient = Brush.verticalGradient(
        colors = listOf(Color(0xFF35509B), Color(0xFF60A5FA))
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
            .shadow(elevation = 16.dp, shape = RoundedCornerShape(16.dp))
            .background(brush = gradient, shape = RoundedCornerShape(16.dp)), // Applica il gradiente
    ){

            Row(
                modifier = modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = modifier.weight(1f)){
//
                    if (weatherIcon == WeatherIconCode.SUNNY)
                        SunIcon(modifier)
                    else if (weatherIcon == WeatherIconCode.CLOUDY)
                        SunWithCloudsIcon(modifier)
                    else if (weatherIcon == WeatherIconCode.RAINY)
                        CloudsAndRainIcon()
                }

                // Sezione Informazioni Meteo
                Column(
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = location,
                        color = Color.White,
                        textAlign = TextAlign.Start,
                        style = MaterialTheme.typography.titleLarge,

                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = temperature,
                        fontSize = 24.sp,
                        color = Color.White,
                        textAlign = TextAlign.Start,
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = summary,
                        fontSize = 16.sp,
                        color = Color.White,
                        textAlign = TextAlign.Start,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

    }
}


@Preview
@Composable
fun SunnyWeatherCardPreview(){
    Box(
        modifier = Modifier.size(width = 500.dp, height = 160.dp)
        .background(color = MaterialTheme.colorScheme.background)
        ){
        WeatherCard(
            location = "Rome",
            temperature = "18°C",
            summary = "Sunny",
            weatherIcon = WeatherIconCode.SUNNY,
            modifier = Modifier
        )
    }
}

@Preview
@Composable
fun CloudyWeatherCardPreview() {
    WeatherCard(
        location = "Rome",
        temperature = "18°C",
        summary = "Cloudy",
        weatherIcon = WeatherIconCode.CLOUDY,
        modifier = Modifier
    )
}

@Preview
@Composable
fun RainyWeatherCardPreview() {
    WeatherCard(
        location = "Rome",
        temperature = "18°C",
        summary = "Rainy",
        weatherIcon = WeatherIconCode.RAINY,
        modifier = Modifier
    )
}
