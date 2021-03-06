package mvvm.model.status

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.withContext

@ExperimentalCoroutinesApi
class StatusChannel {

    val mChannel: ConflatedBroadcastChannel<Status> = ConflatedBroadcastChannel()

    suspend fun sendStatus(status: Status) = withContext(Dispatchers.Default) {
        mChannel.send(status)
    }
}
