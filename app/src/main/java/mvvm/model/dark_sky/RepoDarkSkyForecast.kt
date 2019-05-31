package mvvm.model.dark_sky

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.withContext
import mvvm.model.gps.RepoGPSCoordinates
import mvvm.model.mapBox.RepoMapBox
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
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
    private var appEntity = AppEntity()


    suspend fun loadForecast() = withContext(Dispatchers.Default) {
        if (netManager.isConnectedToInternet!!) {
            repoForecastLocation.getLocation().consumeEach {
                info { "CHANNELSSSSSS latitude: ${it.latitude}  longitude: ${it.longitude}" }
                val lCityName = async { repoMapBox.getLocationName(it) }
                info { "CHANNELSSSSSS city: ${lCityName.await()}" }
                mRepoDarkSkyForecastLocalData.saveCityQuery(lCityName.await())

                val forecastRemote = async { repoForecastRemoteData.forecastRemote(it) }

                mRepoDarkSkyForecastLocalData.saveForecastInDb(lCityName.await(), forecastRemote.await())

                mRepoDarkSkyForecastLocalData.loadLocalForecast(lCityName.await())

            }


            val result = mRepoDarkSkyForecastLocalData.loadLocalForecast("")
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                    onNext = { println(it) },
                    onError = { it.printStackTrace() },
                    onComplete = { println("Done!") }
                )

        }
        /// appEntity
    }

}