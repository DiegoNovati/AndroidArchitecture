package com.elt.passsystem.data.repositories

import com.elt.passsystem.data.BaseDataTest
import com.elt.passsystem.data.datasources.IDataSourceAnalytics
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class RepositoryAnalyticsTest: BaseDataTest() {

    @MockK
    private lateinit var mockDataSourceAnalytics: IDataSourceAnalytics

    private lateinit var repositoryAnalytics: RepositoryAnalytics

    @Before
    fun setUp() {
        repositoryAnalytics = RepositoryAnalytics(mockDataSourceAnalytics)
    }

    @Test
    fun `WHEN setDevice invoked THEN the datasource is called`() = runBlocking {
        val deviceId = "1234567890"

        repositoryAnalytics.setDevice(deviceId)

        coVerify(exactly = 1) {
            mockDataSourceAnalytics.setDevice(deviceId)
        }
        confirmVerified(mockDataSourceAnalytics)
    }

    @Test
    fun `WHEN setUsername invoked THEN the datasource is called`() = runBlocking {
        val userName = "username"

        repositoryAnalytics.setUsername(userName)

        coVerify(exactly = 1) {
            mockDataSourceAnalytics.setUsername(userName)
        }
        confirmVerified(mockDataSourceAnalytics)
    }

    @Test
    fun `WHEN logUseCase invoked THEN the datasource is called`() = runBlocking {
        val useCase = "UseCaseTest"

        repositoryAnalytics.logUseCase(useCase)

        coVerify(exactly = 1) {
            mockDataSourceAnalytics.logUseCase(useCase)
        }
        confirmVerified(mockDataSourceAnalytics)
    }
}