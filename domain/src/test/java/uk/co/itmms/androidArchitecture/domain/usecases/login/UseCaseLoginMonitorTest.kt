package uk.co.itmms.androidArchitecture.domain.usecases.login

import uk.co.itmms.androidArchitecture.domain.BaseDomainTest
import uk.co.itmms.androidArchitecture.domain.repositories.IRepositoryNetworkMonitor
import uk.co.itmms.androidArchitecture.domain.repositories.IRepositoryRuntime
import uk.co.itmms.androidArchitecture.domain.usecases.NoParams
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.impl.annotations.MockK
import junit.framework.TestCase.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class UseCaseLoginMonitorTest : BaseDomainTest() {

    @MockK
    private lateinit var mockRepositoryNetworkMonitor: IRepositoryNetworkMonitor

    @MockK
    private lateinit var mockRepositoryRuntime: IRepositoryRuntime

    private lateinit var useCaseLoginMonitor: UseCaseLoginMonitor

    @Before
    fun setUp() {
        useCaseLoginMonitor = UseCaseLoginMonitor(
            mockRepositoryDevelopmentLogger, mockRepositoryDevelopmentAnalytics, mockRepositoryNetworkMonitor,
            mockRepositoryRuntime,
        )
    }

    @Test
    fun `testing connection state`() = runBlocking {
        coEvery { mockRepositoryNetworkMonitor.monitor() } returns flow { emit(true) }
        coEvery { mockRepositoryRuntime.isAuthenticatedFlow() } returns MutableStateFlow(false).asStateFlow()

        val actual = useCaseLoginMonitor.run(NoParams)

        actual.fold({
            fail("Unexpected failure")
        }){
            val update = it.update.first()
            assertEquals(UseCaseLoginMonitor.UpdateType.Connected, update.updateType)
            assertTrue(update.value)
        }

        coVerify(exactly = 1) {
            mockRepositoryNetworkMonitor.monitor()
        }
        confirmVerified(mockRepositoryNetworkMonitor)
    }

    @Test
    fun `testing authentication state`() = runBlocking {
        coEvery { mockRepositoryNetworkMonitor.monitor() } returns flow { emit(true) }
        coEvery { mockRepositoryRuntime.isAuthenticatedFlow() } returns MutableStateFlow(false).asStateFlow()

        val actual = useCaseLoginMonitor.run(NoParams)

        actual.fold({
            fail("Unexpected failure")
        }){
            val update = it.update.take(2).last()
            assertEquals(UseCaseLoginMonitor.UpdateType.Authentication, update.updateType)
            assertFalse(update.value)
        }

        coVerify(exactly = 1) {
            mockRepositoryNetworkMonitor.monitor()
        }
        confirmVerified(mockRepositoryNetworkMonitor)
    }
}