package net.radstevee.weatherapp.ui

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import net.radstevee.weatherapp.weather.WeatherForecast

@Composable
fun WeatherForecastList(weatherForecasts: List<WeatherForecast>, navController: NavController) {
    val days = WeatherForecast.groupMultipleToDay(weatherForecasts)

    LazyColumn {
        items(days) {
            WeatherDayCard(day = it, navController = navController)
        }
    }
}