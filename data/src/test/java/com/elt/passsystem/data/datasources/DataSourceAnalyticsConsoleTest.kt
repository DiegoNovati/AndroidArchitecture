package com.elt.passsystem.data.datasources

import com.elt.passsystem.data.BaseDataTest
import com.elt.passsystem.data.datasources.console.IConsoleApi
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class DataSourceAnalyticsConsoleTest: BaseDataTest() {

    @MockK
    private lateinit var mockConsoleApi: IConsoleApi

    private lateinit var dataSourceAnalyticsConsole: DataSourceAnalyticsConsole

    @Before
    fun setUp() {
        dataSourceAnalyticsConsole = DataSourceAnalyticsConsole(mockConsoleApi)
    }

    @Test
    fun `WHEN setDevice invoked THEN consoleApi is called `() = runBlocking {
        val deviceId = "1234567890"

        dataSourceAnalyticsConsole.setDevice(deviceId)

        coVerify(exactly = 1) {
            mockConsoleApi.log("analytics.setDeviceId", deviceId)
        }
        confirmVerified(mockConsoleApi)
    }

    @Test
    fun `WHEN setUsername invoked THEN consoleApi is called `() = runBlocking {
        val username = "username"

        dataSourceAnalyticsConsole.setUsername(username)

        coVerify(exactly = 1) {
            mockConsoleApi.log("analytics.setUsername", username)
        }
        confirmVerified(mockConsoleApi)
    }

    @Test
    fun `WHEN logUseCase invoked THEN consoleApi is called `() = runBlocking {
        val useCaseName = "UseCaseTest"

        dataSourceAnalyticsConsole.logUseCase(useCaseName)

        coVerify(exactly = 1) {
            mockConsoleApi.log("logUseCase", useCaseName)
        }
        confirmVerified(mockConsoleApi)
    }
}