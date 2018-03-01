package kozyriatskyi.anton.sked.data.repository

import android.content.Context
import com.jakewharton.rxrelay2.BehaviorRelay
import io.reactivex.Observable
import kozyriatskyi.anton.sked.util.ConnectionStateReceiver

class ConnectionStateProvider(context: Context) :
        ConnectionStateReceiver.OnConnectionStateChangeListener {

    private val emitter = BehaviorRelay.create<Boolean>()
    private val connectionReceiver = ConnectionStateReceiver(context)

    init {
        connectionReceiver.onConnectionStateChangeListener = this
        connectionReceiver.register()
        emitter.doOnDispose { connectionReceiver.unregister() }
    }

    fun connectionStateChanges():
            Observable<Boolean> = emitter.hide()

    override fun onConnectionStateChanged(isConnectionAvailable: Boolean) {
        emitter.accept(isConnectionAvailable)
    }
}