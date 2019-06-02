package di.module

import kotlinx.coroutines.ObsoleteCoroutinesApi
import mvvm.viewmodel.CurrentlyForecastViewModel
import mvvm.viewmodel.KodeinViewModelFactory
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.singleton

@ObsoleteCoroutinesApi
class ViewModelModule {

    val viewModelModule = Kodein.Module("viewmodel_module") {
        bind() from singleton { CurrentlyForecastViewModel(kodein) }
        bind() from singleton { KodeinViewModelFactory(kodein) }
    }
}