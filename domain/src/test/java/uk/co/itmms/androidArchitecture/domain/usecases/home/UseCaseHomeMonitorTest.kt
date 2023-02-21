package uk.co.itmms.androidArchitecture.domain.usecases.home

import uk.co.itmms.androidArchitecture.domain.BaseDomainTest
import uk.co.itmms.androidArchitecture.domain.repositories.IRepositoryNetworkMonitor
import uk.co.itmms.androidArchitecture.domain.usecases.NoParams
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.impl.annotations.MockK
import junit.framework.TestCase.assertTrue
import junit.framework.TestCase.fail
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class UseCaseHomeMonitorTest : BaseDomainTest() {

    @MockK
    private lateinit var mockRepositoryNetworkMonitor: IRepositoryNetworkMonitor

    private lateinit var useCaseHomeMonitor: UseCaseHomeMonitor

    @Before
    fun setUp() {
        useCaseHomeMonitor = UseCaseHomeMonitor(
            mockRepositoryDevelopmentLogger, mockRepositoryDevelopmentAnalytics, mockRepositoryNetworkMonitor
        )
    }

    @Test
    fun `testing useCaseNetworkMonitor`() = runBlocking {
        coEvery { mockRepositoryNetworkMonitor.monitor() } returns flow { emit(true) }

        val actual = useCaseHomeMonitor.run(NoParams)

        actual.fold({
            fail("Unexpected failure")
        }){
            assertTrue(it.connected.first())
        }

        coVerify(exactly = 1) {
            mockRepositoryNetworkMonitor.monitor()
        }
        confirmVerified(mockRepositoryNetworkMonitor)
    }
}