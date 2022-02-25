package com.elt.passsystem.data.datasources

import com.elt.passsystem.data.datasources.networkMonitor.IConnectivityChecker
import com.elt.passsystem.data.datasources.networkMonitor.IConnectivityMonitorCallback
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

interface IDataSourceConnectivityMonitor {
    suspend fun monitor(): Flow<Boolean>
}

class DataSourceConnectivityMonitor(
    private val connectivityChecker: IConnectivityChecker,
    private val connectivityMonitorCallback: IConnectivityMonitorCallback,
) : IDataSourceConnectivityMonitor {

    override suspend fun monitor(): Flow<Boolean> = callbackFlow {
        connectivityMonitorCallback.startMonitoring { connected ->
            trySend(connected)
        }

        trySend(connectivityChecker.isConnected())

        awaitClose {
            connectivityMonitorCallback.stopMonitoring()
        }
    }
}