package di.module

import mvvm.model.dark_sky.RepoDarkSkyForecast
import mvvm.model.dark_sky.RepoDarkSkyForecastLocalData
import mvvm.model.dark_sky.RepoDarkSkyForecastRemoteData
import mvvm.model.gps.RepoGPSCoordinates
import mvvm.model.mapBox.RepoMapBox
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

        bind<RepoMapBox>() with singleton {
            RepoMapBox(instance())
        }


        bind<RepoDarkSkyForecastLocalData>() with singleton {
            RepoDarkSkyForecastLocalData(instance())
        }
    }
}