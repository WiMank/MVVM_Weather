package di

import android.app.Application
import androidx.lifecycle.ViewModelProvider
import di.module.RetrofitKodeinModule
import di.module.RoomKodeinModule
import mvvm.model.RepoForecastModel
import mvvm.viewmodel.CurrentlyForecastViewModel
import mvvm.viewmodel.KodeinViewModelFactory
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

        bind<CurrentlyForecastViewModel>() with singleton { CurrentlyForecastViewModel(kodein) }

        bind<ViewModelProvider.Factory>() with singleton { KodeinViewModelFactory(kodein) }

    }

    override fun onCreate() {
        super.onCreate()
        val k = kodein
        println(k)
    }
}