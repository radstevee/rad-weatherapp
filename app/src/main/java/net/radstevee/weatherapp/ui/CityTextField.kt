package net.radstevee.weatherapp.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import net.radstevee.weatherapp.MainActivity
import net.radstevee.weatherapp.getWeatherForecast
import net.radstevee.weatherapp.ui.theme.Background
import net.radstevee.weatherapp.ui.theme.BackgroundSecondary
import net.radstevee.weatherapp.ui.theme.Foreground
import net.radstevee.weatherapp.weather.CityAutoComplete
import net.radstevee.weatherapp.weather.WeatherForecast

@Composable
fun CityTextField(navController: NavController) {
    var citySuggestions by remember {
        mutableStateOf<List<String>?>(emptyList())
    }
    var selectedCity by remember {
        mutableStateOf(MainActivity.CURRENT_CITY?.name ?: "")
    }
    var shouldShowSuggestions by remember {
        mutableStateOf(false)
    }
    var weatherForecasts by remember {
        mutableStateOf<List<WeatherForecast>>(emptyList())
    }

    MainActivity.CURRENT_FORECASTS.isNotEmpty().let {
        val forecasts = mutableListOf<WeatherForecast>()
        MainActivity.CURRENT_FORECASTS.forEach { day ->
            forecasts.add(day.midnight ?: return@forEach)
            forecasts.add(day.`3am` ?: return@forEach)
            forecasts.add(day.`6am` ?: return@forEach)
            forecasts.add(day.`9am` ?: return@forEach)
            forecasts.add(day.noon ?: return@forEach)
            forecasts.add(day.`3pm` ?: return@forEach)
            forecasts.add(day.`6pm` ?: return@forEach)
            forecasts.add(day.`9pm` ?: return@forEach)
        }
        weatherForecasts = forecasts
    }

    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            TextField(
                value = selectedCity,
                onValueChange = {
                    selectedCity = it
                    citySuggestions = CityAutoComplete.getAutocompleteResults(it)
                },
                label = { Text("Enter a City.") },
                maxLines = 2,
                textStyle = TextStyle(
                    color = Foreground,
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier
                    .background(Background)
                    .fillMaxWidth()
                    .onFocusChanged {
                        shouldShowSuggestions = it.isFocused
                    }
                    .padding(horizontal = 75.dp)
            )

            if (shouldShowSuggestions) {
                LazyColumn {
                    items(citySuggestions ?: emptyList()) { city ->
                        Box(
                            modifier = Modifier
                                .background(BackgroundSecondary)
                                .fillMaxWidth()
                                .clickable {
                                    selectedCity = city
                                    MainActivity.CURRENT_CITY =
                                        MainActivity.CITY_LIST.firstOrNull { it.name == city }
                                    citySuggestions = null
                                }
                                .padding(10.dp)
                        ) {
                            Text(text = city, color = Foreground)
                        }
                    }
                }
            }

            Button(
                onClick = {
                    getWeatherForecast(selectedCity) { result ->
                        result?.let { response ->
                            weatherForecasts = response.list
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(75.dp)
                    .padding(vertical = 15.dp, horizontal = 150.dp)
            ) {
                Text(text = "Get Weather", color = Background)
            }

            WeatherForecastList(weatherForecasts = weatherForecasts, navController = navController)
        }
    }
}
