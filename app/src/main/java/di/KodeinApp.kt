package di

import android.app.Application
import di.module.RetrofitKodeinModule
import di.module.RoomKodeinModule
import mvvm.model.RepoForecastModel
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton

class KodeinApp : Application(), KodeinAware {

    override val kodein by Kodein.lazy {
        import(androidXModule(this@KodeinApp))
        import(RetrofitKodeinModule().retrofitModule)
        import(RoomKodeinModule().roomModule)

        bind() from singleton { RepoForecastModel(instance()) }
    }

    override fun onCreate() {
        super.onCreate()
        val k = kodein
        println(k)
    }
}