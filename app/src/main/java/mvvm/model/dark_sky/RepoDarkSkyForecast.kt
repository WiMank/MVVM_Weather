package mvvm.model.dark_sky

import kotlinx.coroutines.ExperimentalCoroutinesApi
import mvvm.model.RepoPreference
import mvvm.model.gps.GPSCoordinates
import mvvm.model.gps.RepoGPSCoordinates
import mvvm.model.mapBox.RepoMapBox
import mvvm.model.status.Status
import mvvm.model.status.StatusChannel
import org.jetbrains.anko.AnkoLogger
import room.AppEntity
import utils.NetManager
import utils.REPLACE_PREF

@ExperimentalCoroutinesApi
class RepoDarkSkyForecast(
    private val mRepoForecastRemoteData: RepoDarkSkyForecastRemoteData,
    private val mRepoForecastLocation: RepoGPSCoordinates,
    private val mRepoDarkSkyForecastLocalData: RepoDarkSkyForecastLocalData,
    private val mRepoMapBox: RepoMapBox,
    private val mNetManager: NetManager,
    private val mStatusChannel: StatusChannel,
    private val mPreference: RepoPreference
) : AnkoLogger {

    private suspend fun placeCoordinates(place: String) {
        mStatusChannel.sendStatus(Status.PLACE_COORDINATES)
        hasNeedUpdate(place, mRepoMapBox.coordinatesByName(place))
    }

    private suspend fun locationDetermination() {
        mStatusChannel.sendStatus(Status.LOCATION_DETERMINATION)
        mapBoxPlaceName(mRepoForecastLocation.getLocation().receive())
    }

    private suspend fun mapBoxPlaceName(gpsCoordinates: GPSCoordinates) {
        mStatusChannel.sendStatus(Status.LOOKING_FOR_LOCATION_NAME)
        hasNeedUpdate(mRepoMapBox.locationName(gpsCoordinates), gpsCoordinates)
    }

    private suspend fun hasNeedUpdate(placeName: String, gpsCoordinates: GPSCoordinates) {
        mStatusChannel.sendStatus(Status.UPDATE_NEEDED)
        when {
            mPreference.getBooleanSettings(REPLACE_PREF) -> {
                save(placeName, gpsCoordinates)
                mPreference.saveSettings(REPLACE_PREF, false)
            }
            mRepoDarkSkyForecastLocalData.checkNeedUpdate(placeName) -> save(placeName, gpsCoordinates)
            else -> mStatusChannel.sendStatus(Status.DATA_UP_TO_DATE)
        }
    }

    private suspend fun save(placeName: String, gpsCoordinates: GPSCoordinates) {
        mStatusChannel.sendStatus(Status.SAVE_THE_DATA)
        mRepoDarkSkyForecastLocalData.saveForecastInDb(
            placeName,
            mRepoForecastRemoteData.forecastRemote(gpsCoordinates)
        )
        mRepoDarkSkyForecastLocalData.saveCityQuery(placeName)
        mStatusChannel.sendStatus(Status.DONE)
    }

    suspend fun loadGPSForecast() {
        if (mNetManager.isConnectedToInternet!!)
            locationDetermination()
        else mStatusChannel.sendStatus(Status.NO_NETWORK_CONNECTION)
    }

    suspend fun loadPlaceNameCoordinates(place: String) {
        if (mNetManager.isConnectedToInternet!!)
            placeCoordinates(place)
        else mStatusChannel.sendStatus(Status.NO_NETWORK_CONNECTION)
    }

    suspend fun db(): AppEntity {
        return mRepoDarkSkyForecastLocalData.forecastDB(mRepoDarkSkyForecastLocalData.getCity())
    }
}
