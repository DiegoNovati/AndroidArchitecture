package com.elt.passsystem.domain.usecases.networkMonitor

import arrow.core.Either
import com.elt.passsystem.domain.BaseDomainTest
import com.elt.passsystem.domain.repositories.IRepositoryAnalytics
import com.elt.passsystem.domain.repositories.IRepositoryLogger
import com.elt.passsystem.domain.repositories.IRepositoryNetworkMonitor
import com.elt.passsystem.domain.usecases.NoParams
import io.mockk.*
import io.mockk.impl.annotations.MockK
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class UseCaseNetworkMonitorTest : BaseDomainTest() {

    @MockK
    private lateinit var mockRepositoryLogger: IRepositoryLogger

    @MockK
    private lateinit var mockRepositoryAnalytics: IRepositoryAnalytics

    @MockK
    private lateinit var mockRepositoryNetworkMonitor: IRepositoryNetworkMonitor

    private lateinit var useCaseNetworkMonitor: UseCaseNetworkMonitor

    @Before
    fun setUp() {
        useCaseNetworkMonitor = UseCaseNetworkMonitor(
            mockRepositoryLogger, mockRepositoryAnalytics, mockRepositoryNetworkMonitor
        )
    }

    @Test
    fun `testing run`() = runBlocking {
        val spy = spyk(useCaseNetworkMonitor)
        val mockMonitorResult = mockk<Flow<Boolean>>()

        coEvery { spy.monitor() } returns Either.Right(mockMonitorResult)

        spy.run(NoParams)

        coVerify(exactly = 1) {
            spy.run(NoParams)
            spy.monitor()
        }
        confirmVerified(spy)
    }

    @Test
    fun `WHEN monitor is successful THEN returns a right value`() = runBlocking {
        coEvery { mockRepositoryNetworkMonitor.monitor() } returns Either.Right(flow { })

        val actual = useCaseNetworkMonitor.monitor()

        assertTrue(actual.isRight())

        coVerify(exactly = 1) {
            mockRepositoryNetworkMonitor.monitor()
        }
        confirmVerified(mockRepositoryNetworkMonitor)
    }
}