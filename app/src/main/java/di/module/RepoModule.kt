package di.module

import mvvm.model.RepoGPSCoordinates
import mvvm.model.dark_sky.RepoDarkSkyForecast
import mvvm.model.dark_sky.RepoDarkSkyForecastRemoteData
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton


class RepoModule {

    val repoModule = Kodein.Module("repo_mdule") {
        bind<RepoDarkSkyForecast>() with singleton {
            RepoDarkSkyForecast(
                instance(),
                kodein
            )
        }
        bind<RepoGPSCoordinates>() with singleton { RepoGPSCoordinates(instance()) }
        bind<RepoDarkSkyForecastRemoteData>() with singleton {
            RepoDarkSkyForecastRemoteData(
                instance()
            )
        }
    }
}