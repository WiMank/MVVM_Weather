package di.module

import mvvm.model.RepoForecast
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton

class RepoModule {

    val repoModule = Kodein.Module("repo_mdule") {
        bind<RepoForecast>() with singleton { RepoForecast(instance()) }
    }
}