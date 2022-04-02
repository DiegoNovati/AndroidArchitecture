package com.elt.passsystem.screens.login

import androidx.compose.ui.test.junit4.createComposeRule
import com.elt.passsystem.extensions.*
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.runBlocking
import org.junit.Rule
import org.junit.Test

class LoginScreenUITest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun testing_initial_state() {
        composeTestRule.setContent {
            LoginScreenUI(
                username = "",
                password = "",
                enabled = true,
                connected = true,
                error = "",
                onUsernameChanged = {},
                onPasswordChanged = {},
                onLogin = {},
                onReset = {}
            )
        }

        LoginScreenTestTag.UsernameTextField.isEnabledAndEmpty(composeTestRule)
        LoginScreenTestTag.PasswordTextField.isEnabledAndEmpty(composeTestRule)
        LoginScreenTestTag.LoginButton.isNotEnabled(composeTestRule)
        LoginScreenTestTag.ResetButton.isEnabled(composeTestRule)
        LoginScreenTestTag.ErrorText.isEmpty(composeTestRule)
    }

    @Test
    fun testing_with_username_and_password() {
        val username = "myUsername"
        composeTestRule.setContent {
            LoginScreenUI(
                username = username,
                password = "myPassword",
                enabled = true,
                connected = true,
                error = "",
                onUsernameChanged = {},
                onPasswordChanged = {},
                onLogin = {},
                onReset = {}
            )
        }

        LoginScreenTestTag.UsernameTextField.isEnabledAndNotEmpty(composeTestRule, username)
        LoginScreenTestTag.PasswordTextField.isEnabledAndNotEmpty(composeTestRule, "••••••••••")
        LoginScreenTestTag.LoginButton.isEnabled(composeTestRule)
        LoginScreenTestTag.ResetButton.isEnabled(composeTestRule)
        LoginScreenTestTag.ErrorText.isEmpty(composeTestRule)
    }

    @Test
    fun testing_with_username_and_password_and_disabled() {
        val username = "myUsername"
        composeTestRule.setContent {
            LoginScreenUI(
                username = username,
                password = "myPassword",
                enabled = false,
                connected = true,
                error = "",
                onUsernameChanged = {},
                onPasswordChanged = {},
                onLogin = {},
                onReset = {}
            )
        }

        LoginScreenTestTag.UsernameTextField.isNotEnabled(composeTestRule)
        LoginScreenTestTag.PasswordTextField.isNotEnabled(composeTestRule)
        LoginScreenTestTag.LoginButton.isNotEnabled(composeTestRule)
        LoginScreenTestTag.ResetButton.isNotEnabled(composeTestRule)
        LoginScreenTestTag.ErrorText.isEmpty(composeTestRule)
    }

    @Test
    fun testing_when_not_connected() {
        composeTestRule.setContent {
            LoginScreenUI(
                username = "myUsername",
                password = "myPassword",
                enabled = true,
                connected = false,
                error = "",
                onUsernameChanged = {},
                onPasswordChanged = {},
                onLogin = {},
                onReset = {}
            )
        }

        LoginScreenTestTag.TitleText.hasText(composeTestRule, "Login - Disconnected")
    }

    @Test
    fun testing_when_has_error() {
        val errorMessage = "This is an error message"
        composeTestRule.setContent {
            LoginScreenUI(
                username = "myUsername",
                password = "myPassword",
                enabled = true,
                connected = true,
                error = errorMessage,
                onUsernameChanged = {},
                onPasswordChanged = {},
                onLogin = {},
                onReset = {}
            )
        }

        LoginScreenTestTag.ErrorText.hasText(composeTestRule, errorMessage)
    }

    @Test
    fun testing_onUsernameChanged() = runBlocking {
        var onUsernameChangedCalled = false

        composeTestRule.setContent {
            LoginScreenUI(
                username = "",
                password = "",
                enabled = true,
                connected = true,
                error = "",
                onUsernameChanged = {
                    onUsernameChangedCalled = true
                },
                onPasswordChanged = {},
                onLogin = {},
                onReset = {}
            )
        }

        LoginScreenTestTag.UsernameTextField.type(composeTestRule, "user12345")

        assertTrue(onUsernameChangedCalled)
    }

    @Test
    fun testing_onPasswordChanged() = runBlocking {
        var onPasswordChangedCalled = false

        composeTestRule.setContent {
            LoginScreenUI(
                username = "",
                password = "",
                enabled = true,
                connected = true,
                error = "",
                onUsernameChanged = {},
                onPasswordChanged = {
                    onPasswordChangedCalled = true
                },
                onLogin = {},
                onReset = {}
            )
        }

        LoginScreenTestTag.PasswordTextField.type(composeTestRule, "password12345")

        assertTrue(onPasswordChangedCalled)
    }

    @Test
    fun testing_onLogin() = runBlocking {
        var onLoginCalled = false

        composeTestRule.setContent {
            LoginScreenUI(
                username = "user12345",
                password = "password12345",
                enabled = true,
                connected = true,
                error = "",
                onUsernameChanged = {},
                onPasswordChanged = {},
                onLogin = {
                    onLoginCalled = true
                },
                onReset = {}
            )
        }

        LoginScreenTestTag.LoginButton.tap(composeTestRule)

        assertTrue(onLoginCalled)
    }

    @Test
    fun testing_onReset() = runBlocking {
        var onResetCalled = false

        composeTestRule.setContent {
            LoginScreenUI(
                username = "user12345",
                password = "password12345",
                enabled = true,
                connected = true,
                error = "",
                onUsernameChanged = {},
                onPasswordChanged = {},
                onLogin = {},
                onReset = {
                    onResetCalled = true
                }
            )
        }

        LoginScreenTestTag.ResetButton.tap(composeTestRule)

        assertTrue(onResetCalled)
    }
}