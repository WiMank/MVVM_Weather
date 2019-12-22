package mvvm.viewmodel

import androidx.lifecycle.ViewModel
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
class CurrentlyWeatherViewModel(
    private val mRepoForecast: RepoDarkSkyForecast,
    private val mHandler: CoroutineExceptionHandler,
    private val mObservableFields: ObservableFields,
    private val mPreference: RepoPreference,
    private val mStatusChannel: StatusChannel
) : ViewModel(), AnkoLogger {

    private val mJob = SupervisorJob()
    private val mScope = CoroutineScope(Dispatchers.Default + mJob)

    init {
        refresh()
    }

    fun refresh() = mScope.launch(mHandler) {
        try {
            statusChannel()
            when {
                mPreference.getBooleanSettings(PLACE_KEY) -> {
                    if (mPreference.getStringsSettings(SEARCH_QUERY).isNotEmpty())
                        loadPlaceNameForecast(mPreference.getStringsSettings(SEARCH_QUERY))
                }

                mPreference.getBooleanSettings(GPS_KEY) -> loadGPSForecast()

                else -> {
                    mPreference.saveSettings(GPS_KEY, true)
                    loadGPSForecast()
                }
            }
            dataBaseAsync()
        } catch (e: Exception) {
            error { e.printStackTrace() }
            mObservableFields.status.set(R.string.error_forecast)
            mObservableFields.isLoading.set(false)
        }
    }

    private suspend fun loadGPSForecast() {
        mObservableFields.isLoading.set(true)
        mRepoForecast.loadGPSForecast()
        mObservableFields.isLoading.set(false)
    }

    private suspend fun loadPlaceNameForecast(query: String) {
        mObservableFields.isLoading.set(true)
        mRepoForecast.loadPlaceNameCoordinates(query)
        mObservableFields.isLoading.set(false)
    }

    private suspend fun statusChannel() = mScope.launch {
        mStatusChannel.mChannel.consumeEach {
            mObservableFields.status.set(getStatusDescription(it))
            when (it) {
                Status.DONE -> mObservableFields.statusInvisible.set(true)
                Status.DATA_UP_TO_DATE -> mObservableFields.statusInvisible.set(true)
                else -> mObservableFields.statusInvisible.set(false)
            }
        }
    }

    private suspend fun dataBaseAsync() = mScope.launch {
        val forecastDB = async { mRepoForecast.db() }
        withContext(Dispatchers.Main) {
            mObservableFields.temp.set(forecastDB.await().temperature)
            mObservableFields.summary.set(forecastDB.await().summary)
            mObservableFields.toolbarTitle.value = forecastDB.await().city
            mObservableFields.weatherIcon.set(WeatherIcons().map().getValue(forecastDB.await().icon))
            mObservableFields.hourlyAdapter.set(HourlyAdapter(forecastDB.await().jsonHourlyArray))
        }
    }

    fun gps() {
        mPreference.saveSettings(PLACE_KEY, false)
        mPreference.saveSettings(GPS_KEY, true)
        refresh()
    }

    override fun onCleared() {
        super.onCleared()
        mObservableFields.isLoading.set(false)
        mJob.cancel()
    }

}
