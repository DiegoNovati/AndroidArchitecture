package uk.co.itmms.androidArchitecture.data.repositories

import kotlinx.coroutines.flow.Flow
import uk.co.itmms.androidArchitecture.data.datasources.IDataSourceConnectivityMonitor
import uk.co.itmms.androidArchitecture.domain.repositories.IRepositoryNetworkMonitor

class RepositoryNetworkMonitor(
    private val dataSourceConnectivityMonitor: IDataSourceConnectivityMonitor
) : IRepositoryNetworkMonitor {

    override fun monitor(): Flow<Boolean> =
        dataSourceConnectivityMonitor.monitor()
}