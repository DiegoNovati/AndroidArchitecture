package uk.co.itmms.androidArchitecture.data.datasources.networkMonitor

import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.annotation.RequiresApi

interface IConnectivityChecker {
    fun isConnected(): Boolean
}

class ConnectivityChecker(
    private val connectivityManager: ConnectivityManager
) : IConnectivityChecker {

    private val deviceConnectivityChecker: IDeviceConnectivityChecker

    init {
        deviceConnectivityChecker = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            DeviceConnectivityCheckerFromMarshMallow
        } else {
            DeviceConnectivityCheckerUntilLollipop
        }
    }

    override fun isConnected(): Boolean =
        deviceConnectivityChecker.isConnected(connectivityManager)

    internal interface IDeviceConnectivityChecker {
        fun isConnected(connectivityManager: ConnectivityManager): Boolean
    }

    object DeviceConnectivityCheckerUntilLollipop : IDeviceConnectivityChecker {
        @Suppress("DEPRECATION")
        override fun isConnected(connectivityManager: ConnectivityManager): Boolean =
            connectivityManager.activeNetworkInfo?.isConnected ?: false
    }

    object DeviceConnectivityCheckerFromMarshMallow : IDeviceConnectivityChecker {
        @RequiresApi(Build.VERSION_CODES.M)
        override fun isConnected(connectivityManager: ConnectivityManager): Boolean =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
                ?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true
    }
}