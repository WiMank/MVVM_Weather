package mvvm.model.dark_sky

import room.AppDAO
import room.AppEntity
import room.CityDAO
import room.CityEntity
import utils.ONE_HOUR


class RepoDarkSkyForecastLocalData(private val appDAO: AppDAO, private val cityDAO: CityDAO) {

    suspend fun saveForecastInDb(cityName: String, darkSky: DarkSkyForecast.DarkSky) {
        val currentTime = System.currentTimeMillis()
        appDAO.insert(
            AppEntity(
                0,
                cityName,
                darkSky.longitude,
                darkSky.latitude,
                currentTime + ONE_HOUR,
                currentTime,
                darkSky.currently.summary,
                darkSky.currently.precipProbability,
                darkSky.currently.precipType,
                darkSky.currently.temperature,
                darkSky.currently.apparentTemperature,
                darkSky.currently.humidity,
                darkSky.currently.pressure,
                darkSky.currently.windSpeed,
                darkSky.currently.windGust,
                darkSky.currently.cloudCover,
                darkSky.currently.visibility
            )
        )
    }

    suspend fun needUpdate(q: String): Boolean {
        return if (q == cityDAO.getCityName().cityName)
            appDAO.hasNeedUpdate(cityDAO.getCityName().cityName, System.currentTimeMillis())
        else
            false
    }

    suspend fun saveCityQuery(cityName: String) {
        return cityDAO.insert(CityEntity(0, cityName))
    }
}