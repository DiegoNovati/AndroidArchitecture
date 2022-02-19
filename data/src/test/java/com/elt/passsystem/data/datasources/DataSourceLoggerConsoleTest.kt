package com.elt.passsystem.data.datasources

import com.elt.passsystem.data.BaseDataTest
import com.elt.passsystem.data.datasources.console.IConsoleApi
import com.elt.passsystem.data.extensions.fullStackTraceToString
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class DataSourceLoggerConsoleTest: BaseDataTest() {

    @MockK
    private lateinit var mockConsoleApi: IConsoleApi

    private lateinit var dataSourceLoggerConsole: DataSourceLoggerConsole

    @Before
    fun setUp() {
        dataSourceLoggerConsole = DataSourceLoggerConsole(mockConsoleApi)
    }

    @Test
    fun `WHEN setDevice invoked THEN apiConsole is called`() = runBlocking {
        val deviceId = "1234567890"

        dataSourceLoggerConsole.setDevice(deviceId)

        coVerify(exactly = 1) {
            mockConsoleApi.log("logger.setDeviceId", deviceId)
        }
        confirmVerified(mockConsoleApi)
    }

    @Test
    fun `WHEN setUsername invoked THEN apiConsole is called`() = runBlocking {
        val username = "username"

        dataSourceLoggerConsole.setUsername(username)

        coVerify(exactly = 1) {
            mockConsoleApi.log("logger.setUsername", username)
        }
        confirmVerified(mockConsoleApi)
    }

    @Test
    fun `WHEN logInfo invoked THEN apiConsole is called`() = runBlocking {
        val useCaseName = "UseCaseTest"
        val message = "the message"

        dataSourceLoggerConsole.logInfo(useCaseName, message)

        coVerify(exactly = 1) {
            mockConsoleApi.log("logInfo", useCaseName, message)
        }
        confirmVerified(mockConsoleApi)
    }

    @Test
    fun `WHEN logError invoked THEN apiConsole is called`() = runBlocking {
        val useCaseName = "UseCaseTest"
        val params = "params"
        val failure = "internet connection missing"

        dataSourceLoggerConsole.logError(useCaseName, params, failure)

        coVerify(exactly = 1) {
            mockConsoleApi.log("logError", useCaseName, "$params, $failure")
        }
        confirmVerified(mockConsoleApi)
    }

    @Test
    fun `WHEN logUnexpectedThrowable invoked THEN apiConsole is called`() = runBlocking {
        val useCaseName = "UseCaseTest"
        val params = "params"
        val e = RuntimeException("internet connection missing")

        dataSourceLoggerConsole.logUnexpectedThrowable(useCaseName, params, e)

        coVerify(exactly = 1) {
            mockConsoleApi.log("logUnexpectedThrowable", useCaseName, "$params, ${e.fullStackTraceToString()}")
        }
        confirmVerified(mockConsoleApi)
    }

    @Test
    fun `WHEN logUnexpectedFailure invoked THEN apiConsole is called`() = runBlocking {
        val useCaseName = "UseCaseTest"
        val params = "params"
        val failure = "internet connection missing"

        dataSourceLoggerConsole.logUnexpectedFailure(useCaseName, params, failure)

        coVerify(exactly = 1) {
            mockConsoleApi.log("logUnexpectedFailure", useCaseName, "$params, $failure")
        }
        confirmVerified(mockConsoleApi)
    }
}