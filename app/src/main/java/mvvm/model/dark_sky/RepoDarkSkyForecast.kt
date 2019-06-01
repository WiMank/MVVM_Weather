package mvvm.model.dark_sky

import io.reactivex.Flowable
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
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
    private val scope = CoroutineScope(Dispatchers.Default)
    private val statusChannel = Channel<Status>()

    private suspend fun locationDetermination() = withContext(Dispatchers.Default) {
        sendStatus(Status.LOCATION_DETERMINATION)
        val locationDetermination = async { repoForecastLocation.getLocation().receive() }
        mapBoxPlaceName(locationDetermination.await())
    }

    private suspend fun mapBoxPlaceName(gpsCoordinates: GPSCoordinates) = withContext(Dispatchers.Default) {
        sendStatus(Status.LOOKING_FOR_LOCATION_NAME)
        val mapBoxPlaceName = async { repoMapBox.getLocationName(gpsCoordinates) }
        hasNeedUpdate(mapBoxPlaceName.await(), gpsCoordinates)
    }

    private suspend fun hasNeedUpdate(placeName: String, gpsCoordinates: GPSCoordinates) =
        withContext(Dispatchers.Default) {

            if (mRepoDarkSkyForecastLocalData.checkNeedUpdate(placeName)) {
                sendStatus(Status.UPDATE_NEEDED)
                save(placeName, gpsCoordinates)
            } else
                sendStatus(Status.DATA_UP_TO_DATE)
        }

    private suspend fun save(placeName: String, gpsCoordinates: GPSCoordinates) = withContext(Dispatchers.Default) {
        sendStatus(Status.SAVE_THE_DATA)
        mRepoDarkSkyForecastLocalData.saveForecastInDb(placeName, repoForecastRemoteData.forecastRemote(gpsCoordinates))
        mRepoDarkSkyForecastLocalData.saveCityQuery(placeName)
    }


    suspend fun loadForecast() = withContext(Dispatchers.Default) {
        if (netManager.isConnectedToInternet!!) {
            locationDetermination()
        }
    }

    suspend fun db(): Flowable<AppEntity> {
        sendStatus(Status.READY)
        return mRepoDarkSkyForecastLocalData.loadLocalForecast(mRepoDarkSkyForecastLocalData.getCity())
    }


    private suspend fun sendStatus(status: Status) {
        scope.launch {
            statusChannel.send(status)
        }
    }

    fun channel(): Channel<Status> = statusChannel

}