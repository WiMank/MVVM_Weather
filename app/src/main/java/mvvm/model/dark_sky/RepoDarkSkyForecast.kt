package mvvm.model.dark_sky

import io.reactivex.Flowable
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import mvvm.model.gps.GPSCoordinates
import mvvm.model.gps.RepoGPSCoordinates
import mvvm.model.mapBox.RepoMapBox
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.generic.instance
import room.AppEntity
import utils.NetManager

@ObsoleteCoroutinesApi
class RepoDarkSkyForecast(private val netManager: NetManager, override val kodein: Kodein) : KodeinAware, AnkoLogger {
    private val repoForecastLocation: RepoGPSCoordinates by instance()
    private val repoForecastRemoteData: RepoDarkSkyForecastRemoteData by instance()
    private val mRepoDarkSkyForecastLocalData: RepoDarkSkyForecastLocalData by instance()
    private val repoMapBox: RepoMapBox = RepoMapBox()


    private suspend fun locationDetermination() = runBlocking {
        info("TEST locationDetermination")
        val locationDetermination = async { repoForecastLocation.getLocation().receive() }
        mapBoxPlaceName(locationDetermination.await())
    }

    private suspend fun mapBoxPlaceName(gpsCoordinates: GPSCoordinates) = runBlocking {
        info("TEST mapBoxPlaceName")
        val mapBoxPlaceName = async { repoMapBox.getLocationName(gpsCoordinates) }
        hasNeedUpdate(mapBoxPlaceName.await(), gpsCoordinates)
    }

    private suspend fun hasNeedUpdate(placeName: String, gpsCoordinates: GPSCoordinates) = runBlocking {
        info("TEST hasNeedUpdate $placeName")
        if (mRepoDarkSkyForecastLocalData.checkNeedUpdate(placeName)) {
            save(placeName, gpsCoordinates)
        } else
            info("TEST hasNeedUpdate $placeName FALSE")
            false
    }

    private suspend fun save(placeName: String, gpsCoordinates: GPSCoordinates) = runBlocking {
        info("TEST save")
        mRepoDarkSkyForecastLocalData.saveForecastInDb(placeName, repoForecastRemoteData.forecastRemote(gpsCoordinates))
        mRepoDarkSkyForecastLocalData.saveCityQuery(placeName)
        info("TEST save END")
    }


    suspend fun loadForecast() = runBlocking {
        if (netManager.isConnectedToInternet!!) {
            locationDetermination()
        }
    }

    suspend fun db(): Flowable<AppEntity> =
        mRepoDarkSkyForecastLocalData.loadLocalForecast(mRepoDarkSkyForecastLocalData.getCity())
}