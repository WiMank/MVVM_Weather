package mvvm.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import org.jetbrains.anko.AnkoLogger
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.generic.instance
import rest.pojo.DarkSkyPojo
import utils.NetManager

class RepoForecast(private val netManager: NetManager, override val kodein: Kodein) : KodeinAware, AnkoLogger {

    private val repoForecastLocation: RepoForecastLocation by instance()
    private val repoForecastRemoteData: RepoForecastRemoteData by instance()

     fun forecast(): LiveData<DarkSkyPojo.DarkSky> {
        val lData = MutableLiveData<DarkSkyPojo.DarkSky>()
        if (netManager.isConnectedToInternet!!) {
            repoForecastRemoteData.forecastRemote(repoForecastLocation.location())
        } else {

        }
        return lData
    }
}