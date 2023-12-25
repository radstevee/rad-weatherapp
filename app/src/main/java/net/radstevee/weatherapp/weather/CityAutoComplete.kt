package net.radstevee.weatherapp.weather

import com.google.gson.Gson
import com.google.gson.stream.JsonReader
import net.radstevee.weatherapp.MainActivity
import java.io.InputStream
import java.io.InputStreamReader

data class CityLocation(
    val id: Long?,
    val name: String?,
    val state: String?,
    val country: String?,
    val coord: CityCoordinates?
)

data class CityCoordinates(
    val lon: Double?,
    val lat: Double?
)

object CityAutoComplete {
    private val gson = Gson()

    fun readJsonStream(inputStream: InputStream?): List<CityLocation> {
        val cities: MutableList<CityLocation> = mutableListOf()
        try {
            inputStream?.use { stream ->
                JsonReader(InputStreamReader(stream)).use { reader ->
                    reader.beginArray()
                    while (reader.hasNext()) {
                        val city = gson.fromJson<CityLocation>(reader, CityLocation::class.java)
                        cities.add(city)
                    }
                    reader.endArray()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        MainActivity.CITY_LIST = cities
        return cities
    }

    fun getAutocompleteResults(city: String): List<String> {
        val matchingCities = MainActivity.CITY_LIST
            .filter { it.name?.startsWith(city, ignoreCase = true) == true }
            .take(5)

        return matchingCities.mapNotNull { it.name }
    }
}
