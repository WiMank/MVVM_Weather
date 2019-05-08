package di

import android.app.Application
import di.module.RetrofitKodeinModule
import di.module.RoomKodeinModule
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule

class KodeinApp : Application(), KodeinAware {

    override val kodein by Kodein.lazy {
        import(androidXModule(this@KodeinApp))
        import(RetrofitKodeinModule().retrofitModule)
        import(RoomKodeinModule().roomModule)
    }

    override fun onCreate() {
        super.onCreate()
        val k = kodein
        println(k)
    }
}