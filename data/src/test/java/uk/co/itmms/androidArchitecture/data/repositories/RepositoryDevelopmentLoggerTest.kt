package uk.co.itmms.androidArchitecture.data.repositories

import uk.co.itmms.androidArchitecture.data.BaseDataTest
import uk.co.itmms.androidArchitecture.data.datasources.IDataSourceDevelopmentLogger
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class RepositoryDevelopmentLoggerTest : BaseDataTest() {

    @MockK
    private lateinit var mockDataSourceDevelopmentLoggerOne: IDataSourceDevelopmentLogger

    @MockK
    private lateinit var mockDataSourceDevelopmentLoggerTwo: IDataSourceDevelopmentLogger

    private lateinit var repositoryDevelopmentLogger: RepositoryDevelopmentLogger

    private val tag = "tag"
    private val message = "message"

    @Before
    fun setUp() {
        repositoryDevelopmentLogger = RepositoryDevelopmentLogger(
            listOf(
                mockDataSourceDevelopmentLoggerOne,
                mockDataSourceDevelopmentLoggerTwo,
            )
        )
    }

    @Test
    fun `testing setDevice`() = runBlocking {
        val deviceId = "deviceId"

        repositoryDevelopmentLogger.setDevice(deviceId)

        coVerify(exactly = 1) {
            mockDataSourceDevelopmentLoggerOne.setDevice(deviceId)
            mockDataSourceDevelopmentLoggerTwo.setDevice(deviceId)
        }
        confirmVerified(mockDataSourceDevelopmentLoggerOne, mockDataSourceDevelopmentLoggerTwo)
    }

    @Test
    fun `testing setUsername`() = runBlocking {
        val username = "username"

        repositoryDevelopmentLogger.setUsername(username)

        coVerify(exactly = 1) {
            mockDataSourceDevelopmentLoggerOne.setUsername(username)
            mockDataSourceDevelopmentLoggerTwo.setUsername(username)
        }
        confirmVerified(mockDataSourceDevelopmentLoggerOne, mockDataSourceDevelopmentLoggerTwo)
    }

    @Test
    fun `testing setProperty`() = runBlocking {
        val name = "name"
        val value = 1234

        repositoryDevelopmentLogger.setProperty(name, value)

        coVerify(exactly = 1) {
            mockDataSourceDevelopmentLoggerOne.setProperty(name, value)
            mockDataSourceDevelopmentLoggerTwo.setProperty(name, value)
        }
        confirmVerified(mockDataSourceDevelopmentLoggerOne, mockDataSourceDevelopmentLoggerTwo)
    }

    @Test
    fun `testing logIssue`() = runBlocking {
        repositoryDevelopmentLogger.logIssue(tag, message)

        coVerify(exactly = 1) {
            mockDataSourceDevelopmentLoggerOne.logIssue(tag, message)
            mockDataSourceDevelopmentLoggerTwo.logIssue(tag, message)
        }
        confirmVerified(mockDataSourceDevelopmentLoggerOne, mockDataSourceDevelopmentLoggerTwo)
    }

    @Test
    fun `testing logDebug`() = runBlocking {
        repositoryDevelopmentLogger.logDebug(tag, message)

        coVerify(exactly = 1) {
            mockDataSourceDevelopmentLoggerOne.logDebug(tag, message)
            mockDataSourceDevelopmentLoggerTwo.logDebug(tag, message)
        }
        confirmVerified(mockDataSourceDevelopmentLoggerOne, mockDataSourceDevelopmentLoggerTwo)
    }

    @Test
    fun `testing logInfo`() = runBlocking {
        repositoryDevelopmentLogger.logInfo(tag, message)

        coVerify(exactly = 1) {
            mockDataSourceDevelopmentLoggerOne.logInfo(tag, message)
            mockDataSourceDevelopmentLoggerTwo.logInfo(tag, message)
        }
        confirmVerified(mockDataSourceDevelopmentLoggerOne, mockDataSourceDevelopmentLoggerTwo)
    }

    @Test
    fun `testing logWarning`() = runBlocking {
        repositoryDevelopmentLogger.logWarning(tag, message)

        coVerify(exactly = 1) {
            mockDataSourceDevelopmentLoggerOne.logWarning(tag, message)
            mockDataSourceDevelopmentLoggerTwo.logWarning(tag, message)
        }
        confirmVerified(mockDataSourceDevelopmentLoggerOne, mockDataSourceDevelopmentLoggerTwo)
    }

    @Test
    fun `testing logError`() = runBlocking {
        repositoryDevelopmentLogger.logError(tag, message)

        coVerify(exactly = 1) {
            mockDataSourceDevelopmentLoggerOne.logError(tag, message)
            mockDataSourceDevelopmentLoggerTwo.logError(tag, message)
        }
        confirmVerified(mockDataSourceDevelopmentLoggerOne, mockDataSourceDevelopmentLoggerTwo)
    }

    @Test
    fun `testing send`() = runBlocking {
        repositoryDevelopmentLogger.send()

        coVerify(exactly = 1) {
            mockDataSourceDevelopmentLoggerOne.send()
            mockDataSourceDevelopmentLoggerTwo.send()
        }
        confirmVerified(mockDataSourceDevelopmentLoggerOne, mockDataSourceDevelopmentLoggerTwo)
    }
}