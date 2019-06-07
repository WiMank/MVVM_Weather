package di.module

import kotlinx.coroutines.ExperimentalCoroutinesApi
import mvvm.model.dark_sky.RepoDarkSkyForecast
import mvvm.model.dark_sky.RepoDarkSkyForecastLocalData
import mvvm.model.dark_sky.RepoDarkSkyForecastRemoteData
import mvvm.model.gps.RepoGPSCoordinates
import mvvm.model.mapBox.RepoMapBox
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton


@ExperimentalCoroutinesApi
class RepoModule {

    val repoModule = Kodein.Module("repo_module") {
        bind() from singleton {
            RepoDarkSkyForecast(
                instance(),
                instance(),
                instance(),
                instance(),
                instance()
            )
        }

        bind() from singleton { RepoGPSCoordinates(instance()) }

        bind() from singleton {
            RepoDarkSkyForecastRemoteData(
                instance()
            )
        }

        bind() from singleton {
            RepoDarkSkyForecastLocalData(instance(), instance())
        }

        bind() from singleton { RepoMapBox() }
    }
}