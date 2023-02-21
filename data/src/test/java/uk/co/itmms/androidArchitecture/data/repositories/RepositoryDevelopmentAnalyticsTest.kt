package uk.co.itmms.androidArchitecture.data.repositories

import uk.co.itmms.androidArchitecture.data.BaseDataTest
import uk.co.itmms.androidArchitecture.data.datasources.IDataSourceDevelopmentAnalytics
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class RepositoryDevelopmentAnalyticsTest : BaseDataTest() {

    @MockK
    private lateinit var mockDataSourceDevelopmentAnalyticsOne: IDataSourceDevelopmentAnalytics

    @MockK
    private lateinit var mockDataSourceDevelopmentAnalyticsTwo: IDataSourceDevelopmentAnalytics

    private lateinit var repositoryDevelopmentAnalytics: RepositoryDevelopmentAnalytics

    @Before
    fun setUp() {
        repositoryDevelopmentAnalytics = RepositoryDevelopmentAnalytics(
            listOf(
                mockDataSourceDevelopmentAnalyticsOne,
                mockDataSourceDevelopmentAnalyticsTwo,
            )
        )
    }

    @Test
    fun `testing setDevice`() = runBlocking {
        val deviceId = "deviceId"

        repositoryDevelopmentAnalytics.setDevice(deviceId)

        coVerify(exactly = 1) {
            mockDataSourceDevelopmentAnalyticsOne.setDevice(deviceId)
            mockDataSourceDevelopmentAnalyticsTwo.setDevice(deviceId)
        }
        confirmVerified(mockDataSourceDevelopmentAnalyticsOne, mockDataSourceDevelopmentAnalyticsTwo)
    }

    @Test
    fun `testing setProperty`() = runBlocking {
        val name = "name"
        val value = 1234

        repositoryDevelopmentAnalytics.setProperty(name, value.toString())

        coVerify(exactly = 1) {
            mockDataSourceDevelopmentAnalyticsOne.setProperty(name, value.toString())
            mockDataSourceDevelopmentAnalyticsTwo.setProperty(name, value.toString())
        }
        confirmVerified(mockDataSourceDevelopmentAnalyticsOne, mockDataSourceDevelopmentAnalyticsTwo)
    }

    @Test
    fun `testing logEvent`() = runBlocking {
        val eventName = "eventName"
        val eventParamList = listOf(
            Pair("paramOne", 10),
            Pair("paramTwo", 20)
        )

        repositoryDevelopmentAnalytics.logEvent(eventName, eventParamList)

        coVerify(exactly = 1) {
            mockDataSourceDevelopmentAnalyticsOne.logEvent(eventName, eventParamList)
            mockDataSourceDevelopmentAnalyticsTwo.logEvent(eventName, eventParamList)
        }
        confirmVerified(mockDataSourceDevelopmentAnalyticsOne, mockDataSourceDevelopmentAnalyticsTwo)
    }

    @Test
    fun `testing logUseCase`() = runBlocking {
        val useCaseName = "useCaseName"
        val milliSec = 5555L

        repositoryDevelopmentAnalytics.logUseCase(useCaseName, milliSec)

        coVerify(exactly = 1) {
            mockDataSourceDevelopmentAnalyticsOne.logUseCase(useCaseName, milliSec)
            mockDataSourceDevelopmentAnalyticsTwo.logUseCase(useCaseName, milliSec)
        }
        confirmVerified(mockDataSourceDevelopmentAnalyticsOne, mockDataSourceDevelopmentAnalyticsTwo)
    }
}