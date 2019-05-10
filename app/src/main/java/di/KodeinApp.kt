package di

import android.app.Application
import di.module.RepoModule
import di.module.RetrofitKodeinModule
import di.module.RoomKodeinModule
import di.module.ViewModelModule
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton
import utils.NetManager

@Suppress("unused")
class KodeinApp : Application(), KodeinAware {

    override val kodein by Kodein.lazy {
        import(androidXModule(this@KodeinApp))
        import(RetrofitKodeinModule().retrofitModule)
        import(RoomKodeinModule().roomModule)
        import(ViewModelModule().viewModelModule)
        import(RepoModule().repoModule)

        bind<NetManager>() with singleton { NetManager(instance()) }
    }

    override fun onCreate() {
        super.onCreate()
        val k = kodein
        println(k)
    }
}