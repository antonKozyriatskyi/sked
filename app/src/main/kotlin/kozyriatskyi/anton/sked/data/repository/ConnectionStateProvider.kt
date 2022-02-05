package kozyriatskyi.anton.sked.data.repository

import android.content.Context
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kozyriatskyi.anton.sked.util.ConnectionStateReceiver
import kozyriatskyi.anton.sked.util.ConnectionStateReceiver.OnConnectionStateChangeListener

class ConnectionStateProvider(context: Context) {

    private val connectionReceiver = ConnectionStateReceiver(context)

    @OptIn(ExperimentalCoroutinesApi::class)
    fun connectionStateChanges(): Flow<Boolean> = callbackFlow {
        val listener = OnConnectionStateChangeListener(::trySend)
        connectionReceiver.onConnectionStateChangeListener = listener
        connectionReceiver.register()

        awaitClose {
            connectionReceiver.unregister()
        }
    }
}