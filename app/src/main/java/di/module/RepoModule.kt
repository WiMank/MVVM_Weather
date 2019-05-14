package di.module

import mvvm.model.RepoForecast
import mvvm.model.RepoForecastLocation
import mvvm.model.RepoForecastRemoteData
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton


class RepoModule {

    val repoModule = Kodein.Module("repo_mdule") {
        bind<RepoForecast>() with singleton { RepoForecast(instance(), kodein) }
        bind<RepoForecastLocation>() with singleton { RepoForecastLocation(instance()) }
        bind<RepoForecastRemoteData>() with singleton { RepoForecastRemoteData(instance(), instance()) }
    }
}