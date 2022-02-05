package kozyriatskyi.anton.sked.util

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager

class ConnectionStateReceiver(private val context: Context) : BroadcastReceiver() {

    fun interface OnConnectionStateChangeListener {
        fun onConnectionStateChanged(isConnectionAvailable: Boolean)
    }

    var onConnectionStateChangeListener: OnConnectionStateChangeListener? = null

    override fun onReceive(context: Context, intent: Intent) {
        onConnectionStateChangeListener?.onConnectionStateChanged(isNetworkAvailable(context.applicationContext))
    }

    fun register() {
        context.registerReceiver(this, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
    }

    fun unregister() {
        context.unregisterReceiver(this)
    }

    private fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }
}
