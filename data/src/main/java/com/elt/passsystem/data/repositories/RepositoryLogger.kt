package com.elt.passsystem.data.repositories

import com.elt.passsystem.data.datasources.IDataSourceLogger
import com.elt.passsystem.domain.repositories.IRepositoryLogger

class RepositoryLogger(
    private val dataSourceLogger: IDataSourceLogger,
) : IRepositoryLogger {

    override suspend fun setDevice(deviceId: String) =
        dataSourceLogger.setDevice(deviceId)

    override suspend fun setUsername(username: String) =
        dataSourceLogger.setUsername(username)

    override suspend fun logInfo(useCaseName: String, message: String) =
        dataSourceLogger.logInfo(useCaseName, message)

    override suspend fun logError(useCaseName: String, params: String, failure: String) =
        dataSourceLogger.logError(useCaseName, params, failure)

    override suspend fun logUnexpectedThrowable(useCaseName: String, params: String, e: Throwable) =
        dataSourceLogger.logUnexpectedThrowable(useCaseName, params, e)

    override suspend fun logUnexpectedFailure(
        useCaseName: String,
        params: String,
        failure: String
    ) =
        dataSourceLogger.logUnexpectedFailure(useCaseName, params, failure)
}