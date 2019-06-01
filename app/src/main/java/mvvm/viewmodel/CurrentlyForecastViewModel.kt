package mvvm.viewmodel

import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.launch
import mvvm.model.dark_sky.RepoDarkSkyForecast
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.kodein.di.Kodein
import org.kodein.di.generic.instance
import room.AppEntity


@ObsoleteCoroutinesApi
class CurrentlyForecastViewModel(val kodein: Kodein) : ViewModel(), AnkoLogger {
    private val mRepoForecast: RepoDarkSkyForecast by kodein.instance()
    private val handler: CoroutineExceptionHandler by kodein.instance()
    private lateinit var flow: Flowable<AppEntity>

    val city = ObservableField<String>("CITY")
    val temp = ObservableField<String>("TEMP")
    val icon = ObservableInt(0)
    val isLoading = ObservableBoolean(false)

    init {
        start()
    }


    fun start() {
        viewModelScope.launch {
            this.launch(Dispatchers.Default) {
                load()
                dbObserve()
            }
        }
    }

    private suspend fun load() {
        isLoading.set(true)
        info { "GOOOOOOO!!!" }


        val status = mRepoForecast.loadForecast()


        isLoading.set(false)
        info { "COMPLETE! $status" }
    }


    private suspend fun dbObserve() {
        flow = mRepoForecast.db().apply {
            this.observeOn(AndroidSchedulers.mainThread())
            this.subscribe {
                city.set(it.city)
                temp.set(it.temperature.toString())
                info { "RXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXR" }
            }
        }
    }
}