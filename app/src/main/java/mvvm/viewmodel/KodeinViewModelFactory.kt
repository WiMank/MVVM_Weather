package mvvm.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.ObsoleteCoroutinesApi
import mvvm.model.dark_sky.ObservableFields
import mvvm.model.dark_sky.RepoDarkSkyForecast


@ExperimentalCoroutinesApi
@ObsoleteCoroutinesApi
class KodeinViewModelFactory(
    private val mRepoForecast: RepoDarkSkyForecast,
    private val handler: CoroutineExceptionHandler,
    private val observableFields: ObservableFields
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return CurrentlyForecastViewModel(mRepoForecast, handler, observableFields) as T
    }
}