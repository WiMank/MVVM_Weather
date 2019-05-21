package mvvm.viewmodel

import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt
import androidx.lifecycle.ViewModel
import com.wimank.mvvm.weather.R
import io.ktor.client.features.ClientRequestException
import kotlinx.coroutines.*
import mvvm.model.RepoForecast
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.kodein.di.Kodein
import org.kodein.di.generic.instance


class CurrentlyForecastViewModel(val kodein: Kodein) : ViewModel(), AnkoLogger {
    private val job = SupervisorJob()
    private val scope = CoroutineScope(Dispatchers.Default + job)
    private val mRepoForecast: RepoForecast by kodein.instance()

    val city = ObservableField<String>("CITY")
    val temp = ObservableField<String>("TEMP")
    val icon = ObservableInt(R.drawable.ic_launcher_foreground)
    val isLoading = ObservableBoolean(false)

    fun refresh() {
        val handler = CoroutineExceptionHandler { _, exception ->
            info("Caught $exception")
        }

        scope.launch(handler) {
            isLoading.set(true)
            try {
                info { "GOOOOOOO!!!" }
                val go = async { mRepoForecast.forecastAsync() }
                temp.set(go.await()?.currently?.temperature.toString())
                isLoading.set(false)
                info { "COMPLETE!!!" }
            } catch (e: ClientRequestException) {
                temp.set("${e.response.status.description}: ${e.response.status.value}")
                isLoading.set(false)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        job.cancelChildren()
    }
}