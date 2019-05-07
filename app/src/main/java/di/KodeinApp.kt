package di

import android.app.Application
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.generic.bind
import org.kodein.di.generic.provider

class KodeinApp : Application(), KodeinAware {

    override val kodein by Kodein.lazy {

        bind<Application>() with provider { KodeinApp() }

    }
}