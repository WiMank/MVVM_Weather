package mvvm.model.dark_sky

import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import room.AppDAO
import room.AppEntity
import room.CityDAO
import room.CityEntity
import utils.ONE_HOUR

class RepoDarkSkyForecastLocalData(
    private val mAppDAO: AppDAO,
    private val mCityDAO: CityDAO
) : AnkoLogger {

    suspend fun saveForecastInDb(cityName: String, darkSky: DarkSkyForecast.DarkSky) {
        val currentTime = System.currentTimeMillis()
        mAppDAO.insert(
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
                darkSky.currently.visibility,
                darkSky.currently.icon,
                darkSky.daily,
                darkSky.hourly
            )
        )
    }

    suspend fun checkNeedUpdate(q: String): Boolean {
        return if (q == mCityDAO.getCityName()?.cityName ?: "") {
            info { "UpdateTime: [${mAppDAO.updateTime(q)}] CurrentTimeMillis: [${System.currentTimeMillis()}]" }
            mAppDAO.updateTime(q) ?: 0 < System.currentTimeMillis()
        } else true
    }

    suspend fun saveCityQuery(cityName: String) = mCityDAO.insert(CityEntity(0, cityName))

    fun getCity(): String = mCityDAO.getCityName()?.cityName ?: ""

    suspend fun forecastDB(cityName: String): AppEntity = mAppDAO.getByNameAsync(cityName)

}
