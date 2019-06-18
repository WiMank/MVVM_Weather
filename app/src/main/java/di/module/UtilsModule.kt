package di.module

import androidx.fragment.app.Fragment
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.ExperimentalCoroutinesApi
import mvvm.binding.ObservableFields
import mvvm.model.RepoPreference
import mvvm.model.status.StatusChannel
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.kodein.di.Kodein
import org.kodein.di.bindings.WeakContextScope
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.scoped
import org.kodein.di.generic.singleton
import utils.NetManager

@ExperimentalCoroutinesApi
class UtilsModule : AnkoLogger {

    val utilsModule = Kodein.Module("utils_module") {

        bind() from singleton { NetManager(instance()) }

        bind() from singleton { ObservableFields() }

        bind() from singleton { RepoPreference(instance()) }

        bind() from scoped(WeakContextScope<Fragment>()).singleton { StatusChannel() }

        bind() from singleton {
            CoroutineExceptionHandler { _, exception ->
                info("Caught $exception")
            }
        }
    }
}