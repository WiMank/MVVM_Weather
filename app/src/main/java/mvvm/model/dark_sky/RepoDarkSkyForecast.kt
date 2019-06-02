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

    private suspend fun locationDetermination() = withContext(Dispatchers.Default) {
        StatusRepo.setStatus(Status.LOCATION_DETERMINATION)
        val locationDetermination = async { repoForecastLocation.getLocation().receive() }
        mapBoxPlaceName(locationDetermination.await())
    }

    private suspend fun mapBoxPlaceName(gpsCoordinates: GPSCoordinates) = withContext(Dispatchers.Default) {
        StatusRepo.setStatus(Status.LOOKING_FOR_LOCATION_NAME)
        val mapBoxPlaceName = async { repoMapBox.getLocationName(gpsCoordinates) }
        hasNeedUpdate(mapBoxPlaceName.await(), gpsCoordinates)
    }

    private suspend fun hasNeedUpdate(placeName: String, gpsCoordinates: GPSCoordinates) =
        withContext(Dispatchers.Default) {
            if (mRepoDarkSkyForecastLocalData.checkNeedUpdate(placeName)) {
                StatusRepo.setStatus(Status.UPDATE_NEEDED)
                save(placeName, gpsCoordinates)
            } else {
                StatusRepo.setStatus(Status.DATA_UP_TO_DATE)
            }
        }

    private suspend fun save(placeName: String, gpsCoordinates: GPSCoordinates) = withContext(Dispatchers.Default) {
        StatusRepo.setStatus(Status.SAVE_THE_DATA)
        mRepoDarkSkyForecastLocalData.saveForecastInDb(placeName, repoForecastRemoteData.forecastRemote(gpsCoordinates))
        mRepoDarkSkyForecastLocalData.saveCityQuery(placeName)
        StatusRepo.setStatus(Status.READY)
    }


    suspend fun loadForecast() {
        if (netManager.isConnectedToInternet!!)
            locationDetermination()
        else {
            StatusRepo.setStatus(Status.NO_NETWORK_CONNECTION)
        }
    }

    suspend fun db(): Flowable<AppEntity> {
        return mRepoDarkSkyForecastLocalData.loadLocalForecast(mRepoDarkSkyForecastLocalData.getCity())
    }
}