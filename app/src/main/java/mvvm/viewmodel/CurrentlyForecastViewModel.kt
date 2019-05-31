package mvvm.viewmodel

import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import mvvm.model.dark_sky.RepoDarkSkyForecast
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.kodein.di.Kodein
import org.kodein.di.generic.instance


class CurrentlyForecastViewModel(val kodein: Kodein) : ViewModel(), AnkoLogger {
    private val mRepoForecast: RepoDarkSkyForecast by kodein.instance()
    private val handler: CoroutineExceptionHandler by kodein.instance()

    val city = ObservableField<String>("CITY")
    val temp = ObservableField<String>("TEMP")
    val icon = ObservableInt(0)
    val isLoading = ObservableBoolean(false)

    fun refresh() {
        info { "GOG!!!" }
        viewModelScope.launch {
            this.launch(Dispatchers.Default) {
                isLoading.set(true)
                    info { "GOOOOOOO!!!" }
                val result = async { mRepoForecast.loadForecast() }
                //  city.set(result.await().city)
                // temp.set(result.await().temperature.toString())
                    isLoading.set(false)
                    info { "COMPLETE!!!" }
            }
        }
    }
}