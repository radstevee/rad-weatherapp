package net.radstevee.weatherapp.weather

import net.radstevee.weatherapp.MainActivity
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

data class WeatherResponse(
    val cod: Int?,
    val msg: Int?,
    val cnt: Int?,
    val list: List<WeatherForecast>
)

data class WeatherForecast(
    val dt: Long?,
    val main: Main?,
    val weather: List<Weather>?,
    val clouds: Clouds?,
    val wind: Wind?,
    val visibility: Int?,
    val pop: Double?,
    val rain: Rain?,
    val sys: Sys?,
    val dt_txt: String?
) {
    companion object {
        fun groupMultipleToDay(forecasts: List<WeatherForecast>): List<Day> {
            val sortedForecasts = forecasts.sortedBy { it.dt }

            val forecastsWithDate = sortedForecasts.groupBy {
                LocalDateTime.ofEpochSecond(it.dt ?: 0, 0, ZoneOffset.UTC).toLocalDate()
            }

            val days = Day.createFromForecasts(
                forecastsWithDate,
                MainActivity.CURRENT_CITY ?: CityLocation(null, null, null, null, null)
            )
            MainActivity.CURRENT_FORECASTS = days.toMutableList()
            return days
        }
    }
}

data class Main(
    val temp: Double?,
    val feels_like: Double?,
    val temp_min: Double?,
    val temp_max: Double?,
    val pressure: Int?,
    val sea_level: Int?,
    val grnd_level: Int?,
    val humidity: Int?,
    val temp_kf: Double?
)

data class Weather(
    val id: Int?,
    val main: String?,
    val description: String?,
    val icon: String?
)

data class Clouds(
    val all: Int?
)

data class Wind(
    val speed: Double?,
    val deg: Int?,
    val gust: Double?
)

data class Rain(
    val `3h`: Double?
)

data class Sys(
    val pod: String?
)

data class Day(
    val date: LocalDate,
    val location: CityLocation,
    val midnight: WeatherForecast?,
    val `3am`: WeatherForecast?,
    val `6am`: WeatherForecast?,
    val `9am`: WeatherForecast?,
    val noon: WeatherForecast?,
    val `3pm`: WeatherForecast?,
    val `6pm`: WeatherForecast?,
    val `9pm`: WeatherForecast?
) {
    companion object {
        fun createFromForecasts(groupedForecasts: Map<LocalDate, List<WeatherForecast>>, location: CityLocation): List<Day> {
            return groupedForecasts.map { (date, forecasts) ->
                val forecastsMap = forecasts.associateBy { forecast ->
                    LocalDateTime.ofEpochSecond(forecast.dt ?: 0, 0, ZoneOffset.UTC).toLocalTime()
                }

                Day(
                    date = date,
                    location = location,
                    midnight = forecastsMap[LocalTime.MIDNIGHT],
                    `3am` = forecastsMap[LocalTime.of(3, 0)],
                    `6am` = forecastsMap[LocalTime.of(6, 0)],
                    `9am` = forecastsMap[LocalTime.of(9, 0)],
                    noon = forecastsMap[LocalTime.NOON],
                    `3pm` = forecastsMap[LocalTime.of(15, 0)],
                    `6pm` = forecastsMap[LocalTime.of(18, 0)],
                    `9pm` = forecastsMap[LocalTime.of(21, 0)]
                )
            }
        }
    }

    fun getMinTemperature(): Double {
        return (listOfNotNull(
            midnight?.main?.temp_min,
            `3am`?.main?.temp_min,
            `6am`?.main?.temp_min,
            `9am`?.main?.temp_min,
            noon?.main?.temp_min,
            `3pm`?.main?.temp_min,
            `6pm`?.main?.temp_min,
            `9pm`?.main?.temp_min
        ).min())
    }

    fun getMaxTemperature(): Double {
        return (listOfNotNull(
            midnight?.main?.temp_max,
            `3am`?.main?.temp_max,
            `6am`?.main?.temp_max,
            `9am`?.main?.temp_max,
            noon?.main?.temp_max,
            `3pm`?.main?.temp_max,
            `6pm`?.main?.temp_max,
            `9pm`?.main?.temp_max
        ).max())
    }

    fun getAverageTemperature(): Double {
        return (listOfNotNull(
            midnight?.main?.temp,
            `3am`?.main?.temp,
            `6am`?.main?.temp,
            `9am`?.main?.temp,
            noon?.main?.temp,
            `3pm`?.main?.temp,
            `6pm`?.main?.temp,
            `9pm`?.main?.temp
        ).average())
    }

    fun getIcon(): String {
        return noon?.weather?.firstOrNull()?.icon ?: "01d"
    }

    fun formatDate(): String {
        // Format by DD.MM.YY
        return date.format("dd.MM.yy")
    }
}

fun Long.toLocalDateTime(): LocalDateTime {
    return Instant.ofEpochSecond(this).atZone(ZoneOffset.UTC).toLocalDateTime()
}

fun LocalDate.format(pattern: String): String {
    val formatter = DateTimeFormatter.ofPattern(pattern)
    return formatter.format(this)
}

fun LocalDateTime.format(pattern: String): String {
    val formatter = DateTimeFormatter.ofPattern(pattern)
    return formatter.format(this)
}