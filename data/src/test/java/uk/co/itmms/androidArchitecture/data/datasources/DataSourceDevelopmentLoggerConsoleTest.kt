package uk.co.itmms.androidArchitecture.data.datasources

import uk.co.itmms.androidArchitecture.data.BaseDataTest
import uk.co.itmms.androidArchitecture.data.datasources.logging.ILoggingConsole
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class DataSourceDevelopmentLoggerConsoleTest : BaseDataTest() {

    @MockK
    private lateinit var mockLoggingConsole: ILoggingConsole

    private lateinit var dataSourceDevelopmentLoggerConsole: DataSourceDevelopmentLoggerConsole

    @Before
    fun setUp() {
        dataSourceDevelopmentLoggerConsole = DataSourceDevelopmentLoggerConsole(mockLoggingConsole)
    }

    @Test
    fun `testing setDevice`() = runBlocking {
        val deviceId = "deviceId"

        dataSourceDevelopmentLoggerConsole.setDevice(deviceId)

        coVerify(exactly = 1) {
            mockLoggingConsole.write(
                ILoggingConsole.MessageType.Info,
                DataSourceDevelopmentLoggerConsole.TagLogger,
                "setDevice: deviceId = $deviceId"
            )
        }
        confirmVerified(mockLoggingConsole)
    }

    @Test
    fun `testing setUsername`() = runBlocking {
        val username = "pin1234"

        dataSourceDevelopmentLoggerConsole.setUsername(username)

        coVerify(exactly = 1) {
            mockLoggingConsole.write(
                ILoggingConsole.MessageType.Info,
                DataSourceDevelopmentLoggerConsole.TagLogger,
                "setUsername: username = $username"
            )
        }
        confirmVerified(mockLoggingConsole)
    }

    @Test
    fun `testing setProperty`() = runBlocking {
        val name = "propName"
        val value = "propValue"

        dataSourceDevelopmentLoggerConsole.setProperty(name, value)

        coVerify(exactly = 1) {
            mockLoggingConsole.write(
                ILoggingConsole.MessageType.Info,
                DataSourceDevelopmentLoggerConsole.TagLogger,
                "setProperty: name = $name, value = $value"
            )
        }
        confirmVerified(mockLoggingConsole)
    }

    @Test
    fun `testing logIssue`() = runBlocking {
        val tag = "issueTag"
        val message = "issueMessage"

        dataSourceDevelopmentLoggerConsole.logIssue(tag, message)

        coVerify(exactly = 1) {
            mockLoggingConsole.write(ILoggingConsole.MessageType.Error, tag, message)
        }
        confirmVerified(mockLoggingConsole)
    }

    @Test
    fun `testing logDebug`() = runBlocking {
        val tag = "debugTag"
        val message = "debugMessage"
        dataSourceDevelopmentLoggerConsole.logDebug(tag, message)

        coVerify(exactly = 1) {
            mockLoggingConsole.write(ILoggingConsole.MessageType.Debug, tag, message)
        }
        confirmVerified(mockLoggingConsole)
    }

    @Test
    fun `testing logInfo`() = runBlocking {
        val tag = "infoTag"
        val message = "infoMessage"
        dataSourceDevelopmentLoggerConsole.logInfo(tag, message)

        coVerify(exactly = 1) {
            mockLoggingConsole.write(ILoggingConsole.MessageType.Info, tag, message)
        }
        confirmVerified(mockLoggingConsole)
    }

    @Test
    fun `testing logWarning`() = runBlocking {
        val tag = "warningTag"
        val message = "warningMessage"
        dataSourceDevelopmentLoggerConsole.logWarning(tag, message)

        coVerify(exactly = 1) {
            mockLoggingConsole.write(ILoggingConsole.MessageType.Warn, tag, message)
        }
        confirmVerified(mockLoggingConsole)
    }

    @Test
    fun `testing logError`() = runBlocking {
        val tag = "errorTag"
        val message = "errorMessage"

        dataSourceDevelopmentLoggerConsole.logError(tag, message)

        coVerify(exactly = 1) {
            mockLoggingConsole.write(ILoggingConsole.MessageType.Error, tag, message)
        }
        confirmVerified(mockLoggingConsole)
    }

    @Test
    fun `testing send`() = runBlocking {
        dataSourceDevelopmentLoggerConsole.send()

        confirmVerified(mockLoggingConsole)
    }
}