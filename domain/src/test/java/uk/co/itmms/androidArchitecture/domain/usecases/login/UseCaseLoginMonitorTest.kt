package uk.co.itmms.androidArchitecture.domain.usecases.login

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import junit.framework.TestCase.fail
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Test
import uk.co.itmms.androidArchitecture.domain.BaseDomainTest
import uk.co.itmms.androidArchitecture.domain.repositories.IRepositoryNetworkMonitor
import uk.co.itmms.androidArchitecture.domain.repositories.IRepositoryRuntime
import uk.co.itmms.androidArchitecture.domain.repositories.IRepositorySession
import uk.co.itmms.androidArchitecture.domain.usecases.NoParams

class UseCaseLoginMonitorTest : BaseDomainTest() {

    @MockK
    private lateinit var mockRepositoryNetworkMonitor: IRepositoryNetworkMonitor

    @MockK
    private lateinit var mockRepositoryRuntime: IRepositoryRuntime

    @MockK
    private lateinit var mockRepositorySession: IRepositorySession

    @InjectMockKs
    private lateinit var useCaseLoginMonitor: UseCaseLoginMonitor

    @After
    fun tearDown() {
        confirmVerified(mockRepositoryNetworkMonitor, mockRepositoryRuntime, mockRepositorySession)
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
            mockRepositoryRuntime.isAuthenticatedFlow()
            mockRepositorySession.clear()
        }
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
            mockRepositoryRuntime.isAuthenticatedFlow()
            mockRepositorySession.clear()
        }
    }
}