package uk.co.itmms.androidArchitecture.data.repositories

import uk.co.itmms.androidArchitecture.data.datasources.IDataSourceDevelopmentLogger
import uk.co.itmms.androidArchitecture.domain.repositories.IRepositoryDevelopmentLogger

class RepositoryDevelopmentLogger(
    private val dataSourceDevelopmentLoggerList: List<IDataSourceDevelopmentLogger>,
) : IRepositoryDevelopmentLogger {
    override suspend fun setDevice(deviceId: String) =
        dataSourceDevelopmentLoggerList.forEach { it.setDevice(deviceId) }

    override suspend fun setUsername(username: String) =
        dataSourceDevelopmentLoggerList.forEach { it.setUsername(username) }

    override suspend fun setProperty(name: String, value: Any) =
        dataSourceDevelopmentLoggerList.forEach { it.setProperty(name, value) }

    override suspend fun logIssue(tag: String, message: String) =
        dataSourceDevelopmentLoggerList.forEach { it.logIssue(tag, message) }

    override suspend fun logDebug(tag: String, message: String) =
        dataSourceDevelopmentLoggerList.forEach { it.logDebug(tag, message) }

    override suspend fun logInfo(tag: String, message: String) =
        dataSourceDevelopmentLoggerList.forEach { it.logInfo(tag, message) }

    override suspend fun logWarning(tag: String, message: String) =
        dataSourceDevelopmentLoggerList.forEach { it.logWarning(tag, message) }

    override suspend fun logError(tag: String, message: String) =
        dataSourceDevelopmentLoggerList.forEach { it.logError(tag, message) }

    override suspend fun send() =
        dataSourceDevelopmentLoggerList.forEach { it.send() }
}