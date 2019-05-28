package mvvm.viewmodel

import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wimank.mvvm.weather.R
import io.ktor.client.features.ClientRequestException
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
    val icon = ObservableInt(R.drawable.ic_launcher_foreground)
    val isLoading = ObservableBoolean(false)

    fun refresh() {
        viewModelScope.launch {
            this.launch(Dispatchers.Default) {
                isLoading.set(true)
                try {
                    info { "GOOOOOOO!!!" }
                    val go = async { mRepoForecast.loadForecast() }
                    isLoading.set(false)
                    info { "COMPLETE!!!" }
                } catch (e: ClientRequestException) {
                    temp.set("${e.response.status.description}: ${e.response.status.value}")
                } catch (e: IllegalStateException) {
                    temp.set("${e.message}")
                } finally {
                    isLoading.set(false)
                }
            }
        }
    }
}