package mvvm.model.dark_sky

import com.wimank.mvvm.weather.R

class WeatherIcons {
    fun map(): Map<String, Int> {
        val weatherIconMap = HashMap<String, Int>()
        weatherIconMap["clear-day"] = R.drawable.ic_sun
        weatherIconMap["clear-night"] = R.drawable.ic_moon
        weatherIconMap["rain"] = R.drawable.ic_drops
        weatherIconMap["snow"] = R.drawable.ic_snowflake
        weatherIconMap["sleet"] = R.drawable.ic_sleet
        weatherIconMap["wind"] = R.drawable.ic_wind
        weatherIconMap["fog"] = R.drawable.ic_fog
        weatherIconMap["cloudy"] = R.drawable.ic_cloudy
        weatherIconMap["partly-cloudy-day"] = R.drawable.ic_partly_cloudy_day
        weatherIconMap["partly-cloudy-night"] = R.drawable.ic_partly_cloudy_night
        return weatherIconMap
    }
}