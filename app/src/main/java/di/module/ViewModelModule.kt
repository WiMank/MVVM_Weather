package di.module

import di.fragmentScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.ObsoleteCoroutinesApi
import mvvm.viewmodel.CurrentlyForecastViewModel
import mvvm.viewmodel.KodeinViewModelFactory
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.scoped
import org.kodein.di.generic.singleton

@ExperimentalCoroutinesApi
@ObsoleteCoroutinesApi
class ViewModelModule {

    val viewModelModule = Kodein.Module("viewmodel_module") {
        bind() from scoped(fragmentScope).singleton { CurrentlyForecastViewModel(instance(), instance()) }
        bind() from scoped(fragmentScope).singleton { KodeinViewModelFactory(instance(), instance()) }
    }
}