package mvvm.model.dark_sky

import io.reactivex.Flowable
import kotlinx.coroutines.ExperimentalCoroutinesApi
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


    private suspend fun locationDetermination() {
        StatusChannel.sendStatus(Status.LOCATION_DETERMINATION)
        mapBoxPlaceName(repoForecastLocation.getLocation().receive())
    }

    private suspend fun mapBoxPlaceName(gpsCoordinates: GPSCoordinates) {
        StatusChannel.sendStatus(Status.LOOKING_FOR_LOCATION_NAME)
        hasNeedUpdate(repoMapBox.getLocationName(gpsCoordinates), gpsCoordinates)
    }

    private suspend fun hasNeedUpdate(placeName: String, gpsCoordinates: GPSCoordinates) {
        StatusChannel.sendStatus(Status.UPDATE_NEEDED)
        if (mRepoDarkSkyForecastLocalData.checkNeedUpdate(placeName))
            save(placeName, gpsCoordinates)
        else StatusChannel.sendStatus(Status.DATA_UP_TO_DATE)
    }

    private suspend fun save(placeName: String, gpsCoordinates: GPSCoordinates) {
        StatusChannel.sendStatus(Status.SAVE_THE_DATA)
        mRepoDarkSkyForecastLocalData.saveForecastInDb(placeName, repoForecastRemoteData.forecastRemote(gpsCoordinates))
        mRepoDarkSkyForecastLocalData.saveCityQuery(placeName)
        StatusChannel.sendStatus(Status.READY)
    }


    suspend fun loadGPSForecast() {
        if (netManager.isConnectedToInternet!!)
            locationDetermination()
        else StatusChannel.sendStatus(Status.NO_NETWORK_CONNECTION)
    }

    suspend fun db(): Flowable<AppEntity> {
        return mRepoDarkSkyForecastLocalData.loadLocalForecast(mRepoDarkSkyForecastLocalData.getCity())
    }
}