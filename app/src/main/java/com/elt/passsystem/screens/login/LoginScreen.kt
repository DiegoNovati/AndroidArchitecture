package com.elt.passsystem.screens.login

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Surface
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
import com.elt.passsystem.widgets.*

@Composable
fun LoginScreen(
    viewModel: LoginViewModel = hiltViewModel(),
) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val focusManager = LocalFocusManager.current

    val error = when (val stateValue =
        viewModel.state.observeAsState(LoginViewModel.LoginState.NoError).value) {
        LoginViewModel.LoginState.NoError -> ""
        LoginViewModel.LoginState.LoginError -> stringResource(id = R.string.loginErrorLoginError)
        LoginViewModel.LoginState.BackendProblems -> stringResource(id = R.string.loginErrorBackendProblems)
        LoginViewModel.LoginState.ConnectionProblems -> stringResource(id = R.string.loginErrorConnectionProblems)
        is LoginViewModel.LoginState.UnexpectedProblems -> stateValue.error
    }

    LoginScreenUI(
        username = username,
        password = password,
        error = error,
        onUsernameChanged = {
            username = it
            viewModel.resetError()
        },
        onPasswordChanged = {
            password = it
            viewModel.resetError()
        },
        onLogin = {
            viewModel.resetError()
            viewModel.login(
                username = username,
                password = password,
            )
        },
        onReset = {
            username = ""
            password = ""
            focusManager.clearFocus()
            viewModel.resetError()
        }
    )
}

@Composable
fun LoginScreenUI(
    username: String,
    password: String,
    error: String,
    onUsernameChanged: (String) -> Unit,
    onPasswordChanged: (String) -> Unit,
    onLogin: () -> Unit,
    onReset: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize(),
        //horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        TextTitleScreen(
            text = stringResource(id = R.string.loginTitle),
            modifier = Modifier.padding(
                start = 16.dp,
                end = 16.dp,
            )
        )
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
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                onValueChange = { onPasswordChanged(it) }
            )
            ButtonRoundedEdgesPrimary(
                modifier = Modifier
                    .padding(bottom = 16.dp)
                    .fillMaxWidth(),
                enabled = username.isNotEmpty() && password.isNotEmpty(),
                stringId = R.string.loginLoginButton,
                onClick = { onLogin() }
            )
            ButtonRoundedEdgesSecondary(
                modifier = Modifier
                    .padding(bottom = 16.dp)
                    .fillMaxWidth(),
                stringId = R.string.loginLoginReset,
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

@Composable
@Preview(showBackground = true)
fun LoginScreenUIPreview() {
    AndroidArchitectureTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colors.background
        ) {
            LoginScreenUI(
                username = "user01",
                password = "password",
                error = "",
                onUsernameChanged = {},
                onPasswordChanged = {},
                onLogin = {},
                onReset = {},
            )
        }
    }
}