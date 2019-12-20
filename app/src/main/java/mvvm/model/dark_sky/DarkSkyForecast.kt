package mvvm.model.dark_sky

object DarkSkyForecast {

    data class DarkSky(
        val latitude: Double,
        val longitude: Double,
        val timezone: String,
        val currently: Currently,
        val daily: Daily,
        val hourly: Hourly
    )

    data class Currently(
        val time: Int,
        val summary: String,
        val icon: String,
        val nearestStormDistance: Int,
        val precipIntensity: Double,
        val precipIntensityError: Double,
        val precipProbability: Double,
        val precipType: String,
        val temperature: Double,
        val apparentTemperature: Double,
        val dewPoint: Double,
        val humidity: Double,
        val pressure: Double,
        val windSpeed: Double,
        val windGust: Double,
        val windBearing: Int,
        val cloudCover: Double,
        val uvIndex: Int,
        val visibility: Double,
        val ozone: Double
    )

    data class Daily(
        val summary: String,
        val data: MutableList<DailyItem>,
        val icon: String
    )

    data class DailyItem(
        val time: Int,
        val summary: String,
        val icon: String,
        val sunriseTime: Long,
        val sunsetTime: Long,
        val moonPhase: Double,
        val precipIntensity: Double,
        val precipIntensityMax: Double,
        val precipIntensityMaxTime: Long,
        val precipProbability: Double,
        val precipType: String,
        val temperatureHigh: Double,
        val temperatureHighTime: Long,
        val temperatureLow: Double,
        val temperatureLowTime: Long,
        val apparentTemperatureHigh: Double,
        val apparentTemperatureHighTime: Long,
        val apparentTemperatureLow: Double,
        val apparentTemperatureLowTime: Long,
        val dewPoint: Double,
        val humidity: Double,
        val pressure: Double,
        val windSpeed: Double,
        val windGust: Double,
        val windGustTime: Long,
        val windBearing: Int,
        val cloudCover: Double,
        val uvIndex: Int,
        val uvIndexTime: Long,
        val visibility: Double,
        val ozone: Double,
        val temperatureMin: Double,
        val temperatureMinTime: Long,
        val temperatureMax: Double,
        val temperatureMaxTime: Long,
        val apparentTemperatureMin: Double,
        val apparentTemperatureMinTime: Long,
        val apparentTemperatureMax: Double,
        val apparentTemperatureMaxTime: Long
    )

    data class Hourly(
        val summary: String = "",
        val data: List<HourlyItem> = listOf(),
        val icon: String = ""
    )

    data class HourlyItem(
        val time: Long,
        val summary: String,
        val icon: String,
        val precipIntensity: Double,
        val precipProbability: Double,
        val precipType: String,
        val temperature: Double,
        val apparentTemperature: Double,
        val dewPoint: Double,
        val humidity: Double,
        val pressure: Double,
        val windSpeed: Double,
        val windGust: Double,
        val windBearing: Int,
        val cloudCover: Double,
        val uvIndex: Int,
        val visibility: Double,
        val ozone: Double
    )
}
