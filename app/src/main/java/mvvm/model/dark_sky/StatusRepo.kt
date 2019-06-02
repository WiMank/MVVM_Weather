package mvvm.model.dark_sky

import io.reactivex.subjects.BehaviorSubject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import utils.Status

object StatusRepo : AnkoLogger {

    val channel: Channel<Status> = Channel()

    val rxChannel: BehaviorSubject<Status> = BehaviorSubject.create()

    suspend fun setStatus(status: Status) = withContext(Dispatchers.Default) {
        info("$status")
        this.launch { channel.offer(status) }
    }

    fun setStatusRx(status: Status) {
        info("$status")
        rxChannel.onNext(status)
    }
}