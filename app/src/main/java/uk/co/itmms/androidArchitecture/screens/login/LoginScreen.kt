package uk.co.itmms.androidArchitecture.screens.login

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import uk.co.itmms.androidArchitecture.R
import uk.co.itmms.androidArchitecture.domain.failures.FailureLogin
import uk.co.itmms.androidArchitecture.domain.failures.UnexpectedError
import uk.co.itmms.androidArchitecture.ui.theme.AndroidArchitectureTheme
import uk.co.itmms.androidArchitecture.widgets.*

@Composable
fun LoginScreen(
    viewModel: LoginViewModel = hiltViewModel(),
) {
    viewModel.state.observeAsState(LoginViewModel.State()).value.apply {
        LoginScreenUI(
            state = this,
            onEvent = viewModel::doEvent,
        )
    }
}

@Composable
private fun FailureLogin?.toMessage(): String? =
    this?.let {
        when (it) {
            FailureLogin.ConnectionProblems -> stringResource(id = R.string.loginErrorConnectionProblems)
            FailureLogin.BackendProblems -> stringResource(id = R.string.loginErrorBackendProblems)
            FailureLogin.LoginError -> stringResource(id = R.string.loginErrorLoginError)
            is UnexpectedError -> it.e.localizedMessage ?: stringResource(id = R.string.loginErrorUnexpectedError)
        }
    }

@Composable
fun LoginScreenUI(
    state: LoginViewModel.State,
    onEvent: (LoginViewModel.EventType) -> Unit,
) {
    val connectedText = if (!state.data.connected) stringResource(id = R.string.connectionMissing) else ""

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
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState())
                    .fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(16.dp),
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
                    value = state.data.username,
                    enabled = !state.data.loggingIn,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    onValueChange = {
                        onEvent(LoginViewModel.EventType.UpdateData(
                            state.data.copy(username = it)
                        ))
                    }
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
                    value = state.data.password,
                    enabled = !state.data.loggingIn,
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    onValueChange = {
                        onEvent(LoginViewModel.EventType.UpdateData(
                            state.data.copy(password = it)
                        ))
                    }
                )
                SwitchRow(
                    testTag = LoginScreenTestTag.FakeBackendSwitch.name,
                    text = stringResource(id = R.string.loginInputFakeBackend),
                    checked = state.data.fakeBackend,
                    onCheckedChange = {
                        onEvent(LoginViewModel.EventType.UpdateData(
                            state.data.copy(fakeBackend = it)
                        ))
                    },
                )
                SwitchRow(
                    testTag = LoginScreenTestTag.FakeAuthorizationExpiring.name,
                    text = stringResource(id = R.string.loginInputFakeAuthenticationExpire),
                    checked = state.data.fakeAuthenticationExpire,
                    onCheckedChange = {
                        onEvent(LoginViewModel.EventType.UpdateData(
                            state.data.copy(fakeAuthenticationExpire = it)
                        ))
                    },
                )
                ButtonRoundedEdgesPrimary(modifier = Modifier
                    .padding(bottom = 16.dp)
                    .fillMaxWidth()
                    .testTag(LoginScreenTestTag.LoginButton.name),
                    stringId = R.string.loginLoginButton,
                    enabled = !state.data.loggingIn && state.data.username.isNotEmpty() &&state.data. password.isNotEmpty(),
                    onClick = { onEvent(LoginViewModel.EventType.Login) }
                )
                ButtonRoundedEdgesSecondary(
                    modifier = Modifier
                        .padding(bottom = 16.dp)
                        .fillMaxWidth()
                        .testTag(LoginScreenTestTag.ResetButton.name),
                    stringId = R.string.loginLoginReset,
                    enabled = !state.data.loggingIn,
                    onClick = { onEvent(LoginViewModel.EventType.ResetData) }
                )
                    TextError(
                        modifier = Modifier
                            .padding(bottom = 16.dp)
                            .fillMaxWidth()
                            .testTag(LoginScreenTestTag.ErrorText.name),
                        text = state.failureLogin?.toMessage() ?: "",
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
                state = state,
                onEvent = {},
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
                state = state.copy(data = state.data.copy(loggingIn = true)),
                onEvent = {},
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
                state = state.copy(data = state.data.copy(connected = false)),
                onEvent = {},
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
fun LoginScreenUIErrorPreview() {
    AndroidArchitectureTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colors.background
        ) {
            LoginScreenUI(
                state = state.copy(failureLogin = FailureLogin.ConnectionProblems),
                onEvent = {},
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
fun LoginScreenUIRealBackendPreview() {
    AndroidArchitectureTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colors.background
        ) {
            LoginScreenUI(
                state = state.copy(data = state.data.copy(fakeBackend = false)),
                onEvent = {},
            )
        }
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
        username = "my username",
        password = "my password",
        fakeBackend = true,
        loggingIn = false,
        connected = true,
    )
}