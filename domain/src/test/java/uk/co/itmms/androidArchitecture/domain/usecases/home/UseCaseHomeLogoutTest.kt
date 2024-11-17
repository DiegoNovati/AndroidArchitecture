package uk.co.itmms.androidArchitecture.domain.usecases.home

import arrow.core.Either
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Test
import uk.co.itmms.androidArchitecture.domain.BaseDomainTest
import uk.co.itmms.androidArchitecture.domain.repositories.IRepositoryAuthentication
import uk.co.itmms.androidArchitecture.domain.repositories.IRepositorySession
import uk.co.itmms.androidArchitecture.domain.usecases.NoParams

class UseCaseHomeLogoutTest: BaseDomainTest() {

    @MockK
    private lateinit var mockRepositoryAuthentication: IRepositoryAuthentication

    @MockK
    private lateinit var mockRepositorySession: IRepositorySession

    @InjectMockKs
    private lateinit var useCaseHomeLogout: UseCaseHomeLogout

    @After
    fun tearDown() {
        confirmVerified(mockRepositoryAuthentication, mockRepositorySession)
    }

    @Test
    fun `the run always return a right value`() = runBlocking {
        val actual = useCaseHomeLogout.run(NoParams)

        assertTrue(actual.isRight())
        assertEquals(Either.Right(Unit), actual)

        coVerify(exactly = 1) {
            mockRepositoryAuthentication.logout()
            mockRepositorySession.clear()
        }
    }
}