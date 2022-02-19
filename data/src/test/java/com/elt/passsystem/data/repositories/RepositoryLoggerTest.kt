package com.elt.passsystem.data.repositories

import com.elt.passsystem.data.BaseDataTest
import com.elt.passsystem.data.datasources.IDataSourceLogger
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class RepositoryLoggerTest: BaseDataTest() {

    @MockK
    private lateinit var mockDataSourceLogger: IDataSourceLogger

    private lateinit var repositoryLogger: RepositoryLogger

    @Before
    fun setUp() {
        repositoryLogger = RepositoryLogger(mockDataSourceLogger)
    }

    @Test
    fun `WHEN setDevice invoked THEN the datasource is called `() = runBlocking {
        val deviceId = "1234567890"

        repositoryLogger.setDevice(deviceId)

        coVerify(exactly = 1) {
            mockDataSourceLogger.setDevice(deviceId)
        }
        confirmVerified(mockDataSourceLogger)
    }

    @Test
    fun `WHEN setUsername invoked THEN the datasource is called `() = runBlocking {
        val username = "username"

        repositoryLogger.setUsername(username)

        coVerify(exactly = 1) {
            mockDataSourceLogger.setUsername(username)
        }
        confirmVerified(mockDataSourceLogger)
    }

    @Test
    fun `WHEN logInfo invoked THEN the datasource is called `() = runBlocking {
        val userCaseName = "UseCaseTest"
        val message = "the message"

        repositoryLogger.logInfo(userCaseName, message)

        coVerify(exactly = 1) {
            mockDataSourceLogger.logInfo(userCaseName, message)
        }
        confirmVerified(mockDataSourceLogger)
    }

    @Test
    fun `WHEN logError invoked THEN the datasource is called `() = runBlocking {
        val userCaseName = "UseCaseTest"
        val params = "param value"
        val failure = "no internet connection"

        repositoryLogger.logError(userCaseName, params, failure)

        coVerify(exactly = 1) {
            mockDataSourceLogger.logError(userCaseName, params, failure)
        }
        confirmVerified(mockDataSourceLogger)
    }

    @Test
    fun `WHEN logUnexpectedThrowable invoked THEN the datasource is called `() = runBlocking {
        val userCaseName = "UseCaseTest"
        val params = "param value"
        val throwable = RuntimeException("no internet connection")

        repositoryLogger.logUnexpectedThrowable(userCaseName, params, throwable)

        coVerify(exactly = 1) {
            mockDataSourceLogger.logUnexpectedThrowable(userCaseName, params, throwable)
        }
        confirmVerified(mockDataSourceLogger)
    }

    @Test
    fun `WHEN logUnexpectedFailure invoked THEN the datasource is called `() = runBlocking {
        val userCaseName = "UseCaseTest"
        val params = "param value"
        val failure = "no internet connection"

        repositoryLogger.logUnexpectedFailure(userCaseName, params, failure)

        coVerify(exactly = 1) {
            mockDataSourceLogger.logUnexpectedFailure(userCaseName, params, failure)
        }
        confirmVerified(mockDataSourceLogger)
    }
}