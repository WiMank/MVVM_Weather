package mvvm.model.dark_sky

import room.AppDAO


class RepoDarkSkyForecastLocalData(private val appDAO: AppDAO) {
    private val currentTime = System.currentTimeMillis()

    fun saveForecastInDb(darkSky: DarkSkyForecast.DarkSky) {
        /*appDAO.insert(
            AppEntity(0, "TEST", darkSky.latitude, darkSky.longitude, currentTime + ONE_HOUR)
        )*/
    }

    fun getLocalForecast() {

    }


    fun checkNeedUpdate(): Boolean {
        return true
    }


}