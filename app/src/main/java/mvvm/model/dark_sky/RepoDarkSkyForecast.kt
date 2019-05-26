package mvvm.model.dark_sky

import mvvm.model.gps.GPSCoordinates
import mvvm.model.gps.RepoGPSCoordinates
import mvvm.model.mapBox.RepoMapBox
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.generic.instance
import utils.NetManager

class RepoDarkSkyForecast(private val netManager: NetManager, override val kodein: Kodein) : KodeinAware, AnkoLogger {
    private val repoForecastLocation: RepoGPSCoordinates by instance()
    private val repoForecastRemoteData: RepoDarkSkyForecastRemoteData by instance()
    private val mRepoDarkSkyForecastLocalData: RepoDarkSkyForecastLocalData by instance()
    private val repoMapBox: RepoMapBox by instance()
    private lateinit var gpsCoordinates: GPSCoordinates

    suspend fun forecastAsync(): DarkSkyForecast.DarkSky? {
        //TODO: Нужна проверка для выбора источника данных
        return if (netManager.isConnectedToInternet!!) {
            gpsCoordinates = repoForecastLocation.location()
            val map = repoMapBox.getLocationName(gpsCoordinates)
            info { "TEST ${map}" }
            // val remoteResult = repoForecastRemoteData.forecastRemoteAsync(gpsCoordinates)
            // remoteResult?.let { mRepoDarkSkyForecastLocalData.saveForecastInDb(it) }
            null
        } else {
            //TODO: DB Data return
            null
        }
    }
}