package di.module

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.ObsoleteCoroutinesApi
import mvvm.viewmodel.CurrentlyWeatherViewModelFactory
import mvvm.viewmodel.MainViewModelFactory
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider

@ExperimentalCoroutinesApi
@ObsoleteCoroutinesApi
class ViewModelModule {

    val viewModelModule = Kodein.Module("viewmodel_module") {

        bind() from provider {
            CurrentlyWeatherViewModelFactory(
                instance(),
                instance(),
                instance(),
                instance(),
                instance()
            )
        }

        bind() from provider {
            MainViewModelFactory(instance())
        }

    }
}
