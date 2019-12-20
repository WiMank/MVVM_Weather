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
class KodeinViewModelFactory(
    private val mRepoForecast: RepoDarkSkyForecast,
    private val handler: CoroutineExceptionHandler,
    private val observableFields: ObservableFields,
    private val preference: RepoPreference,
    private val statusChannel: StatusChannel
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return CurrentlyWeatherViewModel(
            mRepoForecast,
            handler,
            observableFields,
            preference,
            statusChannel
        ) as T
    }
}
