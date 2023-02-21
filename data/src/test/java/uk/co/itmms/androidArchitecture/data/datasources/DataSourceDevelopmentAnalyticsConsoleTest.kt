package uk.co.itmms.androidArchitecture.data.datasources

import uk.co.itmms.androidArchitecture.data.BaseDataTest
import uk.co.itmms.androidArchitecture.data.datasources.logging.ILoggingConsole
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class DataSourceDevelopmentAnalyticsConsoleTest : BaseDataTest() {

    @MockK
    private lateinit var mockLoggingConsole: ILoggingConsole

    private lateinit var dataSourceDevelopmentAnalyticsConsole: DataSourceDevelopmentAnalyticsConsole

    @Before
    fun setUp() {
        dataSourceDevelopmentAnalyticsConsole = DataSourceDevelopmentAnalyticsConsole(mockLoggingConsole)
    }

    @Test
    fun `testing setDevice`() = runBlocking {
        val deviceId = "deviceId"

        dataSourceDevelopmentAnalyticsConsole.setDevice(deviceId)

        coVerify(exactly = 1) {
            mockLoggingConsole.write(
                ILoggingConsole.MessageType.Info,
                DataSourceDevelopmentAnalyticsConsole.TagAnalytics,
                "setDevice: deviceId = $deviceId"
            )
        }
        confirmVerified(mockLoggingConsole)
    }

    @Test
    fun `testing setUsername`() = runBlocking {
        val userName = "theUserName"

        dataSourceDevelopmentAnalyticsConsole.setUsername(userName)

        coVerify(exactly = 1) {
            mockLoggingConsole.write(
                ILoggingConsole.MessageType.Info,
                DataSourceDevelopmentAnalyticsConsole.TagAnalytics,
                "setUsername: userName = $userName"
            )
        }
        confirmVerified(mockLoggingConsole)
    }

    @Test
    fun `testing setProperty`() = runBlocking {
        val name = "propertyName"
        val value = "propertyValue"

        dataSourceDevelopmentAnalyticsConsole.setProperty(name, value)

        coVerify(exactly = 1) {
            mockLoggingConsole.write(
                ILoggingConsole.MessageType.Info,
                DataSourceDevelopmentAnalyticsConsole.TagAnalytics,
                "setProperty: name = $name, value = $value"
            )
        }
        confirmVerified(mockLoggingConsole)
    }

    @Test
    fun `testing logEvent`() = runBlocking {
        val eventName = "nameOfTheEvent"
        val paramList = listOf(Pair("param1", "value1"), Pair("param2", 2))

        dataSourceDevelopmentAnalyticsConsole.logEvent(eventName, paramList)

        coVerify(exactly = 1) {
            mockLoggingConsole.write(
                ILoggingConsole.MessageType.Info,
                DataSourceDevelopmentAnalyticsConsole.TagAnalytics,
                any()
            )
        }
        confirmVerified(mockLoggingConsole)
    }


    @Test
    fun `testing logUseCase`() = runBlocking {
        val useCaseName = "nameOfTheUseCase"
        val milliSec = 1111L

        dataSourceDevelopmentAnalyticsConsole.logUseCase(useCaseName, milliSec)

        coVerify(exactly = 1) {
            mockLoggingConsole.write(
                ILoggingConsole.MessageType.Info,
                DataSourceDevelopmentAnalyticsConsole.TagAnalytics,
                "logUseCase - useCaseName: $useCaseName, milliSec: $milliSec"
            )
        }
        confirmVerified(mockLoggingConsole)
    }
}