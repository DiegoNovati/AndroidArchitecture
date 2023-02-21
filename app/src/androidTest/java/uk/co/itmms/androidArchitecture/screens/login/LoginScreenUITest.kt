package uk.co.itmms.androidArchitecture.screens.login

import androidx.compose.ui.test.junit4.createComposeRule
import uk.co.itmms.androidArchitecture.domain.failures.FailureLogin
import uk.co.itmms.androidArchitecture.extensions.*
import junit.framework.TestCase.*
import kotlinx.coroutines.runBlocking
import org.junit.Rule
import org.junit.Test

class LoginScreenUITest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val username = "myUsername"
    private val password = "myPassword"

    @Test
    fun testing_initial_state() {
        composeTestRule.setContent {
            LoginScreenUI(
                state = state,
                onEvent = {},
            )
        }

        LoginScreenTestTag.UsernameTextField.isEnabledAndEmpty(composeTestRule)
        LoginScreenTestTag.PasswordTextField.isEnabledAndEmpty(composeTestRule)
        LoginScreenTestTag.FakeBackendSwitch.isChecked(composeTestRule)
        LoginScreenTestTag.FakeAuthorizationExpiring.isNotChecked(composeTestRule)
        LoginScreenTestTag.LoginButton.isNotEnabled(composeTestRule)
        LoginScreenTestTag.ResetButton.isEnabled(composeTestRule)
        LoginScreenTestTag.ErrorText.isEmpty(composeTestRule)
    }

    @Test
    fun testing_state_with_username_and_password() {
        composeTestRule.setContent {
            LoginScreenUI(
                state = state.copy(data = state.data.copy(
                    username = username,
                    password = password,
                )),
                onEvent = {},
            )
        }

        LoginScreenTestTag.UsernameTextField.isEnabledAndNotEmpty(composeTestRule, username)
        LoginScreenTestTag.PasswordTextField.isEnabledAndNotEmpty(composeTestRule, "••••••••••")
        LoginScreenTestTag.LoginButton.isEnabled(composeTestRule)
        LoginScreenTestTag.ResetButton.isEnabled(composeTestRule)
        LoginScreenTestTag.ErrorText.isEmpty(composeTestRule)
    }

    @Test
    fun testing_state_when_logging_in() {
        composeTestRule.setContent {
            LoginScreenUI(
                state = state.copy(data = state.data.copy(
                    username = username,
                    password = password,
                    loggingIn = true,
                )),
                onEvent = {},
            )
        }

        LoginScreenTestTag.UsernameTextField.isNotEnabled(composeTestRule)
        LoginScreenTestTag.PasswordTextField.isNotEnabled(composeTestRule)
        LoginScreenTestTag.LoginButton.isNotEnabled(composeTestRule)
        LoginScreenTestTag.ResetButton.isNotEnabled(composeTestRule)
        LoginScreenTestTag.ErrorText.isEmpty(composeTestRule)
    }

    @Test
    fun testing_state_when_connected() {
        composeTestRule.setContent {
            LoginScreenUI(
                state = state.copy(data = state.data.copy(connected = true)),
                onEvent = {},
            )
        }

        LoginScreenTestTag.TitleText.hasText(composeTestRule, "Login ")
    }



    @Test
    fun testing_state_when_not_connected() {
        composeTestRule.setContent {
            LoginScreenUI(
                state = state.copy(data = state.data.copy(connected = false)),
                onEvent = {},
            )
        }

        LoginScreenTestTag.TitleText.hasText(composeTestRule, "Login - Disconnected")
    }

    @Test
    fun testing_state_when_has_error() {
        val errorMessage = "Login failed. Please check the username/password"

        composeTestRule.setContent {
            LoginScreenUI(
                state = state.copy(failureLogin = FailureLogin.LoginError),
                onEvent = {},
            )
        }

        LoginScreenTestTag.ErrorText.hasText(composeTestRule, errorMessage)
    }

    @Test
    fun testing_username_changes() = runBlocking {
        var onEventCalled = false
        val newUsername = "user12345"
        var inputUsername = ""

        composeTestRule.setContent {
            LoginScreenUI(
                state = state,
                onEvent = {
                    Thread.sleep(100)
                    onEventCalled = it is LoginViewModel.EventType.UpdateData
                    inputUsername = (it as LoginViewModel.EventType.UpdateData).stateData.username
                }
            )
        }

        LoginScreenTestTag.UsernameTextField.type(composeTestRule, newUsername)

        assertTrue(onEventCalled)
        assertEquals(newUsername, inputUsername)
    }

    @Test
    fun testing_password_changes() = runBlocking {
        var onEventCalled = false
        val newPassword = "password12345"
        var inputPassword = ""

        composeTestRule.setContent {
            LoginScreenUI(
                state = state,
                onEvent = {
                    onEventCalled = it is LoginViewModel.EventType.UpdateData
                    inputPassword = (it as LoginViewModel.EventType.UpdateData).stateData.password
                }
            )
        }

        LoginScreenTestTag.PasswordTextField.type(composeTestRule, newPassword)

        assertTrue(onEventCalled)
        assertEquals(newPassword, inputPassword)
    }

    @Test
    fun testing_disabling_fake_backend() = runBlocking {
        var onEventCalled = false
        var newFakeBackend = true

        composeTestRule.setContent {
            LoginScreenUI(
                state = state,
                onEvent = {
                    onEventCalled = it is LoginViewModel.EventType.UpdateData
                    newFakeBackend = (it as LoginViewModel.EventType.UpdateData).stateData.fakeBackend
                }
            )
        }

        LoginScreenTestTag.FakeBackendSwitch.tap(composeTestRule)

        assertTrue(onEventCalled)
        assertFalse(newFakeBackend)
    }

    @Test
    fun testing_enabling_fake_authentication_expiring() = runBlocking {
        var onEventCalled = false
        var newFakeAuthenticationExpiring = true

        composeTestRule.setContent {
            LoginScreenUI(
                state = state,
                onEvent = {
                    onEventCalled = it is LoginViewModel.EventType.UpdateData
                    newFakeAuthenticationExpiring = (it as LoginViewModel.EventType.UpdateData).stateData.fakeAuthenticationExpire
                }
            )
        }

        LoginScreenTestTag.FakeAuthorizationExpiring.tap(composeTestRule)

        assertTrue(onEventCalled)
        assertTrue(newFakeAuthenticationExpiring)
    }

    @Test
    fun testing_clicking_login() = runBlocking {
        var onEventCalled = false

        composeTestRule.setContent {
            LoginScreenUI(
                state = state.copy(data = state.data.copy(
                    username = username,
                    password = password,
                )),
                onEvent = {
                    onEventCalled = it is LoginViewModel.EventType.Login
                }
            )
        }

        LoginScreenTestTag.LoginButton.tap(composeTestRule)

        assertTrue(onEventCalled)
    }

    @Test
    fun testing_clicking_reset() = runBlocking {
        var onEventCalled = false

        composeTestRule.setContent {
            LoginScreenUI(
                state = state,
                onEvent = {
                    onEventCalled = it is LoginViewModel.EventType.ResetData
                }
            )
        }

        LoginScreenTestTag.ResetButton.tap(composeTestRule)

        assertTrue(onEventCalled)
    }
}

private val state: LoginViewModel.State by lazy {
    LoginViewModel.State(
        data = stateData,
        failureLogin = null,
    )
}

private val stateData: LoginViewModel.StateData by lazy {
    LoginViewModel.StateData(
        username = "",
        loggingIn = false,
        password = "",
        fakeBackend = true,
        connected = true,
    )
}