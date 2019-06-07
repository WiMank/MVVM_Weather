package di.module

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.ObsoleteCoroutinesApi
import mvvm.viewmodel.CurrentlyForecastViewModel
import mvvm.viewmodel.KodeinViewModelFactory
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider

@ExperimentalCoroutinesApi
@ObsoleteCoroutinesApi
class ViewModelModule {

    val viewModelModule = Kodein.Module("viewmodel_module") {
        bind() from provider {
            CurrentlyForecastViewModel(
                instance(),
                instance(),
                instance(),
                instance(),
                instance()
            )
        }
        bind() from provider {
            KodeinViewModelFactory(
                instance(),
                instance(),
                instance(),
                instance(),
                instance()
            )
        }
    }
}