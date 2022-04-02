package com.elt.passsystem.screens.login

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import arrow.core.Either
import com.elt.passsystem.BaseAppTest
import com.elt.passsystem.domain.entities.AuthenticationLoginFailure
import com.elt.passsystem.domain.entities.LoginResult
import com.elt.passsystem.domain.entities.UnexpectedError
import com.elt.passsystem.domain.usecases.authentication.UseCaseAuthenticationLogin
import com.elt.passsystem.services.IServiceActivityBus
import com.elt.passsystem.services.IServiceNavigation
import com.elt.passsystem.services.Route
import com.elt.passsystem.state.GlobalState
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import junit.framework.TestCase.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

@ExperimentalCoroutinesApi
class LoginViewModelTest: BaseAppTest() {

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @MockK
    private lateinit var mockServiceNavigation: IServiceNavigation

    @MockK
    private lateinit var mockServiceActivityBus: IServiceActivityBus

    @MockK
    private lateinit var mockUseCaseAuthenticationLogin: UseCaseAuthenticationLogin

    @MockK
    private lateinit var mockGlobalState: GlobalState

    private lateinit var loginViewModel: LoginViewModel

    @Before
    fun setUp() {
        // Required by init
        every { mockServiceActivityBus.getConnectivityState() } returns true

        loginViewModel = LoginViewModel(
            mockServiceNavigation, mockServiceActivityBus, mockUseCaseAuthenticationLogin,
            mockGlobalState
        )
    }

    @Test
    fun `testing initialization`() {
        val connected = false
        every { mockServiceActivityBus.getConnectivityState() } returns connected

        loginViewModel = LoginViewModel(
            mockServiceNavigation, mockServiceActivityBus, mockUseCaseAuthenticationLogin,
            mockGlobalState
        )

        val loginState = loginViewModel.state.value
        assertEquals(connected, loginState?.connected)

        verify {
            mockServiceActivityBus.registerConnectivityStateChanged(any())
            mockServiceActivityBus.getConnectivityState()
        }
    }

    @Test
    fun `testing updateUsername`() {
        val username = "myUsername"

        loginViewModel.updateUsername(username)

        val loginState = loginViewModel.state.value
        assertEquals(username, loginState?.username)
    }

    @Test
    fun `testing resetData`() {
        loginViewModel.resetData()

        val loginState = loginViewModel.state.value!!
        assertEquals("", loginState.username)
        assertNull(loginState.authenticationLoginFailure)
    }

    @Test
    fun `testing resetError`() {
        loginViewModel.resetError()

        val loginState = loginViewModel.state.value!!
        assertNull(loginState.authenticationLoginFailure)
    }

    @Test
    fun `testing successful login`() {
        Dispatchers.setMain(Dispatchers.Unconfined)

        val password = "myPassword"
        val officeBid = "officeBid"
        val result: Either<AuthenticationLoginFailure, LoginResult> = Either.Right(LoginResult(officeBid))
        every { mockUseCaseAuthenticationLogin.invoke(any(), any(), any()) } answers {
            thirdArg<(Either<AuthenticationLoginFailure, LoginResult>) -> Unit>().invoke(result)
            this.value
        }

        loginViewModel.login(password)

        assertFalse(loginViewModel.state.value!!.loggingIn)

        verify(exactly = 1) {
            mockUseCaseAuthenticationLogin.invoke(any(), any(), any())
            mockServiceNavigation.open(Route.Home)
        }
        confirmVerified(mockUseCaseAuthenticationLogin)
        confirmVerified(mockServiceNavigation)
    }

    @Test
    fun `testing login failure`() {
        Dispatchers.setMain(Dispatchers.Unconfined)

        val password = "myPassword"
        val result: Either<AuthenticationLoginFailure, LoginResult> = Either.Left(AuthenticationLoginFailure.LoginError)
        every { mockUseCaseAuthenticationLogin.invoke(any(), any(), any()) } answers {
            thirdArg<(Either<AuthenticationLoginFailure, LoginResult>) -> Unit>().invoke(result)
            this.value
        }

        loginViewModel.login(password)
        val actual = loginViewModel.state.value!!

        assertFalse(actual.loggingIn)
        assertEquals(AuthenticationLoginFailure.LoginError, actual.authenticationLoginFailure)

        verify(exactly = 1) {
            mockUseCaseAuthenticationLogin.invoke(any(), any(), any())
        }
        confirmVerified(mockUseCaseAuthenticationLogin)
        confirmVerified(mockServiceNavigation)
    }

    @Test
    fun `testing connection problems during login`() {
        Dispatchers.setMain(Dispatchers.Unconfined)

        val password = "myPassword"
        val result: Either<AuthenticationLoginFailure, LoginResult> = Either.Left(AuthenticationLoginFailure.ConnectionProblems)
        every { mockUseCaseAuthenticationLogin.invoke(any(), any(), any()) } answers {
            thirdArg<(Either<AuthenticationLoginFailure, LoginResult>) -> Unit>().invoke(result)
            this.value
        }

        loginViewModel.login(password)
        val actual = loginViewModel.state.value!!

        assertFalse(actual.loggingIn)
        assertEquals(AuthenticationLoginFailure.ConnectionProblems, actual.authenticationLoginFailure)

        verify(exactly = 1) {
            mockUseCaseAuthenticationLogin.invoke(any(), any(), any())
        }
        confirmVerified(mockUseCaseAuthenticationLogin)
        confirmVerified(mockServiceNavigation)
    }

    @Test
    fun `testing backend problems during login`() {
        Dispatchers.setMain(Dispatchers.Unconfined)

        val password = "myPassword"
        val result: Either<AuthenticationLoginFailure, LoginResult> = Either.Left(AuthenticationLoginFailure.BackendProblems)
        every { mockUseCaseAuthenticationLogin.invoke(any(), any(), any()) } answers {
            thirdArg<(Either<AuthenticationLoginFailure, LoginResult>) -> Unit>().invoke(result)
            this.value
        }

        loginViewModel.login(password)
        val actual = loginViewModel.state.value!!

        assertFalse(actual.loggingIn)
        assertEquals(AuthenticationLoginFailure.BackendProblems, actual.authenticationLoginFailure)

        verify(exactly = 1) {
            mockUseCaseAuthenticationLogin.invoke(any(), any(), any())
        }
        confirmVerified(mockUseCaseAuthenticationLogin)
        confirmVerified(mockServiceNavigation)
    }

    @Test
    fun `testing unexpected problems during login`() {
        Dispatchers.setMain(Dispatchers.Unconfined)

        val password = "myPassword"
        val exception = RuntimeException("something unexpected happened")
        val result: Either<AuthenticationLoginFailure, LoginResult> = Either.Left(UnexpectedError(exception))
        every { mockUseCaseAuthenticationLogin.invoke(any(), any(), any()) } answers {
            thirdArg<(Either<AuthenticationLoginFailure, LoginResult>) -> Unit>().invoke(result)
            this.value
        }

        loginViewModel.login(password)
        val actual = loginViewModel.state.value!!

        assertFalse(actual.loggingIn)
        assertEquals(UnexpectedError(exception), actual.authenticationLoginFailure)

        verify(exactly = 1) {
            mockUseCaseAuthenticationLogin.invoke(any(), any(), any())
        }
        confirmVerified(mockUseCaseAuthenticationLogin)
        confirmVerified(mockServiceNavigation)
    }
}