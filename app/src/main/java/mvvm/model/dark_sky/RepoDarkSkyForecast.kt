package mvvm.model.dark_sky

import io.reactivex.Flowable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import mvvm.model.gps.GPSCoordinates
import mvvm.model.gps.RepoGPSCoordinates
import mvvm.model.mapBox.RepoMapBox
import org.jetbrains.anko.AnkoLogger
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.generic.instance
import room.AppEntity
import utils.NetManager
import utils.Status

@ObsoleteCoroutinesApi
class RepoDarkSkyForecast(private val netManager: NetManager, override val kodein: Kodein) : KodeinAware, AnkoLogger {
    private val repoForecastLocation: RepoGPSCoordinates by instance()
    private val repoForecastRemoteData: RepoDarkSkyForecastRemoteData by instance()
    private val mRepoDarkSkyForecastLocalData: RepoDarkSkyForecastLocalData by instance()
    private val repoMapBox: RepoMapBox = RepoMapBox()


    private suspend fun locationDetermination(): GPSCoordinates = withContext(Dispatchers.Default) {
        repoForecastLocation.getLocation().receive()
    }

    private suspend fun mapBoxPlaceName(): String = withContext(Dispatchers.Default) {
        val location = async { locationDetermination() }
        repoMapBox.getLocationName(location.await())
    }

    private suspend fun hasNeedUpdate(): Boolean = withContext(Dispatchers.Default) {
        val placeName = async { mapBoxPlaceName() }
        if (mRepoDarkSkyForecastLocalData.needUpdate(placeName.await())) {
            save(placeName.await())
            true
        } else
            false
    }

    private suspend fun save(placeName: String) = withContext(Dispatchers.Default) {
        mRepoDarkSkyForecastLocalData.saveCityQuery(placeName)
        val forecastRemote = async { repoForecastRemoteData.forecastRemote(locationDetermination()) }
        mRepoDarkSkyForecastLocalData.saveForecastInDb(placeName, forecastRemote.await())
    }


    suspend fun loadForecast(): Status = withContext(Dispatchers.Default) {

        if (netManager.isConnectedToInternet!!) {
            if (!hasNeedUpdate())
                return@withContext Status.DATA_UP_TO_DATE
        } else
            return@withContext Status.NO_NETWORK_CONNECTION

        return@withContext Status.ALL_IS_WELL
    }

    suspend fun db(): Flowable<AppEntity> =
        mRepoDarkSkyForecastLocalData.loadLocalForecast(mRepoDarkSkyForecastLocalData.getCity())
}