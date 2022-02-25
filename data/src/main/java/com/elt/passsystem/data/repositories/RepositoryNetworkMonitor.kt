package com.elt.passsystem.data.repositories

import arrow.core.Either
import com.elt.passsystem.data.datasources.IDataSourceConnectivityMonitor
import com.elt.passsystem.domain.repositories.IRepositoryNetworkMonitor
import com.elt.passsystem.domain.repositories.RepositoryNoFailure
import kotlinx.coroutines.flow.Flow

class RepositoryNetworkMonitor(
    private val dataSourceConnectivityMonitor: IDataSourceConnectivityMonitor
) : IRepositoryNetworkMonitor {

    override suspend fun monitor(): Either<RepositoryNoFailure, Flow<Boolean>> =
        Either.Right(dataSourceConnectivityMonitor.monitor())
}