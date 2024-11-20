package uk.co.itmms.androidArchitecture.screens.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import uk.co.itmms.androidArchitecture.R
import uk.co.itmms.androidArchitecture.components.AppButton
import uk.co.itmms.androidArchitecture.components.AppButtonType
import uk.co.itmms.androidArchitecture.components.AppSwitch
import uk.co.itmms.androidArchitecture.components.AppText
import uk.co.itmms.androidArchitecture.components.AppTextField
import uk.co.itmms.androidArchitecture.components.AppTextFieldType
import uk.co.itmms.androidArchitecture.components.AppTextType
import uk.co.itmms.androidArchitecture.domain.failures.FailureLogin
import uk.co.itmms.androidArchitecture.domain.failures.UnexpectedError
import uk.co.itmms.androidArchitecture.screens.PreviewAppScreen
import uk.co.itmms.androidArchitecture.ui.theme.AndroidArchitectureTheme

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
            is UnexpectedError -> it.e.localizedMessage
                ?: stringResource(id = R.string.loginErrorUnexpectedError)
        }
    }

@Composable
fun LoginScreenUI(
    state: LoginViewModel.State,
    onEvent: (LoginViewModel.EventType) -> Unit,
) {
    Scaffold(
        topBar = { LoginTopBar(connected = state.data.connected) },
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
                AppTextField(
                    modifier = Modifier
                        .fillMaxWidth(),
                    label = stringResource(id = R.string.loginInputUsername),
                    value = state.data.username,
                    testTag = LoginScreenTestTag.UsernameTextField.name,
                    enabled = !state.data.loggingIn,
                    type = AppTextFieldType.Email,
                    onValueChange = {
                        onEvent(
                            LoginViewModel.EventType.UpdateData(
                                state.data.copy(username = it)
                            )
                        )
                    }
                )
                AppTextField(
                    modifier = Modifier
                        .fillMaxWidth(),
                    label = stringResource(id = R.string.loginInputPassword),
                    value = state.data.password,
                    testTag = LoginScreenTestTag.PasswordTextField.name,
                    enabled = !state.data.loggingIn,
                    type = AppTextFieldType.Password,
                    onValueChange = {
                        onEvent(
                            LoginViewModel.EventType.UpdateData(
                                state.data.copy(password = it)
                            )
                        )
                    }
                )
                AppSwitch(
                    testTag = LoginScreenTestTag.FakeBackendSwitch.name,
                    text = stringResource(id = R.string.loginInputFakeBackend),
                    checked = state.data.fakeBackend,
                    onCheckedChange = {
                        onEvent(
                            LoginViewModel.EventType.UpdateData(
                                state.data.copy(fakeBackend = it)
                            )
                        )
                    },
                )
                AppSwitch(
                    testTag = LoginScreenTestTag.FakeAuthorizationExpiring.name,
                    text = stringResource(id = R.string.loginInputFakeAuthenticationExpire),
                    checked = state.data.fakeAuthenticationExpire,
                    onCheckedChange = {
                        onEvent(
                            LoginViewModel.EventType.UpdateData(
                                state.data.copy(fakeAuthenticationExpire = it)
                            )
                        )
                    },
                )
                AppButton(
                    modifier = Modifier
                        .fillMaxWidth(),
                    type = AppButtonType.Primary,
                    stringId = R.string.loginLoginButton,
                    testTag = LoginScreenTestTag.LoginButton.name,
                    enabled = !state.data.loggingIn && state.data.username.isNotEmpty() && state.data.password.isNotEmpty(),
                    onClick = { onEvent(LoginViewModel.EventType.Login) }
                )
                AppButton(
                    modifier = Modifier
                        .fillMaxWidth(),
                    type = AppButtonType.Secondary,
                    stringId = R.string.loginLoginReset,
                    enabled = !state.data.loggingIn,
                    testTag = LoginScreenTestTag.ResetButton.name,
                    onClick = { onEvent(LoginViewModel.EventType.ResetData) }
                )
                AppText(
                    modifier = Modifier
                        .padding(bottom = 16.dp)
                        .fillMaxWidth(),
                    text = state.failureLogin?.toMessage() ?: "",
                    type = AppTextType.Error,
                    testTag = LoginScreenTestTag.ErrorText.name,
                )
            }
        }
    }
}

@PreviewAppScreen
@Composable
private fun LoginScreenUIEnabledPreview() {
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

@PreviewAppScreen
@Composable
private fun LoginScreenUIDisabledPreview() {
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

@PreviewAppScreen
@Composable
private fun LoginScreenUIDisconnectedPreview() {
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

@PreviewAppScreen
@Composable
private fun LoginScreenUIErrorPreview() {
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

@PreviewAppScreen
@Composable
private fun LoginScreenUIRealBackendPreview() {
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