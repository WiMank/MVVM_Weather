package mvvm.model

import kotlinx.coroutines.*
import org.jetbrains.anko.AnkoLogger
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.generic.instance
import rest.pojo.DarkSkyPojo
import utils.NetManager

class RepoForecast(private val netManager: NetManager, override val kodein: Kodein) : KodeinAware, AnkoLogger {
    private lateinit var locationDeferred: Deferred<Coordinates>
    private val repoForecastLocation: RepoForecastLocation by instance()
    private val repoForecastRemoteData: RepoForecastRemoteData by instance()

    suspend fun forecastAsync(): DarkSkyPojo.DarkSky? {
        return if (netManager.isConnectedToInternet!!) {
            runBlocking {
                withContext(Dispatchers.Main) {
                    locationDeferred = async { repoForecastLocation.location() }
                }
                repoForecastRemoteData.forecastRemoteAsync(locationDeferred.await())
            }
        } else {
            //TODO: DB Data return
            null
        }
    }
}