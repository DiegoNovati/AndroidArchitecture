package com.elt.passsystem.data.datasources

import com.elt.passsystem.data.datasources.console.ConsoleApi
import com.elt.passsystem.data.datasources.console.IConsoleApi

interface IDataSourceAnalytics {
    suspend fun setDevice(deviceId: String)
    suspend fun setUsername(username: String)

    suspend fun logUseCase(useCaseName: String)
}

class DataSourceAnalyticsConsole(
    private val consoleApi: IConsoleApi = ConsoleApi(),
): IDataSourceAnalytics {

    override suspend fun setDevice(deviceId: String) =
        consoleApi.log("analytics.setDeviceId", deviceId)

    override suspend fun setUsername(username: String) =
        consoleApi.log("analytics.setUsername", username)

    override suspend fun logUseCase(useCaseName: String) =
        consoleApi.log("logUseCase", useCaseName)
}