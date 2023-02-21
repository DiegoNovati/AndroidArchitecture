package uk.co.itmms.androidArchitecture.data.datasources

import uk.co.itmms.androidArchitecture.data.datasources.networkMonitor.IConnectivityChecker
import uk.co.itmms.androidArchitecture.data.datasources.networkMonitor.IConnectivityMonitorCallback
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

interface IDataSourceConnectivityMonitor {
    fun monitor(): Flow<Boolean>
}

class DataSourceConnectivityMonitor(
    private val connectivityChecker: IConnectivityChecker,
    private val connectivityMonitorCallback: IConnectivityMonitorCallback,
) : IDataSourceConnectivityMonitor {

    override fun monitor(): Flow<Boolean> = callbackFlow {
        connectivityMonitorCallback.startMonitoring { connected ->
            trySend(connected)
        }

        trySend(connectivityChecker.isConnected())

        awaitClose {
            connectivityMonitorCallback.stopMonitoring()
        }
    }
}