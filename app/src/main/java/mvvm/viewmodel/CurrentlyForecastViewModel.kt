package mvvm.viewmodel

import androidx.lifecycle.ViewModel
import com.mapbox.api.geocoding.v5.models.CarmenFeature
import com.mapbox.mapboxsdk.plugins.places.autocomplete.ui.PlaceSelectionListener
import com.wimank.mvvm.weather.R
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.consumeEach
import mvvm.binding.ObservableFields
import mvvm.binding.recycler.HourlyAdapter
import mvvm.model.RepoPreference
import mvvm.model.dark_sky.RepoDarkSkyForecast
import mvvm.model.dark_sky.WeatherIcons
import mvvm.model.status.Status
import mvvm.model.status.StatusChannel
import mvvm.model.status.getStatusDescription
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.error
import utils.GPS_KEY
import utils.PLACE_KEY
import utils.SEARCH_QUERY


@ObsoleteCoroutinesApi
@ExperimentalCoroutinesApi
class CurrentlyForecastViewModel(
    private val mRepoForecast: RepoDarkSkyForecast,
    private val handler: CoroutineExceptionHandler,
    private val observableFields: ObservableFields,
    private val preference: RepoPreference,
    private val statusChannel: StatusChannel
) : ViewModel(), AnkoLogger, PlaceSelectionListener {

    private val job = SupervisorJob()
    private val scope = CoroutineScope(Dispatchers.Default + job)

    init {
        refresh()
    }

    fun refresh() = scope.launch(handler) {
        try {
            statusChannel()
            when {
                preference.getBooleanSettings(PLACE_KEY) -> {
                    if (preference.getStringsSettings(SEARCH_QUERY).isNotEmpty())
                        loadPlaceNameForecast(preference.getStringsSettings(SEARCH_QUERY))
                }

                preference.getBooleanSettings(GPS_KEY) -> loadGPSForecast()

                else -> {
                    preference.saveSettings(GPS_KEY, true)
                    loadGPSForecast()
                }
            }
            dataBaseAsync()
        } catch (e: Exception) {
            error { e.printStackTrace() }
            observableFields.status.set(R.string.error_forecast)
            observableFields.isLoading.set(false)
        }
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
            observableFields.status.set(getStatusDescription(it))
            when (it) {
                Status.DONE -> observableFields.statusInvisible.set(true)
                Status.DATA_UP_TO_DATE -> observableFields.statusInvisible.set(true)
                else -> observableFields.statusInvisible.set(false)
            }
        }
    }

    private suspend fun dataBaseAsync() = scope.launch {
        val forecastDB = async { mRepoForecast.db() }
        withContext(Dispatchers.Main) {
            observableFields.temp.set(forecastDB.await().temperature)
            observableFields.summary.set(forecastDB.await().summary)
            observableFields.toolbarTitle.value = forecastDB.await().city
            observableFields.weatherIcon.set(WeatherIcons().map().getValue(forecastDB.await().icon))
            observableFields.hourlyAdapter.set(HourlyAdapter(forecastDB.await().jsonHourlyArray))
        }
    }

    override fun onCancel() {
        observableFields.cancelPlaceSearch.value = true
    }

    fun gps() {
        preference.saveSettings(PLACE_KEY, false)
        preference.saveSettings(GPS_KEY, true)
        refresh()
    }

    override fun onPlaceSelected(carmenFeature: CarmenFeature?) {
        observableFields.cancelPlaceSearch.value = false
        if (carmenFeature == null)
            return
        if (carmenFeature.center() != null) {
            preference.saveSettings(PLACE_KEY, true)
            preference.saveSettings(GPS_KEY, false)
            preference.saveSettings(SEARCH_QUERY, carmenFeature.text() ?: "")
            refresh()
        } else {
            observableFields.status.set(R.string.We_could_not_find)
            error { "CarmenFeature center's null." }
        }
    }

    override fun onCleared() {
        super.onCleared()
        observableFields.isLoading.set(false)
        job.cancel()
    }
}