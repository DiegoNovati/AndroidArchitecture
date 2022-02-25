package com.elt.passsystem.screens.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elt.passsystem.domain.entities.AuthenticationLoginFailure
import com.elt.passsystem.domain.entities.LoginResult
import com.elt.passsystem.domain.entities.UnexpectedError
import com.elt.passsystem.domain.usecases.authentication.UseCaseAuthenticationLogin
import com.elt.passsystem.services.IServiceActivityBus
import com.elt.passsystem.services.IServiceNavigation
import com.elt.passsystem.services.Route
import com.elt.passsystem.state.GlobalState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val serviceNavigation: IServiceNavigation,
    serviceActivityBus: IServiceActivityBus,
    private val useCaseAuthenticationLogin: UseCaseAuthenticationLogin,
    private val globalState: GlobalState,
) : ViewModel() {

    sealed class ErrorType {
        object NoError: ErrorType()
        object LoginError: ErrorType()
        object ConnectionError: ErrorType()
        object BackendError: ErrorType()
        data class UnexpectedError(val error: String): ErrorType()
    }

    data class LoginState(
        val connected: Boolean,
        val loggingIn: Boolean,
        val username: String,
        val errorType: ErrorType,
    )

    private val _state: MutableLiveData<LoginState> by lazy { MutableLiveData<LoginState>() }
    val state: LiveData<LoginState> = _state

    private var loginState = LoginState(
        connected = true,
        loggingIn = false,
        username = "",
        errorType = ErrorType.NoError,
    )

    init {
        serviceActivityBus.registerConnectivityStateChanged {
            updateState(loginState.copy(connected = it))
        }

        updateState(loginState.copy(connected = serviceActivityBus.getConnectivityState()))
    }

    fun updateUsername(username: String) =
        updateState(loginState.copy(username = username, errorType = ErrorType.NoError))

    fun resetData() {
        updateState(loginState.copy(username = "", errorType = ErrorType.NoError))
    }

    fun resetError() =
        updateState(loginState.copy(errorType = ErrorType.NoError))

    fun login(password: String) {
        updateState(loginState.copy(loggingIn = true))
        viewModelScope.launch {
            useCaseAuthenticationLogin.invoke(
                params = UseCaseAuthenticationLogin.Params(
                    username = loginState.username,
                    password = password,
                ),
                scope = viewModelScope,
            ) {
                it.fold({ failure ->
                    val errorType = when (failure) {
                        AuthenticationLoginFailure.LoginError ->
                            ErrorType.LoginError
                        AuthenticationLoginFailure.ConnectionProblems ->
                            ErrorType.ConnectionError
                        AuthenticationLoginFailure.BackendProblems ->
                            ErrorType.BackendError
                        is UnexpectedError ->
                            ErrorType.UnexpectedError(failure.e.message ?: "Unexpected problem")
                    }
                    updateState(loginState.copy(errorType = errorType, loggingIn = false))
                }) { loginResult ->
                    openHome(loginResult)
                    updateState(loginState.copy(loggingIn = false))
                }
            }
        }
    }

    private fun updateState(loginState: LoginState) {
        this.loginState = loginState

        _state.value = loginState
    }

    private fun openHome(loginResult: LoginResult) {
        globalState.loginResult = loginResult
        serviceNavigation.open(Route.Home)
    }
}