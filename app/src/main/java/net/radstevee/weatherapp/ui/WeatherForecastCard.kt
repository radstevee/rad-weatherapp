package net.radstevee.weatherapp.ui

import android.graphics.Bitmap
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
import net.radstevee.weatherapp.BitmapImage
import net.radstevee.weatherapp.ui.theme.Foreground
import net.radstevee.weatherapp.weather.WeatherApi
import net.radstevee.weatherapp.weather.WeatherForecast
import net.radstevee.weatherapp.weather.format
import net.radstevee.weatherapp.weather.toLocalDateTime
import kotlin.math.floor

@Composable
fun WeatherForecastCard(forecast: WeatherForecast) {
    val weather = forecast.weather?.firstOrNull()
    var date = forecast.dt?.toLocalDateTime()?.format("h a")
    if(date == "" || date == null) date = "Midnight"
    date = when(date) {
        "12 am" -> "Midnight"
        "3 am" -> "3 AM"
        "6 am" -> "6 AM"
        "9 am" -> "9 AM"
        "12 pm" -> "Noon"
        "3 pm" -> "3 PM"
        "6 pm" -> "6 PM"
        "9 pm" -> "9 PM"
        else -> "Midnight"
    }

    // Use WeatherApi to get the Bitmap and convert it to an ImageBitmap
    var iconBitmap by remember {
        mutableStateOf<Bitmap?>(null)
    }
    LaunchedEffect(weather?.icon) {
        WeatherApi.getWeatherIcon(weather?.icon ?: "") {
            iconBitmap = it
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(185.dp)
            .padding(5.dp)
    ) {
        // Date at the top
        Text(
            text = date,
            fontWeight = FontWeight.ExtraBold,
            color = Foreground,
            modifier = Modifier
                .padding(bottom = 8.dp)
                .padding(horizontal = 15.dp)
        )

        Row(modifier = Modifier.padding(start = 15.dp)) {
            Box(modifier = Modifier.align(Alignment.CenterVertically)) {
                // Icon
                if (iconBitmap != null) {
                    BitmapImage(
                        bitmap = iconBitmap!!,
                        modifier = Modifier
                            .size(100.dp)
                    )
                }
            }

            // Temperature details stacked below each other
            Column {
                Text(
                    text = "${floor(forecast.main!!.temp!!).toInt()}°C",
                    color = Foreground,
                    fontWeight = FontWeight.ExtraBold,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                Text(
                    text = forecast.weather!!.first().description!!,
                    color = Foreground,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 4.dp)
                )

                Text(
                    text = "Feels like: ${floor(forecast.main.feels_like!!).toInt()}°C",
                    color = Foreground,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 4.dp)
                )

                Text(
                    text = "Wind speed: ${floor(forecast.wind!!.speed!!).toInt()} km/h",
                    color = Foreground,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        // Spacing between rows
        Spacer(modifier = Modifier.height(8.dp))
    }
}