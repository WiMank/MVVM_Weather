package di.module

import mvvm.model.RepoForecastModel
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton

class RepoModule {

    val repoModule = Kodein.Module("repo_mdule") {
        bind<RepoForecastModel>() with singleton { RepoForecastModel(instance()) }
    }
}