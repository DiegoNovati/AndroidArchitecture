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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.elt.passsystem.R
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
    val stateValue = viewModel.state.observeAsState(
        LoginViewModel.LoginState(
            connected = true,
            loggingIn = false,
            username = "",
            errorType = LoginViewModel.ErrorType.NoError,
        )
    ).value

    val focusManager = LocalFocusManager.current

    val errorMessage = when (stateValue.errorType) {
        LoginViewModel.ErrorType.NoError -> ""
        LoginViewModel.ErrorType.ConnectionError -> stringResource(id = R.string.loginErrorConnectionProblems)
        LoginViewModel.ErrorType.BackendError -> stringResource(id = R.string.loginErrorBackendProblems)
        LoginViewModel.ErrorType.LoginError -> stringResource(id = R.string.loginErrorLoginError)
        is LoginViewModel.ErrorType.UnexpectedError -> stateValue.errorType.error
    }

    LoginScreenUI(
        username = stateValue.username,
        password = password,
        error = errorMessage,
        enabled = !stateValue.loggingIn,
        connected = stateValue.connected,
        onUsernameChanged = {
            viewModel.updateUsername(it)
        },
        onPasswordChanged = {
            password = it
            viewModel.resetError()
        },
        onLogin = {
            viewModel.resetError()
            viewModel.login(
                password = password,
            )
        },
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
                title = { Text(text = stringResource(id = R.string.loginTitle, connectedText)) }
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
                        .fillMaxWidth(),
                    label = {
                        TextBody(
                            text = stringResource(id = R.string.loginInputUsername)
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
                        .fillMaxWidth(),
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
                ButtonRoundedEdgesPrimary(
                    modifier = Modifier
                        .padding(bottom = 16.dp)
                        .fillMaxWidth(),
                    stringId = R.string.loginLoginButton,
                    enabled = enabled && username.isNotEmpty() && password.isNotEmpty(),
                    onClick = { onLogin() }
                )
                ButtonRoundedEdgesSecondary(
                    modifier = Modifier
                        .padding(bottom = 16.dp)
                        .fillMaxWidth(),
                    stringId = R.string.loginLoginReset,
                    enabled = enabled,
                    onClick = { onReset() }
                )
                TextError(
                    modifier = Modifier
                        .padding(bottom = 16.dp)
                        .fillMaxWidth(),
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