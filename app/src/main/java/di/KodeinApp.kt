package di

import android.app.Application
import di.module.KtorModule
import di.module.RepoModule
import di.module.RoomModule
import di.module.ViewModelModule
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.ObsoleteCoroutinesApi
import mvvm.model.dark_sky.ObservableFields
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.scoped
import org.kodein.di.generic.singleton
import utils.NetManager

@ExperimentalCoroutinesApi
@ObsoleteCoroutinesApi
@Suppress("unused")
class KodeinApp : Application(), KodeinAware, AnkoLogger {

    override val kodein by Kodein.lazy {
        import(androidXModule(this@KodeinApp))
        import(RoomModule().roomModule)
        import(ViewModelModule().viewModelModule)
        import(RepoModule().repoModule)
        import(KtorModule().ktorClientModule)

        bind() from singleton { NetManager(instance()) }

        bind() from singleton {
            CoroutineExceptionHandler { _, exception ->
                info("Caught $exception")
            }
        }

        bind() from scoped(fragmentScope).singleton { ObservableFields() }
    }
}