package net.radstevee.weatherapp.weather

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.google.gson.Gson
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import net.radstevee.weatherapp.MainActivity
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

object WeatherApi {
    private const val BASE_URL = "https://api.openweathermap.org/data/2.5"
    private const val ICON_BASE_URL = "https://openweathermap.org/img/wn/"

    @OptIn(DelicateCoroutinesApi::class)
    fun getWeatherIcon(condition: String, callback: (Bitmap?) -> Unit) {
        val url = URL("$ICON_BASE_URL/$condition@2x.png")
        GlobalScope.launch {
            try {
                val connection: HttpURLConnection = url.openConnection() as HttpURLConnection
                val inputStream: InputStream = connection.inputStream

                withContext(Dispatchers.Main) {
                    // Access the UI thread to update the UI
                    callback(inputStream.toPicture())
                }
            } catch (e: Exception) {
                // Handle exceptions, e.g., network error
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    callback(null)
                }
            }
        }
    }

    private fun InputStream.toPicture(): Bitmap? {
        return try {
            BitmapFactory.decodeStream(this)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun getWeatherForecast(city: String): WeatherResponse? {
        val url =
            URL("$BASE_URL/forecast?q=$city&units=metric&appid=${MainActivity.OPENWEATHERMAP_API_KEY}")
        val connection: HttpURLConnection = url.openConnection() as HttpURLConnection

        try {
            connection.requestMethod = "GET"
            connection.connect()

            val responseCode = connection.responseCode

            if (responseCode != 200) {
                return null
            }

            val inputStream = connection.inputStream
            val reader = BufferedReader(InputStreamReader(inputStream))
            val response = StringBuilder()
            var line: String?

            while (reader.readLine().also { line = it } != null) {
                response.append(line)
            }

            reader.close()

            return Gson().fromJson(response.toString(), WeatherResponse::class.java)
        } finally {
            connection.disconnect()
        }
    }
}
