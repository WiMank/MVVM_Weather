package mvvm.viewmodel

import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.consumeEach
import mvvm.model.dark_sky.ObservableFields
import mvvm.model.dark_sky.RepoDarkSkyForecast
import mvvm.model.status.Status
import mvvm.model.status.StatusChannel
import org.jetbrains.anko.AnkoLogger
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
    private val settings: Settings
) : ViewModel(), AnkoLogger {

    private val job = SupervisorJob()
    private val scope = CoroutineScope(Dispatchers.Default + job)
    private val composite = CompositeDisposable()

    init {
        refresh()
    }


    fun refresh() = scope.launch(handler) {
        info { "GPS: [${settings.getBooleanSettings(GPS_KEY)}] PLACE: [${settings.getBooleanSettings(PLACE_KEY)}]" }
        when {
            settings.getBooleanSettings(PLACE_KEY) -> {
                if (settings.getStringsSettings(SEARCH_QUERY).isNotEmpty()) {
                    observableFields.gpsEnabled.set(false)
                    loadPlaceNameForecast(settings.getStringsSettings(SEARCH_QUERY))
                }
            }
            settings.getBooleanSettings(GPS_KEY) -> {
                observableFields.gpsEnabled.set(true)
                loadGPSForecast()
            }

            else -> {
                observableFields.gpsEnabled.set(true)
                settings.saveSettings(GPS_KEY, true)
                loadGPSForecast()
            }
        }
        initStatusAndDb()
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

    private suspend fun initStatusAndDb() {
        statusChannel()
        dataBaseObserve()
    }

    private suspend fun statusChannel() = scope.launch {
        StatusChannel.channel.consumeEach {
            when (it) {
                Status.LOCATION_DETERMINATION -> observableFields.status.set("Определяем местоположение...")
                Status.LOOKING_FOR_LOCATION_NAME -> observableFields.status.set("Пытаемся найти название местоположения...")
                Status.UPDATE_NEEDED -> observableFields.status.set("Проверяем актуальность данных...")
                Status.DATA_UP_TO_DATE -> observableFields.status.set("Данные в актуальном состоянии!")
                Status.SAVE_THE_DATA -> observableFields.status.set("Сохраняем новые данные...")
                Status.DONE -> observableFields.status.set("Готово!")
                Status.NO_NETWORK_CONNECTION -> observableFields.status.set("Нет подключения к сети!")
                Status.PLACE_COORDINATES -> observableFields.status.set("Узнаём координаты места...")
            }
        }
    }

    private suspend fun dataBaseObserve() {
        composite.add(
            mRepoForecast.db()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    observableFields.city.set(it.city)
                    observableFields.temp.set(it.temperature.toString())
                })
    }

    val onQueryTextListener = object : SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(query: String?): Boolean {
            settings.saveSettings(PLACE_KEY, true)
            settings.saveSettings(GPS_KEY, false)
            settings.saveSettings(SEARCH_QUERY, query ?: "")
            refresh()
            if (observableFields.collapseSearchView.get())
                observableFields.collapseSearchView.set(false)
            else
                observableFields.collapseSearchView.set(true)
            return false
        }

        override fun onQueryTextChange(newText: String?): Boolean {
            return false
        }
    }

    fun gps() {
        settings.saveSettings(PLACE_KEY, false)
        settings.saveSettings(GPS_KEY, true)
        refresh()
    }

    override fun onCleared() {
        super.onCleared()
        StatusChannel.channel.close()
        composite.dispose()
        job.cancel()
    }
}