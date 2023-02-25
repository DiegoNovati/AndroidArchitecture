package uk.co.itmms.androidArchitecture.domain.usecases.home

import arrow.core.Either
import uk.co.itmms.androidArchitecture.domain.BaseDomainTest
import uk.co.itmms.androidArchitecture.domain.repositories.IRepositoryAuthentication
import uk.co.itmms.androidArchitecture.domain.repositories.IRepositoryRuntime
import uk.co.itmms.androidArchitecture.domain.usecases.NoParams
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.impl.annotations.MockK
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class UseCaseHomeLogoutTest: BaseDomainTest() {

    @MockK
    private lateinit var mockRepositoryAuthentication: IRepositoryAuthentication

    @MockK
    private lateinit var mockRepositoryRuntime: IRepositoryRuntime

    private lateinit var useCaseHomeLogout: UseCaseHomeLogout

    @Before
    fun setUp() {
        useCaseHomeLogout = UseCaseHomeLogout(
            mockRepositoryDevelopmentLogger, mockRepositoryDevelopmentAnalytics, mockRepositoryAuthentication,
            mockRepositoryRuntime,
        )
    }

    @Test
    fun `the run always return a right value`() = runBlocking {
        val actual = useCaseHomeLogout.run(NoParams)

        assertTrue(actual.isRight())
        assertEquals(Either.Right(Unit), actual)

        coVerify(exactly = 1) {
            mockRepositoryAuthentication.logout()
            mockRepositoryRuntime.clear()
        }
        confirmVerified(mockRepositoryAuthentication, mockRepositoryRuntime)
    }
}