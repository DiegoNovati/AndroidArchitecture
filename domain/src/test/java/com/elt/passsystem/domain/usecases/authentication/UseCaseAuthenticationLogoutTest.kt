package com.elt.passsystem.domain.usecases.authentication

import arrow.core.Either
import com.elt.passsystem.domain.BaseDomainTest
import com.elt.passsystem.domain.repositories.IRepositoryAnalytics
import com.elt.passsystem.domain.repositories.IRepositoryAuthentication
import com.elt.passsystem.domain.repositories.IRepositoryLogger
import com.elt.passsystem.domain.usecases.NoParams
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.impl.annotations.MockK
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class UseCaseAuthenticationLogoutTest: BaseDomainTest() {

    @MockK
    private lateinit var mockRepositoryLogger: IRepositoryLogger

    @MockK
    private lateinit var mockRepositoryAnalytics: IRepositoryAnalytics

    @MockK
    private lateinit var mockRepositoryAuthentication: IRepositoryAuthentication

    private lateinit var useCaseAuthenticationLogout: UseCaseAuthenticationLogout

    @Before
    fun setUp() {
        useCaseAuthenticationLogout = UseCaseAuthenticationLogout(
            mockRepositoryLogger, mockRepositoryAnalytics, mockRepositoryAuthentication,
        )
    }

    @Test
    fun `the run always return a right value`() = runBlocking {
        val actual = useCaseAuthenticationLogout.run(NoParams)

        assertTrue(actual.isRight())
        assertEquals(Either.Right(Unit), actual)

        coVerify(exactly = 1) {
            mockRepositoryAuthentication.logout()
        }
        confirmVerified(mockRepositoryAuthentication)
    }
}