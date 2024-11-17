package uk.co.itmms.androidArchitecture.data.repositories

import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import uk.co.itmms.androidArchitecture.data.BaseDataTest

class RepositoryRuntimeTest : BaseDataTest() {

    private lateinit var repositoryRuntime: RepositoryRuntime

    private val timeoutSeconds = 1

    @Before
    fun setUp() {
        repositoryRuntime = RepositoryRuntime(
            timeoutSeconds = timeoutSeconds,
        )
    }

    @Test
    fun `testing authenticated`() = runBlocking {
        assertFalse(repositoryRuntime.isAuthenticatedFlow().value)

        repositoryRuntime.setAuthenticated(true)

        assertTrue(repositoryRuntime.isAuthenticatedFlow().value)

        repositoryRuntime.setAuthenticated(false)

        assertFalse(repositoryRuntime.isAuthenticatedFlow().value)
    }

    @Test
    fun `testing fake authentication expire`() = runBlocking {
        assertFalse(repositoryRuntime.isAuthenticatedFlow().value)

        repositoryRuntime.setAuthenticated(true)

        assertTrue(repositoryRuntime.isAuthenticatedFlow().value)

        repositoryRuntime.setFakeAuthenticationExpire(true)

        delay(timeoutSeconds * 1000L + 500)

        assertFalse(repositoryRuntime.isAuthenticatedFlow().value)
    }
}