package com.elt.passsystem.screens.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elt.passsystem.domain.entities.AuthenticationLoginFailure
import com.elt.passsystem.domain.entities.LoginResult
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

    data class State(
        val connected: Boolean = true,
        val loggingIn: Boolean = false,
        val username: String = "",
        val authenticationLoginFailure: AuthenticationLoginFailure? = null,
    )

    private var localState = State()
    private val _state = MutableLiveData<State>()
    val state: LiveData<State> = _state

    init {
        serviceActivityBus.registerConnectivityStateChanged {
            localState = localState.copy(connected = it)
            updateState()
        }

        localState = localState.copy(connected = serviceActivityBus.getConnectivityState())
        updateState()
    }

    fun updateUsername(username: String) {
        localState = localState.copy(username = username, authenticationLoginFailure = null)
        updateState()
    }

    fun resetData() {
        localState = localState.copy(username = "", authenticationLoginFailure = null)
        updateState()
    }

    fun resetError() {
        localState = localState.copy(authenticationLoginFailure = null)
        updateState()
    }

    fun login(password: String) {
        localState = localState.copy(loggingIn = true, authenticationLoginFailure = null)
        updateState()

        val params = UseCaseAuthenticationLogin.Params(
            username = localState.username,
            password = password,
        )
        viewModelScope.launch {
            useCaseAuthenticationLogin.invoke(params, viewModelScope) {
                it.fold(::displayError, ::loginSuccessful)
            }
        }
    }

    private fun loginSuccessful(loginResult: LoginResult) {
        localState = localState.copy(loggingIn = false)
        updateState()
        openHome(loginResult)
    }

    private fun displayError(authenticationLoginFailure: AuthenticationLoginFailure) {
        localState = localState.copy(
            loggingIn = false,
            authenticationLoginFailure = authenticationLoginFailure,
        )
        updateState()
    }

    private fun openHome(loginResult: LoginResult) {
        globalState.loginResult = loginResult
        serviceNavigation.open(Route.Home)
    }

    private fun updateState() {
        _state.value = localState
    }
}