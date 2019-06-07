package di

import android.app.Application
import di.module.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.ObsoleteCoroutinesApi
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule

@ExperimentalCoroutinesApi
@ObsoleteCoroutinesApi
@Suppress("unused")
class KodeinApp : Application(), KodeinAware {

    override val kodein by Kodein.lazy {
        import(androidXModule(this@KodeinApp))
        import(RoomModule().roomModule)
        import(ViewModelModule().viewModelModule)
        import(RepoModule().repoModule)
        import(KtorModule().ktorClientModule)
        import(UtilsModule().utilsModule)
    }
}