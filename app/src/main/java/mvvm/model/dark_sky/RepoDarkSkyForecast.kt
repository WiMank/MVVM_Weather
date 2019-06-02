package mvvm.model.dark_sky

import io.reactivex.Flowable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import mvvm.model.gps.GPSCoordinates
import mvvm.model.gps.RepoGPSCoordinates
import mvvm.model.mapBox.RepoMapBox
import mvvm.model.status.Status
import mvvm.model.status.StatusChannel
import org.jetbrains.anko.AnkoLogger
import room.AppEntity
import utils.NetManager

@ExperimentalCoroutinesApi
class RepoDarkSkyForecast(
    private val repoForecastRemoteData: RepoDarkSkyForecastRemoteData,
    private val repoForecastLocation: RepoGPSCoordinates,
    private val mRepoDarkSkyForecastLocalData: RepoDarkSkyForecastLocalData,
    private val repoMapBox: RepoMapBox,
    private val netManager: NetManager
) : AnkoLogger {


    private suspend fun locationDetermination() = withContext(Dispatchers.Default) {
        StatusChannel.sendStatus(Status.LOCATION_DETERMINATION)
        val locationDetermination = async { repoForecastLocation.getLocation().receive() }
        mapBoxPlaceName(locationDetermination.await())
    }

    private suspend fun mapBoxPlaceName(gpsCoordinates: GPSCoordinates) = withContext(Dispatchers.Default) {
        StatusChannel.sendStatus(Status.LOOKING_FOR_LOCATION_NAME)
        val mapBoxPlaceName = async { repoMapBox.getLocationName(gpsCoordinates) }
        hasNeedUpdate(mapBoxPlaceName.await(), gpsCoordinates)
    }

    private suspend fun hasNeedUpdate(placeName: String, gpsCoordinates: GPSCoordinates) =
        withContext(Dispatchers.Default) {
            StatusChannel.sendStatus(Status.UPDATE_NEEDED)
            if (mRepoDarkSkyForecastLocalData.checkNeedUpdate(placeName)) {
                save(placeName, gpsCoordinates)
            } else
                StatusChannel.sendStatus(Status.DATA_UP_TO_DATE)
        }

    private suspend fun save(placeName: String, gpsCoordinates: GPSCoordinates) = withContext(Dispatchers.Default) {
        StatusChannel.sendStatus(Status.SAVE_THE_DATA)
        mRepoDarkSkyForecastLocalData.saveForecastInDb(placeName, repoForecastRemoteData.forecastRemote(gpsCoordinates))
        mRepoDarkSkyForecastLocalData.saveCityQuery(placeName)
        StatusChannel.sendStatus(Status.READY)
    }


    suspend fun loadForecast() = withContext(Dispatchers.Default) {
        if (netManager.isConnectedToInternet!!)
            locationDetermination()
        else
            StatusChannel.sendStatus(Status.NO_NETWORK_CONNECTION)
    }

    suspend fun db(): Flowable<AppEntity> {
        return mRepoDarkSkyForecastLocalData.loadLocalForecast(mRepoDarkSkyForecastLocalData.getCity())
    }
}