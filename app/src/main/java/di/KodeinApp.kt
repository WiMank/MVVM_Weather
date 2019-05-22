package di

import android.app.Application
import di.module.*
import kotlinx.coroutines.CoroutineExceptionHandler
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton
import utils.NetManager

@Suppress("unused")
class KodeinApp : Application(), KodeinAware, AnkoLogger {

    override val kodein by Kodein.lazy {
        import(androidXModule(this@KodeinApp))
        import(RetrofitKodeinModule().retrofitModule)
        import(RoomKodeinModule().roomModule)
        import(ViewModelModule().viewModelModule)
        import(RepoModule().repoModule)
        import(KtorModule().ktorClientModule)

        bind<NetManager>() with singleton { NetManager(instance()) }

        bind<CoroutineExceptionHandler>() with singleton {
            CoroutineExceptionHandler { _, exception ->
                info("Caught $exception")
            }
        }
    }

    override fun onCreate() {
        super.onCreate()
        val k = kodein
        println(k)
    }
}