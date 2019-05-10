package mvvm.model

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.jetbrains.anko.AnkoLogger
import utils.NetManager

class RepoForecast(private val netManager: NetManager) : AnkoLogger {

    private val job = SupervisorJob()
    private val scope = CoroutineScope(Dispatchers.Default + job)


}