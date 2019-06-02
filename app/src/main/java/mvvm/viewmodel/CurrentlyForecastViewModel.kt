package mvvm.viewmodel

import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.consumeEach
import mvvm.model.dark_sky.RepoDarkSkyForecast
import mvvm.model.dark_sky.StatusRepo
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.kodein.di.Kodein
import org.kodein.di.generic.instance
import room.AppEntity
import utils.Status


@ObsoleteCoroutinesApi
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
        start()
    }

    fun start() {

        viewModelScope.launch(handler) {
            this.launch(Dispatchers.Default) {
                load()
                dataBaseObserve()
                //statusChannel()


                StatusRepo.rxChannel
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe {
                        info {
                            "RX $it"
                        }

                    }


            }
        }
    }

    private suspend fun load() {
        isLoading.set(true)
        mRepoForecast.loadForecast()
        isLoading.set(false)
    }

    private suspend fun statusChannel() = withContext(Dispatchers.Default) {
        this.launch {
            StatusRepo.channel.consumeEach {
                when (it) {
                    Status.LOCATION_DETERMINATION -> status.set("Определяем местоположение...")
                    Status.LOOKING_FOR_LOCATION_NAME -> status.set("Пытаемся найти название местоположения...")
                    Status.UPDATE_NEEDED -> status.set("Обновляем данные...")
                    Status.DATA_UP_TO_DATE -> status.set("Данные в актуальном состоянии...")
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
}