package net.radstevee.weatherapp

import android.graphics.Bitmap
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import net.radstevee.weatherapp.ui.CityTextField
import net.radstevee.weatherapp.ui.WeatherDayPage
import net.radstevee.weatherapp.ui.theme.WeatherAppTheme
import net.radstevee.weatherapp.weather.CityAutoComplete
import net.radstevee.weatherapp.weather.CityLocation
import net.radstevee.weatherapp.weather.Day
import net.radstevee.weatherapp.weather.WeatherApi
import net.radstevee.weatherapp.weather.WeatherResponse
import java.io.BufferedReader
import java.io.InputStream

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        CITY_LIST_STREAM = assets.open("city.list.json")
        OPENWEATHERMAP_API_KEY = assets.open("openweathermap_api_key.txt").bufferedReader().use(
            BufferedReader::readText)
        CityAutoComplete.readJsonStream(CITY_LIST_STREAM)
        setContent {
            WeatherAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                ) {
                    val navController = rememberNavController()
                    NavHost(navController = navController, startDestination = "home") {
                        composable("home") {
                            CityTextField(navController = navController)
                        }
                        composable("forecast") {
                            WeatherDayPage(
                                day = CURRENT_FORECAST ?: return@composable,
                                navController = navController
                            )
                        }
                    }
                }
            }
        }
    }

    companion object {
        var OPENWEATHERMAP_API_KEY = ""
        var CITY_LIST_STREAM: InputStream? = null
        var CITY_LIST: MutableList<CityLocation> = mutableListOf()
        var CURRENT_FORECAST: Day? = null
        var CURRENT_FORECASTS: MutableList<Day> = mutableListOf()
        var CURRENT_CITY: CityLocation? = null
    }
}

@OptIn(DelicateCoroutinesApi::class)
fun getWeatherForecast(city: String, onResult: (WeatherResponse?) -> Unit) {
    GlobalScope.launch {
        val result = WeatherApi.getWeatherForecast(city)
        onResult(result)
    }
}

@Composable
fun BitmapImage(bitmap: Bitmap, modifier: Modifier) {
    val imageBitmap = bitmap.asImageBitmap()

    // Use the Canvas composable to draw the ImageBitmap
    Canvas(
        modifier = modifier.fillMaxSize(),
        onDraw = {
            drawImage(imageBitmap)
        },
    )
}

