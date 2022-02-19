package com.elt.passsystem.data.datasources

import com.elt.passsystem.data.datasources.console.ConsoleApi
import com.elt.passsystem.data.datasources.console.IConsoleApi
import com.elt.passsystem.data.extensions.fullStackTraceToString

interface IDataSourceLogger {
    suspend fun setDevice(deviceId: String)
    suspend fun setUsername(username: String)

    suspend fun logInfo(useCaseName: String, message: String)
    suspend fun logError(useCaseName: String, params: String, failure: String)
    suspend fun logUnexpectedThrowable(useCaseName: String, params: String, e: Throwable)
    suspend fun logUnexpectedFailure(useCaseName: String, params: String, failure: String)
}

class DataSourceLoggerConsole(
    private val consoleApi: IConsoleApi = ConsoleApi(),
): IDataSourceLogger {

    override suspend fun setDevice(deviceId: String) =
        consoleApi.log("logger.setDeviceId", deviceId)

    override suspend fun setUsername(username: String) =
        consoleApi.log("logger.setUsername", username)

    override suspend fun logInfo(useCaseName: String, message: String) =
        consoleApi.log("logInfo", useCaseName, message)

    override suspend fun logError(useCaseName: String, params: String, failure: String) =
        consoleApi.log("logError", useCaseName, "$params, $failure")

    override suspend fun logUnexpectedThrowable(useCaseName: String, params: String, e: Throwable) =
        consoleApi.log("logUnexpectedThrowable", useCaseName, "$params, ${e.fullStackTraceToString()}")

    override suspend fun logUnexpectedFailure(
        useCaseName: String,
        params: String,
        failure: String
    ) =
        consoleApi.log("logUnexpectedFailure", useCaseName, "$params, $failure")
}