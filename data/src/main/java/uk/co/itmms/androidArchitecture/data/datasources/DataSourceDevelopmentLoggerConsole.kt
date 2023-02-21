package uk.co.itmms.androidArchitecture.data.datasources

import uk.co.itmms.androidArchitecture.data.datasources.logging.ILoggingConsole
import uk.co.itmms.androidArchitecture.data.datasources.logging.LoggingConsole

interface IDataSourceDevelopmentLogger {
    suspend fun setDevice(deviceId: String)
    suspend fun setUsername(username: String)
    suspend fun setProperty(name: String, value: Any)

    suspend fun logIssue(tag: String, message: String)
    suspend fun logDebug(tag: String, message: String)
    suspend fun logInfo(tag: String, message: String)
    suspend fun logWarning(tag: String, message: String)
    suspend fun logError(tag: String, message: String)

    suspend fun send()
}

class DataSourceDevelopmentLoggerConsole(
    private val loggingConsole: ILoggingConsole = LoggingConsole(),
): IDataSourceDevelopmentLogger {
    companion object {
        const val TagLogger = "Logger"
    }

    override suspend fun setDevice(deviceId: String) =
        loggingConsole.write(
            ILoggingConsole.MessageType.Info,
            TagLogger,
            "setDevice: deviceId = $deviceId"
        )

    override suspend fun setUsername(username: String) =
        loggingConsole.write(
            ILoggingConsole.MessageType.Info,
            TagLogger,
            "setUsername: username = $username"
        )

    override suspend fun setProperty(name: String, value: Any) =
        loggingConsole.write(
            ILoggingConsole.MessageType.Info,
            TagLogger,
            "setProperty: name = $name, value = $value"
        )

    override suspend fun logIssue(tag: String, message: String) =
        loggingConsole.write(ILoggingConsole.MessageType.Error, tag, message)

    override suspend fun logDebug(tag: String, message: String) =
        loggingConsole.write(ILoggingConsole.MessageType.Debug, tag, message)

    override suspend fun logInfo(tag: String, message: String) =
        loggingConsole.write(ILoggingConsole.MessageType.Info, tag, message)

    override suspend fun logWarning(tag: String, message: String) =
        loggingConsole.write(ILoggingConsole.MessageType.Warn, tag, message)

    override suspend fun logError(tag: String, message: String) =
        loggingConsole.write(ILoggingConsole.MessageType.Error, tag, message)

    override suspend fun send() {}
}