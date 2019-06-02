package mvvm.viewmodel

import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.launch
import mvvm.model.dark_sky.RepoDarkSkyForecast
import mvvm.model.status.Status
import mvvm.model.status.StatusChannel
import org.jetbrains.anko.AnkoLogger
import org.kodein.di.Kodein
import org.kodein.di.generic.instance
import room.AppEntity


@ObsoleteCoroutinesApi
@ExperimentalCoroutinesApi
class CurrentlyForecastViewModel(val kodein: Kodein) : ViewModel(), AnkoLogger {
    private val mRepoForecast: RepoDarkSkyForecast by kodein.instance()
    private val handler: CoroutineExceptionHandler by kodein.instance()
    private lateinit var flow: Flowable<AppEntity>

    val city = ObservableField<String>("CITY")
    val temp = ObservableField<String>("TEMP")
    val status = ObservableField<String>("...")
    val icon = ObservableInt(0)
    val isLoading = ObservableBoolean(false)

    init {
        refresh()
    }

    fun refresh() {
        viewModelScope.launch(handler) {
            statusChannel()
            loadForecast()
            dataBaseObserve()
        }
    }

    private suspend fun loadForecast() {
        isLoading.set(true)
        mRepoForecast.loadForecast()
        isLoading.set(false)
    }


    private suspend fun statusChannel() {
        viewModelScope.launch {
            StatusChannel.channel.consumeEach {
                when (it) {
                    Status.LOCATION_DETERMINATION -> status.set("Определяем местоположение...")
                    Status.LOOKING_FOR_LOCATION_NAME -> status.set("Пытаемся найти название местоположения...")
                    Status.UPDATE_NEEDED -> status.set("Обновляем данные...")
                    Status.DATA_UP_TO_DATE -> status.set("Данные в актуальном состоянии!")
                    Status.SAVE_THE_DATA -> status.set("Сохраняем новые данные...")
                    Status.READY -> status.set("Готово!")
                    Status.NO_NETWORK_CONNECTION -> status.set("Нет подключения к сети!")
                }
            }
        }
    }

    private suspend fun dataBaseObserve() {
        flow = mRepoForecast.db().apply {
            this.observeOn(AndroidSchedulers.mainThread())
            this.subscribe {
                city.set(it.city)
                temp.set(it.temperature.toString())
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        StatusChannel.channel.close()
    }
}