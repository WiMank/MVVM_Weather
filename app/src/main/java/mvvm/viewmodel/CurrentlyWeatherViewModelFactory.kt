package mvvm.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.ObsoleteCoroutinesApi
import mvvm.binding.ObservableFields
import mvvm.model.RepoPreference
import mvvm.model.dark_sky.RepoDarkSkyForecast
import mvvm.model.status.StatusChannel

@ExperimentalCoroutinesApi
@ObsoleteCoroutinesApi
class CurrentlyWeatherViewModelFactory(
    private val mRepoForecast: RepoDarkSkyForecast,
    private val mHandler: CoroutineExceptionHandler,
    private val mObservableFields: ObservableFields,
    private val mPreference: RepoPreference,
    private val mStatusChannel: StatusChannel
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return CurrentlyWeatherViewModel(
            mRepoForecast,
            mHandler,
            mObservableFields,
            mPreference,
            mStatusChannel
        ) as T
    }
}
