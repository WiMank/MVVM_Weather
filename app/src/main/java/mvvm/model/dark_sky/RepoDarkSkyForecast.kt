package mvvm.model.dark_sky

import mvvm.model.RepoGPSCoordinates
import org.jetbrains.anko.AnkoLogger
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.generic.instance
import utils.NetManager

class RepoDarkSkyForecast(private val netManager: NetManager, override val kodein: Kodein) : KodeinAware, AnkoLogger {
    private val repoForecastLocation: RepoGPSCoordinates by instance()
    private val repoForecastRemoteData: RepoDarkSkyForecastRemoteData by instance()
    private val mRepoDarkSkyForecastLocalData: RepoDarkSkyForecastLocalData by instance()

    suspend fun forecastAsync(): DarkSkyForecast.DarkSky? {
        //TODO: Нужна проверка для выбора источника данных
        return if (netManager.isConnectedToInternet!!) {
            val remoteResult = repoForecastRemoteData.forecastRemoteAsync(repoForecastLocation.location())
            remoteResult?.let { mRepoDarkSkyForecastLocalData.saveForecastInDb(it) }
            remoteResult
        } else {
            //TODO: DB Data return
            null
        }
    }
}