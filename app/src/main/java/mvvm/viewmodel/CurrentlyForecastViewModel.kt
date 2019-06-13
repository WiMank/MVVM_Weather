package mvvm.viewmodel

import androidx.lifecycle.ViewModel
import com.mapbox.api.geocoding.v5.models.CarmenFeature
import com.mapbox.mapboxsdk.plugins.places.autocomplete.ui.PlaceSelectionListener
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.consumeEach
import mvvm.model.dark_sky.ObservableFields
import mvvm.model.dark_sky.RepoDarkSkyForecast
import mvvm.model.dark_sky.WeatherIcons
import mvvm.model.status.Status
import mvvm.model.status.StatusChannel
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.error
import org.jetbrains.anko.info
import utils.GPS_KEY
import utils.PLACE_KEY
import utils.SEARCH_QUERY
import utils.Settings


@ObsoleteCoroutinesApi
@ExperimentalCoroutinesApi
class CurrentlyForecastViewModel(
    private val mRepoForecast: RepoDarkSkyForecast,
    private val handler: CoroutineExceptionHandler,
    private val observableFields: ObservableFields,
    private val settings: Settings,
    private val statusChannel: StatusChannel
) : ViewModel(), AnkoLogger, PlaceSelectionListener {

    private val job = SupervisorJob()
    private val scope = CoroutineScope(Dispatchers.Default + job)
    private val composite = CompositeDisposable()

    init {
        refresh()
    }

    fun refresh() = scope.launch {
        statusChannel()
        when {
            settings.getBooleanSettings(PLACE_KEY) -> {
                if (settings.getStringsSettings(SEARCH_QUERY).isNotEmpty())
                    loadPlaceNameForecast(settings.getStringsSettings(SEARCH_QUERY))
            }

            settings.getBooleanSettings(GPS_KEY) -> loadGPSForecast()

            else -> {
                settings.saveSettings(GPS_KEY, true)
                loadGPSForecast()
            }
        }
        dataBaseObserve()
    }

    private suspend fun loadGPSForecast() {
        observableFields.isLoading.set(true)
        mRepoForecast.loadGPSForecast()
        observableFields.isLoading.set(false)

    }

    private suspend fun loadPlaceNameForecast(query: String) {
        observableFields.isLoading.set(true)
        mRepoForecast.loadPlaceNameCoordinates(query)
        observableFields.isLoading.set(false)
    }

    private suspend fun statusChannel() = scope.launch {
        statusChannel.channel.consumeEach {
            when (it) {
                Status.LOCATION_DETERMINATION -> {
                    observableFields.status.set("Определяем местоположение...")
                    observableFields.statusInvisible.set(false)
                }
                Status.LOOKING_FOR_LOCATION_NAME -> {
                    observableFields.status.set("Пытаемся найти название местоположения...")
                    observableFields.statusInvisible.set(false)
                }
                Status.UPDATE_NEEDED -> {
                    observableFields.status.set("Проверяем актуальность данных...")
                    observableFields.statusInvisible.set(false)
                }

                Status.SAVE_THE_DATA -> {
                    observableFields.status.set("Сохраняем новые данные...")
                    observableFields.statusInvisible.set(false)
                }

                Status.PLACE_COORDINATES -> {
                    observableFields.status.set("Узнаём координаты места...")
                    observableFields.statusInvisible.set(false)
                }

                Status.NO_NETWORK_CONNECTION -> {
                    observableFields.status.set("Нет подключения к сети!")
                    observableFields.statusInvisible.set(false)
                }

                Status.DONE -> {
                    observableFields.status.set("Готово!")
                    observableFields.statusInvisible.set(true)
                }

                Status.DATA_UP_TO_DATE -> {
                    observableFields.status.set("Данные в актуальном состоянии!")
                    observableFields.statusInvisible.set(true)
                }
            }
        }
    }

    //Room does not support coroutines channels
    private suspend fun dataBaseObserve() {
        composite.add(
            mRepoForecast.db()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    observableFields.temp.set(it.temperature)
                    observableFields.summary.set(it.summary)
                    observableFields.toolbarTitle.value = it.city
                    observableFields.weatherIcon.set(WeatherIcons().map().getValue(it.icon))
                })
    }

    override fun onCancel() {
        observableFields.cancelPlaceSearch.value = true
    }

    fun gps() {
        settings.saveSettings(PLACE_KEY, false)
        settings.saveSettings(GPS_KEY, true)
        refresh()
    }

    override fun onPlaceSelected(carmenFeature: CarmenFeature?) {
        observableFields.cancelPlaceSearch.value = false
        if (carmenFeature == null)
            return
        if (carmenFeature.center() != null) {
            settings.saveSettings(PLACE_KEY, true)
            settings.saveSettings(GPS_KEY, false)
            settings.saveSettings(SEARCH_QUERY, carmenFeature.text() ?: "")
            refresh()
        } else {
            observableFields.status.set("Мы не смогли найти указанное место :(")
            error { "CarmenFeature center's null." }
        }
    }

    override fun onCleared() {
        info { "VM onCleared() ${observableFields.isLoading.get()}" }
        super.onCleared()
        observableFields.isLoading.set(false)
        //Проблема с закрытием канала
        //statusChannel.channel.close()
        composite.dispose()
        job.cancel()
    }
}