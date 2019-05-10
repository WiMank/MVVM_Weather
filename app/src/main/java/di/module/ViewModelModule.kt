package di.module

import androidx.lifecycle.ViewModelProvider
import mvvm.viewmodel.CurrentlyForecastViewModel
import mvvm.viewmodel.KodeinViewModelFactory
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.singleton

class ViewModelModule {

    val viewModelModule = Kodein.Module("viewmodel_module") {
        bind<CurrentlyForecastViewModel>() with singleton { CurrentlyForecastViewModel(kodein) }
        bind<ViewModelProvider.Factory>() with singleton { KodeinViewModelFactory(kodein) }
    }
}