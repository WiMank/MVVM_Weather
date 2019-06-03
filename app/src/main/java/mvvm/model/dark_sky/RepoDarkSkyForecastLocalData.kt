package mvvm.model.dark_sky

import io.reactivex.Flowable
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import room.AppDAO
import room.AppEntity
import room.CityDAO
import room.CityEntity
import utils.ONE_HOUR


class RepoDarkSkyForecastLocalData(private val appDAO: AppDAO, private val cityDAO: CityDAO) : AnkoLogger {

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

    suspend fun checkNeedUpdate(q: String): Boolean {
        return if (q == cityDAO.getCityName()?.cityName ?: "") {
            info { "UpdateTime: [${appDAO.updateTime(q)}] CurrentTimeMillis: [${System.currentTimeMillis()}]" }
            appDAO.updateTime(q) ?: 0 < System.currentTimeMillis()
        } else true
    }

    suspend fun saveCityQuery(cityName: String) = cityDAO.insert(CityEntity(0, cityName))

    suspend fun getCity(): String = cityDAO.getCityName()?.cityName ?: ""

    fun loadLocalForecast(cityName: String): Flowable<AppEntity> = appDAO.getByNameAsync(cityName)

}