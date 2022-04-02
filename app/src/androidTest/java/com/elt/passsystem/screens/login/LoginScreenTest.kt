package com.elt.passsystem.screens.login

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.elt.passsystem.BaseAppAndroidTest
import com.elt.passsystem.domain.entities.AuthenticationLoginFailure
import com.elt.passsystem.extensions.hasText
import com.elt.passsystem.extensions.isEnabledAndNotEmpty
import com.elt.passsystem.extensions.tap
import com.elt.passsystem.extensions.type
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.Rule
import org.junit.Test

class LoginScreenTest: BaseAppAndroidTest() {

    @MockK
    private lateinit var mockLoginViewModel: LoginViewModel

    @MockK
    private lateinit var mockState: LiveData<LoginViewModel.State>

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun testing_properties() = runBlocking<Unit> {
        val username = "user12345"
        val state = MutableLiveData<LoginViewModel.State>()
        every { mockLoginViewModel.state } returns state
        launch(Dispatchers.Main) {
            state.value = LoginViewModel.State(
                connected = false,
                loggingIn = false,
                username = username,
                authenticationLoginFailure = AuthenticationLoginFailure.BackendProblems,
            )
        }

        composeTestRule.setContent {
            LoginScreen(mockLoginViewModel)
        }

        LoginScreenTestTag.TitleText.hasText(composeTestRule, "Login - Disconnected")
        LoginScreenTestTag.UsernameTextField.isEnabledAndNotEmpty(composeTestRule, username)
        LoginScreenTestTag.ErrorText.hasText(
            composeTestRule,
            "There were backend problems. Please retry in a couple of minutes"
        )
    }

    @Test
    fun testing_onUsernameChanged() {
        val username = "user12345"
        every { mockLoginViewModel.state } returns mockState

        composeTestRule.setContent {
            LoginScreen(mockLoginViewModel)
        }

        LoginScreenTestTag.UsernameTextField.type(composeTestRule, username)

        verify { mockLoginViewModel.updateUsername(username) }
    }

    @Test
    fun testing_onPasswordChanged() {
        val password = "myPassword"
        every { mockLoginViewModel.state } returns mockState

        composeTestRule.setContent {
            LoginScreen(mockLoginViewModel)
        }

        LoginScreenTestTag.PasswordTextField.type(composeTestRule, password)

        verify { mockLoginViewModel.resetError() }
    }

    @Test
    fun testing_onLogin() = runBlocking {
        val password = "thePassword"
        val state = MutableLiveData<LoginViewModel.State>()
        every { mockLoginViewModel.state } returns state
        launch(Dispatchers.Main) {
            state.value = LoginViewModel.State(
                connected = true,
                loggingIn = false,
                username = "user12345",
                authenticationLoginFailure = null,
            )
        }

        composeTestRule.setContent {
            LoginScreen(mockLoginViewModel)
        }

        LoginScreenTestTag.PasswordTextField.type(composeTestRule, password)
        LoginScreenTestTag.LoginButton.tap(composeTestRule)

        verify {
            mockLoginViewModel.resetError()
            mockLoginViewModel.login(password)
        }
    }

    @Test
    fun testing_onReset() {
        every { mockLoginViewModel.state } returns mockState

        composeTestRule.setContent {
            LoginScreen(mockLoginViewModel)
        }

        LoginScreenTestTag.ResetButton.tap(composeTestRule)

        verify {
            mockLoginViewModel.resetData()
        }
    }
}