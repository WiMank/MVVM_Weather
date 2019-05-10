package utils

import android.content.Context
import android.net.ConnectivityManager

class NetManager(private var context: Context) {
    val isConnectedToInternet: Boolean?
        get() {
            val conManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val ni = conManager.activeNetworkInfo
            return ni != null && ni.isConnected
        }
}