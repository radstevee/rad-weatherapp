package net.radstevee.weatherapp.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import net.radstevee.weatherapp.MainActivity
import net.radstevee.weatherapp.ui.theme.Background
import net.radstevee.weatherapp.ui.theme.Foreground
import net.radstevee.weatherapp.weather.Day
import net.radstevee.weatherapp.weather.format

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherDayPage(day: Day, navController: NavController) {
    val forecasts = listOf(
        day.midnight,
        day.`3am`,
        day.`6am`,
        day.`9am`,
        day.noon,
        day.`3pm`,
        day.`6pm`,
        day.`9pm`
    )

    Scaffold(
        topBar = {
            TopAppBar(
                modifier = Modifier
                    .background(Background)
                    .fillMaxWidth(),
                navigationIcon = {
                    IconButton(
                        onClick = {
                            MainActivity.CURRENT_FORECAST = null
                            navController.navigate("home")
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            modifier = Modifier.padding(top = 7.dp)
                        )
                    }
                },
                title = {
                    Text(
                        text = "Weather forecast for the ${day.date.format("dd.MM.yy")}",
                        color = Foreground,
                        fontWeight = FontWeight.Bold
                    )
                },
            )
        }
    ) {
        LazyColumn(modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(top = 56.dp)
        ) {
            items(forecasts) { forecast ->
                if (forecast != null) {
                    WeatherForecastCard(forecast = forecast)
                }
            }
        }
    }
}