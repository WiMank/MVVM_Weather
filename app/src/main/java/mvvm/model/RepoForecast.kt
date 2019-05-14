package mvvm.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.jetbrains.anko.AnkoLogger
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.generic.instance
import rest.pojo.DarkSkyPojo
import utils.NetManager

class RepoForecast(private val netManager: NetManager, override val kodein: Kodein) : KodeinAware, AnkoLogger {

    private val job = SupervisorJob()
    private val scope = CoroutineScope(Dispatchers.Default + job)

    private val repoForecastLocation: RepoForecastLocation by instance()
    private val repoForecastRemoteData: RepoForecastRemoteData by instance()


    fun forecast(): LiveData<DarkSkyPojo.DarkSky> {
        val lData = MutableLiveData<DarkSkyPojo.DarkSky>()
        if (netManager.isConnectedToInternet!!) {

        } else {

        }
        return lData
    }
}