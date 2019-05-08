package rest.pojo

object DarkSkyModel {

    data class DarkSky(
        val latitude: Double,
        val longitude: Double,
        val timezone: String,
        val currently: Currently
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
}