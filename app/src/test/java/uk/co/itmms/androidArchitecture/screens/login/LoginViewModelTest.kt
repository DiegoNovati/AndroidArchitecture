package uk.co.itmms.androidArchitecture.screens.login

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import arrow.core.Either
import arrow.core.left
import arrow.core.right
import uk.co.itmms.androidArchitecture.BaseAppTest
import uk.co.itmms.androidArchitecture.domain.failures.FailureLogin
import uk.co.itmms.androidArchitecture.domain.failures.UnexpectedError
import uk.co.itmms.androidArchitecture.domain.usecases.login.UseCaseLoginLogin
import uk.co.itmms.androidArchitecture.domain.usecases.login.UseCaseLoginMonitor
import uk.co.itmms.androidArchitecture.services.IServiceNavigation
import uk.co.itmms.androidArchitecture.services.Route
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.flow
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
    private lateinit var mockUseCaseLoginMonitor: UseCaseLoginMonitor

    @MockK
    private lateinit var mockUseCaseLoginLogin: UseCaseLoginLogin

    private lateinit var loginViewModel: LoginViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(Dispatchers.Unconfined)  // Required by 'viewModelScope.launch'

        // Required by init
        val result: Either<FailureLogin, UseCaseLoginMonitor.Result> = UseCaseLoginMonitor.Result(update = flow {}).right()
        every { mockUseCaseLoginMonitor.invoke(any(), any(), any()) } answers {
            thirdArg<(Either<FailureLogin, UseCaseLoginMonitor.Result>) -> Unit>().invoke(result)
            Job()
        }

        loginViewModel = LoginViewModel(
            mockServiceNavigation, mockUseCaseLoginMonitor, mockUseCaseLoginLogin,
        )
    }

    @Test
    fun `testing that any change of connection value updates the state`() {
        val connected = false

        val result: Either<FailureLogin, UseCaseLoginMonitor.Result> = UseCaseLoginMonitor.Result(
            update = flow { emit(UseCaseLoginMonitor.Update(updateType = UseCaseLoginMonitor.UpdateType.Connected, value = connected)) }).right()
        every { mockUseCaseLoginMonitor.invoke(any(), any(), any()) } answers {
            thirdArg<(Either<FailureLogin, UseCaseLoginMonitor.Result>) -> Unit>().invoke(result)
            Job()
        }

        loginViewModel = LoginViewModel(
            mockServiceNavigation, mockUseCaseLoginMonitor, mockUseCaseLoginLogin,
        )

        val loginState = loginViewModel.state.value
        assertEquals(connected, loginState?.data?.connected)

        verify(exactly = 2) {
            mockUseCaseLoginMonitor.invoke(any(), any(), any())
        }
        confirmVerified(mockServiceNavigation, mockUseCaseLoginLogin, mockUseCaseLoginMonitor, mockServiceNavigation)
    }

    @Test
    fun `testing that when the user is not authenticated anymore it pops back to the login screen`() {
        val authenticated = false

        every { mockServiceNavigation.getCurrentRoute() } returnsMany listOf(Route.Home, Route.Login)

        val result: Either<FailureLogin, UseCaseLoginMonitor.Result> = UseCaseLoginMonitor.Result(
            update = flow { emit(UseCaseLoginMonitor.Update(updateType = UseCaseLoginMonitor.UpdateType.Authentication, value = authenticated)) }).right()
        every { mockUseCaseLoginMonitor.invoke(any(), any(), any()) } answers {
            thirdArg<(Either<FailureLogin, UseCaseLoginMonitor.Result>) -> Unit>().invoke(result)
            Job()
        }

        loginViewModel = LoginViewModel(
            mockServiceNavigation, mockUseCaseLoginMonitor, mockUseCaseLoginLogin,
        )

        verify(exactly = 2) {
            mockUseCaseLoginMonitor.invoke(any(), any(), any())
            mockServiceNavigation.getCurrentRoute()
        }
        verify(exactly = 1) {
            mockServiceNavigation.popBack()
        }
        confirmVerified(mockServiceNavigation, mockUseCaseLoginLogin, mockUseCaseLoginMonitor, mockServiceNavigation)
    }

    @Test
    fun `testing doEvent when eventType is UpdateData - tha state is updated`() {
        val newUsername = "myUsername"
        val newPassword = "myPassword"
        val newStateData = LoginViewModel.StateData().copy(
            username = newUsername,
            password = newPassword,
        )

        loginViewModel.doEvent(LoginViewModel.EventType.UpdateData(stateData = newStateData))

        val actual = loginViewModel.state.value

        assertEquals(newUsername, actual?.data?.username)
        assertEquals(newPassword, actual?.data?.password)
    }

    @Test
    fun `testing doEvent when eventType is ResetData - the state is updated`() {
        val beforeStateData = LoginViewModel.StateData().copy(
            username = "myUsername",
            password = "myPassword",
            loggingIn = true,
        )
        loginViewModel.doEvent(LoginViewModel.EventType.UpdateData(stateData = beforeStateData))
        loginViewModel.doEvent(LoginViewModel.EventType.ResetData)

        val actual = loginViewModel.state.value

        assertEquals("", actual?.data?.username)
        assertEquals("", actual?.data?.password)
        assertEquals(false, actual?.data?.loggingIn)
    }

    @Test
    fun `testing doEvent when eventType is Login and the login is successful`() {
        val result: Either<FailureLogin, Unit> = Unit.right()
        every { mockUseCaseLoginLogin.invoke(any(), any(), any()) } answers {
            thirdArg<(Either<FailureLogin, Unit>) -> Unit>().invoke(result)
            Job()
        }

        loginViewModel.doEvent(LoginViewModel.EventType.Login)

        val actual = loginViewModel.state.value

        assertEquals("", actual?.data?.password)
        assertEquals(false, actual?.data?.loggingIn)

        verify(exactly = 1) {
            mockUseCaseLoginMonitor.invoke(any(), any(), any())
            mockUseCaseLoginLogin.invoke(any(), any(), any())
            mockServiceNavigation.open(Route.Home)
        }
        confirmVerified(mockServiceNavigation, mockUseCaseLoginLogin, mockUseCaseLoginMonitor, mockServiceNavigation)
    }

    @Test
    fun `testing doEvent when eventType is Login and the login fails`() {
        val result: Either<FailureLogin, Unit> = FailureLogin.LoginError.left()
        every { mockUseCaseLoginLogin.invoke(any(), any(), any()) } answers {
            thirdArg<(Either<FailureLogin, Unit>) -> Unit>().invoke(result)
            Job()
        }

        loginViewModel.doEvent(LoginViewModel.EventType.Login)

        val actual = loginViewModel.state.value

        assertEquals(false, actual?.data?.loggingIn)
        assertEquals(FailureLogin.LoginError, actual?.failureLogin)

        verify(exactly = 1) {
            mockUseCaseLoginMonitor.invoke(any(), any(), any())
            mockUseCaseLoginLogin.invoke(any(), any(), any())
        }
        confirmVerified(mockServiceNavigation, mockUseCaseLoginLogin, mockUseCaseLoginMonitor, mockServiceNavigation)
    }

    @Test
    fun `testing doEvent when eventType is Login and the login raises an exception`() {
        val exception = RuntimeException("something unexpected happened")
        val result: Either<FailureLogin, Unit> = Either.Left(UnexpectedError(exception))
        every { mockUseCaseLoginLogin.invoke(any(), any(), any()) } answers {
            thirdArg<(Either<FailureLogin, Unit>) -> Unit>().invoke(result)
            Job()
        }

        loginViewModel.doEvent(LoginViewModel.EventType.Login)

        val actual = loginViewModel.state.value

        assertEquals(false, actual?.data?.loggingIn)
        assertEquals(UnexpectedError(exception), actual?.failureLogin)

        verify(exactly = 1) {
            mockUseCaseLoginMonitor.invoke(any(), any(), any())
            mockUseCaseLoginLogin.invoke(any(), any(), any())
        }
        confirmVerified(mockServiceNavigation, mockUseCaseLoginLogin, mockUseCaseLoginMonitor, mockServiceNavigation)
    }
}