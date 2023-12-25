package net.radstevee.weatherapp.ui

import android.graphics.Bitmap
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import net.radstevee.weatherapp.BitmapImage
import net.radstevee.weatherapp.MainActivity
import net.radstevee.weatherapp.ui.theme.Foreground
import net.radstevee.weatherapp.weather.Day
import net.radstevee.weatherapp.weather.WeatherApi
import kotlin.math.floor

@Composable
fun WeatherDayCard(day: Day, navController: NavController) {
    val dayMinTemperature = day.getMinTemperature()
    val dayMaxTemperature = day.getMaxTemperature()
    val dayAverageTemperature = day.getAverageTemperature()
    var iconBitmap by remember {
        mutableStateOf<Bitmap?>(null)
    }
    LaunchedEffect(day.getIcon()) {
        WeatherApi.getWeatherIcon(day.getIcon()) {
            iconBitmap = it
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
            .padding(5.dp)
            .clickable {
                MainActivity.CURRENT_FORECAST = day
                navController.navigate("forecast")
            }
    ) {
        // Date at the top
        Text(
            text = day.formatDate(),
            fontWeight = FontWeight.Bold,
            color = Foreground,
            modifier = Modifier
                .padding(bottom = 8.dp)
                .align(Alignment.CenterHorizontally)
        )

        Row(modifier = Modifier.padding(start = 15.dp)) {
            Box(modifier = Modifier.align(Alignment.CenterVertically)) {
                // Icon
                if (iconBitmap != null) {
                    BitmapImage(
                        bitmap = iconBitmap!!,
                        modifier = Modifier
                            .size(85.dp)
                    )
                }
            }

            // Temperature details stacked below each other
            Column {
                Text(
                    text = "Min: ${floor(dayMinTemperature).toInt()}°C",
                    color = Foreground,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 4.dp)
                )

                Text(
                    text = "Max: ${floor(dayMaxTemperature).toInt()}°C",
                    color = Foreground,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 4.dp)
                )

                Text(
                    text = "Average: ${floor(dayAverageTemperature).toInt()}°C",
                    color = Foreground,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        // Spacing between rows
        Spacer(modifier = Modifier.height(8.dp))
    }
}