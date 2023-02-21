package uk.co.itmms.androidArchitecture.data.repositories

import uk.co.itmms.androidArchitecture.data.datasources.IDataSourceDevelopmentAnalytics
import uk.co.itmms.androidArchitecture.domain.repositories.IRepositoryDevelopmentAnalytics

class RepositoryDevelopmentAnalytics(
    private val dataSourceDevelopmentAnalyticsList: List<IDataSourceDevelopmentAnalytics>,
): IRepositoryDevelopmentAnalytics {
    override suspend fun setDevice(deviceId: String) =
        dataSourceDevelopmentAnalyticsList.forEach { it.setDevice(deviceId) }

    override suspend fun setProperty(name: String, value: String?) =
        dataSourceDevelopmentAnalyticsList.forEach { it.setProperty(name, value) }

    override suspend fun logEvent(eventName: String, eventParamList: List<Pair<String, Any>>) =
        dataSourceDevelopmentAnalyticsList.forEach { it.logEvent(eventName, eventParamList) }

    override suspend fun logUseCase(useCaseName: String, milliSec: Long) =
        dataSourceDevelopmentAnalyticsList.forEach { it.logUseCase(useCaseName, milliSec) }
}