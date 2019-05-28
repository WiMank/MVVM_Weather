package mvvm.model.dark_sky

import kotlinx.coroutines.async
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import mvvm.model.gps.RepoGPSCoordinates
import mvvm.model.mapBox.RepoMapBox
import org.jetbrains.anko.AnkoLogger
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.generic.instance
import utils.NetManager

class RepoDarkSkyForecast(private val netManager: NetManager, override val kodein: Kodein) : KodeinAware, AnkoLogger {
    private val repoForecastLocation: RepoGPSCoordinates by instance()
    private val repoForecastRemoteData: RepoDarkSkyForecastRemoteData by instance()
    private val mRepoDarkSkyForecastLocalData: RepoDarkSkyForecastLocalData by instance()
    private val repoMapBox: RepoMapBox = RepoMapBox()


    suspend fun loadForecast() = runBlocking {
        if (netManager.isConnectedToInternet!!) {
            repoForecastLocation.getLocation().consumeEach {
                launch {
                    val cityName = async { repoMapBox.getLocationName(it) }
                    if (mRepoDarkSkyForecastLocalData.needUpdate(cityName.await())) {
                        val forecastRemote = async { repoForecastRemoteData.forecastRemote(it) }
                        mRepoDarkSkyForecastLocalData.saveForecastInDb(cityName.await(), forecastRemote.await())
                        mRepoDarkSkyForecastLocalData.saveCityQuery(cityName.await())
                    }
                }
            }
        }
    }
}