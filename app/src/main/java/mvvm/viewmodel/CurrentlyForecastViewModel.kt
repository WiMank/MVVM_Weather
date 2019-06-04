package mvvm.viewmodel

import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt
import androidx.lifecycle.ViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.consumeEach
import mvvm.model.dark_sky.RepoDarkSkyForecast
import mvvm.model.status.Status
import mvvm.model.status.StatusChannel
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info


@ObsoleteCoroutinesApi
@ExperimentalCoroutinesApi
class CurrentlyForecastViewModel(
    private val mRepoForecast: RepoDarkSkyForecast,
    private val handler: CoroutineExceptionHandler
) : ViewModel(), AnkoLogger {

    private val job = SupervisorJob()
    private val scope = CoroutineScope(Dispatchers.Default + job)
    private val composite = CompositeDisposable()
    val city = ObservableField<String>("CITY")
    val temp = ObservableField<String>("TEMP")
    val status = ObservableField<String>("...")
    val icon = ObservableInt(0)
    val isLoading = ObservableBoolean(false)

    init {
        refresh()
    }

    fun refresh() {
        info { "TEWRTERTERT" }
        scope.launch(handler) {
            //  statusChannel()
            //   loadForecast()
            // dataBaseObserve()
        }
    }

    private suspend fun loadForecast() {
        isLoading.set(true)
        mRepoForecast.loadGPSForecast()
        isLoading.set(false)
    }

    private suspend fun loadPlaceNameForecast() {
        isLoading.set(true)
        mRepoForecast.loadGPSForecast()
        isLoading.set(false)
    }

    private suspend fun statusChannel() = scope.launch {
        StatusChannel.channel.consumeEach {
            when (it) {
                Status.LOCATION_DETERMINATION -> status.set("Определяем местоположение...")
                Status.LOOKING_FOR_LOCATION_NAME -> status.set("Пытаемся найти название местоположения...")
                Status.UPDATE_NEEDED -> status.set("Проверяем актуальность данных...")
                Status.DATA_UP_TO_DATE -> status.set("Данные в актуальном состоянии!")
                Status.SAVE_THE_DATA -> status.set("Сохраняем новые данные...")
                Status.DONE -> status.set("Готово!")
                Status.NO_NETWORK_CONNECTION -> status.set("Нет подключения к сети!")
            }
        }
    }

    private suspend fun dataBaseObserve() {
        composite.add(
            mRepoForecast.db()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    city.set(it.city)
                    temp.set(it.temperature.toString())
                })
    }


    override fun onCleared() {
        super.onCleared()
        StatusChannel.channel.close()
        composite.dispose()
        job.cancel()
    }
}