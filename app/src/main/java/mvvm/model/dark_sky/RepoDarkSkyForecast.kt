package mvvm.model.dark_sky

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import mvvm.model.gps.RepoGPSCoordinates
import mvvm.model.mapBox.RepoMapBox
import org.jetbrains.anko.AnkoLogger
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.generic.instance
import room.AppEntity
import utils.NetManager

class RepoDarkSkyForecast(private val netManager: NetManager, override val kodein: Kodein) : KodeinAware, AnkoLogger {
    private val repoForecastLocation: RepoGPSCoordinates by instance()
    private val repoForecastRemoteData: RepoDarkSkyForecastRemoteData by instance()
    private val mRepoDarkSkyForecastLocalData: RepoDarkSkyForecastLocalData by instance()
    private val repoMapBox: RepoMapBox = RepoMapBox()
    private val appEntity = Channel<AppEntity>()


    suspend fun loadForecast(): Channel<AppEntity> = withContext(Dispatchers.Default) {
        if (netManager.isConnectedToInternet!!) {
            repoForecastLocation.getLocation().consumeEach {
                launch {
                    val lCityName = async { repoMapBox.getLocationName(it) }
                    if (mRepoDarkSkyForecastLocalData.needUpdate(lCityName.await())) {
                        val forecastRemote = async { repoForecastRemoteData.forecastRemote(it) }
                        mRepoDarkSkyForecastLocalData.saveForecastInDb(lCityName.await(), forecastRemote.await())
                        mRepoDarkSkyForecastLocalData.saveCityQuery(lCityName.await())
                        appEntity.send(mRepoDarkSkyForecastLocalData.loadLocalForecast(lCityName.await()))
                        appEntity.close()
                    } else {
                        appEntity.send(mRepoDarkSkyForecastLocalData.loadLocalForecast(lCityName.await()))
                        appEntity.close()
                    }
                }
            }
        }
        appEntity
    }
}