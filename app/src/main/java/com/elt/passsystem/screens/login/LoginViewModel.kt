package com.elt.passsystem.screens.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elt.passsystem.domain.entities.LoginResult
import com.elt.passsystem.domain.usecases.Failure
import com.elt.passsystem.domain.usecases.authentication.UseCaseAuthenticationLogin
import com.elt.passsystem.services.IServiceNavigation
import com.elt.passsystem.services.Route
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val serviceNavigation: IServiceNavigation,
    private val useCaseAuthenticationLogin: UseCaseAuthenticationLogin,
) : ViewModel() {

    sealed class LoginState {
        object NoError: LoginState()
        object LoginError: LoginState()
        object BackendProblems: LoginState()
        object ConnectionProblems: LoginState()
        data class UnexpectedProblems(val error: String): LoginState()
    }

    private val _state: MutableLiveData<LoginState> by lazy { MutableLiveData<LoginState>() }
    val state: LiveData<LoginState> = _state

    fun resetError() =
        updateState(LoginState.NoError)

    fun login(username: String, password: String) {
        viewModelScope.launch {
            useCaseAuthenticationLogin.invoke(
                params = UseCaseAuthenticationLogin.Params(
                    username = username,
                    password = password,
                ),
                scope = viewModelScope,
            ) {
                it.fold({ failure ->
                    when (failure) {
                        is Failure.UnexpectedError ->
                            updateState(LoginState.UnexpectedProblems(failure.e.message ?: "Unexpected problem"))
                        is Failure.FeatureFailure -> when (failure as UseCaseAuthenticationLogin.LoginFailure) {
                            UseCaseAuthenticationLogin.LoginFailure.BackendProblems ->
                                updateState(LoginState.BackendProblems)
                            UseCaseAuthenticationLogin.LoginFailure.ConnectionProblems ->
                                updateState(LoginState.ConnectionProblems)
                            UseCaseAuthenticationLogin.LoginFailure.LoginError ->
                                updateState(LoginState.LoginError)
                        }
                    }
                }){ loginResult ->
                    openHome(loginResult)
                }
            }
        }
    }

    private fun updateState(loginState: LoginState) {
        _state.value = loginState
    }

    private fun openHome(loginResult: LoginResult) {
        // TODO: pass the result to the next screen
        serviceNavigation.open(Route.Home)
    }
}