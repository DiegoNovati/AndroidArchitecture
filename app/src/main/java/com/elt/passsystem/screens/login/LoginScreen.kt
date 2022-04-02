package com.elt.passsystem.screens.login

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.elt.passsystem.R
import com.elt.passsystem.extensions.toErrorMessage
import com.elt.passsystem.ui.theme.AndroidArchitectureTheme
import com.elt.passsystem.widgets.ButtonRoundedEdgesPrimary
import com.elt.passsystem.widgets.ButtonRoundedEdgesSecondary
import com.elt.passsystem.widgets.TextBody
import com.elt.passsystem.widgets.TextError

@Composable
fun LoginScreen(
    viewModel: LoginViewModel = hiltViewModel(),
) {
    var password by remember { mutableStateOf("") }

    val username: String
    val enabled: Boolean
    val connected: Boolean
    var errorMessage = ""
    viewModel.state.observeAsState(LoginViewModel.State()).value.apply {
        username = this.username
        enabled = !this.loggingIn
        connected = this.connected
        this.authenticationLoginFailure?.let {
            errorMessage = it.toErrorMessage()
        }
    }

    val focusManager = LocalFocusManager.current

    LoginScreenUI(
        username = username,
        password = password,
        error = errorMessage,
        enabled = enabled,
        connected = connected,
        onUsernameChanged = { viewModel.updateUsername(it) },
        onPasswordChanged = {
            password = it
            viewModel.resetError()
        },
        onLogin = { viewModel.login(password) },
        onReset = {
            password = ""
            focusManager.clearFocus()
            viewModel.resetData()
        }
    )
}

@Composable
fun LoginScreenUI(
    username: String,
    password: String,
    enabled: Boolean,
    connected: Boolean,
    error: String,
    onUsernameChanged: (String) -> Unit,
    onPasswordChanged: (String) -> Unit,
    onLogin: () -> Unit,
    onReset: () -> Unit
) {
    val connectedText = if (!connected) stringResource(id = R.string.connectionMissing) else ""
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        modifier = Modifier.testTag(LoginScreenTestTag.TitleText.name),
                        text = stringResource(id = R.string.loginTitle, connectedText),
                    )
                }
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState())
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center,
            ) {
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag(LoginScreenTestTag.UsernameTextField.name),
                    label = {
                        TextBody(
                            text = stringResource(id = R.string.loginInputUsername),
                        )
                    },
                    value = username,
                    enabled = enabled,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    onValueChange = { onUsernameChanged(it) }
                )
                OutlinedTextField(
                    modifier = Modifier
                        .padding(bottom = 16.dp)
                        .fillMaxWidth()
                        .testTag(LoginScreenTestTag.PasswordTextField.name),
                    label = {
                        TextBody(
                            text = stringResource(id = R.string.loginInputPassword)
                        )
                    },
                    value = password,
                    enabled = enabled,
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    onValueChange = { onPasswordChanged(it) }
                )
                ButtonRoundedEdgesPrimary(modifier = Modifier
                    .padding(bottom = 16.dp)
                    .fillMaxWidth()
                    .testTag(LoginScreenTestTag.LoginButton.name),
                    stringId = R.string.loginLoginButton,
                    enabled = enabled && username.isNotEmpty() && password.isNotEmpty(),
                    onClick = { onLogin() })
                ButtonRoundedEdgesSecondary(
                    modifier = Modifier
                        .padding(bottom = 16.dp)
                        .fillMaxWidth()
                        .testTag(LoginScreenTestTag.ResetButton.name),
                    stringId = R.string.loginLoginReset,
                    enabled = enabled,
                    onClick = { onReset() }
                )
                TextError(
                    modifier = Modifier
                        .padding(bottom = 16.dp)
                        .fillMaxWidth()
                        .testTag(LoginScreenTestTag.ErrorText.name),
                    text = error,
                )
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun LoginScreenUIEnabledPreview() {
    AndroidArchitectureTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colors.background
        ) {
            LoginScreenUI(
                username = "user01",
                password = "password",
                error = "",
                enabled = true,
                connected = true,
                onUsernameChanged = {},
                onPasswordChanged = {},
                onLogin = {},
                onReset = {},
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
fun LoginScreenUIDisabledPreview() {
    AndroidArchitectureTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colors.background
        ) {
            LoginScreenUI(
                username = "user01",
                password = "password",
                error = "",
                enabled = false,
                connected = true,
                onUsernameChanged = {},
                onPasswordChanged = {},
                onLogin = {},
                onReset = {},
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
fun LoginScreenUIDisconnectedPreview() {
    AndroidArchitectureTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colors.background
        ) {
            LoginScreenUI(
                username = "user01",
                password = "password",
                error = "",
                enabled = true,
                connected = false,
                onUsernameChanged = {},
                onPasswordChanged = {},
                onLogin = {},
                onReset = {},
            )
        }
    }
}