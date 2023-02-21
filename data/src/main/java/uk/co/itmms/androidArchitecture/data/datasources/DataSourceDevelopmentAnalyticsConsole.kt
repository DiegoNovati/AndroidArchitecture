package uk.co.itmms.androidArchitecture.data.datasources

import uk.co.itmms.androidArchitecture.data.datasources.logging.ILoggingConsole
import uk.co.itmms.androidArchitecture.data.datasources.logging.LoggingConsole

interface IDataSourceDevelopmentAnalytics {
    suspend fun setDevice(deviceId: String)
    suspend fun setUsername(userName: String)
    suspend fun setProperty(name: String, value: String?)

    suspend fun logEvent(eventName: String, eventParamList: List<Pair<String, Any>>)

    suspend fun logUseCase(useCaseName: String, milliSec: Long)
}

class DataSourceDevelopmentAnalyticsConsole(
    private val loggingConsole: ILoggingConsole = LoggingConsole(),
): IDataSourceDevelopmentAnalytics {

    companion object {
        const val TagAnalytics = "Analytics"
    }

    override suspend fun setDevice(deviceId: String) =
        loggingConsole.write(
            ILoggingConsole.MessageType.Info,
            TagAnalytics,
            "setDevice: deviceId = $deviceId"
        )

    override suspend fun setUsername(userName: String) =
        loggingConsole.write(
            ILoggingConsole.MessageType.Info,
            TagAnalytics,
            "setUsername: userName = $userName"
        )

    override suspend fun setProperty(name: String, value: String?) =
        loggingConsole.write(
            ILoggingConsole.MessageType.Info,
            TagAnalytics,
            "setProperty: name = $name, value = $value"
        )

    override suspend fun logEvent(eventName: String, eventParamList: List<Pair<String, Any>>) {
        val str = eventParamList.joinToString(", ") {
            "${it.first} = ${it.second}"
        }

        loggingConsole.write(
            ILoggingConsole.MessageType.Info,
            TagAnalytics,
            "logEvent - eventName: $eventName, eventParamList: $str"
        )
    }

    override suspend fun logUseCase(useCaseName: String, milliSec: Long) =
        loggingConsole.write(
            ILoggingConsole.MessageType.Info,
            TagAnalytics,
            "logUseCase - useCaseName: $useCaseName, milliSec: $milliSec"
        )
}