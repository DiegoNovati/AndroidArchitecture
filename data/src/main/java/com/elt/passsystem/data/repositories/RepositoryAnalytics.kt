package com.elt.passsystem.data.repositories

import com.elt.passsystem.data.datasources.IDataSourceAnalytics
import com.elt.passsystem.domain.repositories.IRepositoryAnalytics

class RepositoryAnalytics(
    private val dataSourceAnalytics: IDataSourceAnalytics,
): IRepositoryAnalytics {

    override suspend fun setDevice(deviceId: String) =
        dataSourceAnalytics.setDevice(deviceId)

    override suspend fun setUsername(userName: String) =
        dataSourceAnalytics.setUsername(userName)

    override suspend fun logUseCase(useCaseName: String) =
        dataSourceAnalytics.logUseCase(useCaseName)
}