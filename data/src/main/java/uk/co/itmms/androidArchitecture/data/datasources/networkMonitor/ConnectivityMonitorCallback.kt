package uk.co.itmms.androidArchitecture.data.datasources.networkMonitor

import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Build

interface IConnectivityMonitorCallback {
    fun startMonitoring(onStatusChanged: (Boolean) -> Unit)
    fun stopMonitoring()
}

class ConnectivityMonitorCallback(
    private val connectivityManager: ConnectivityManager,
) : IConnectivityMonitorCallback{

    private var networkCallback: ConnectivityManager.NetworkCallback? = null

    override fun startMonitoring(onStatusChanged: (Boolean) -> Unit) {
        stopMonitoring()

        networkCallback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                super.onAvailable(network)
                onStatusChanged(true)
            }

            override fun onLost(network: Network) {
                onStatusChanged(false)
                super.onLost(network)
            }
        }
        val networkRequest = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .apply {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    addCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
                }
            }
            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
            .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
            .build()
        networkCallback?.let {
            connectivityManager.registerNetworkCallback(networkRequest, it)
        }
    }

    override fun stopMonitoring() {
        networkCallback?.let {
            connectivityManager.unregisterNetworkCallback(it)
        }
        networkCallback = null
    }
}