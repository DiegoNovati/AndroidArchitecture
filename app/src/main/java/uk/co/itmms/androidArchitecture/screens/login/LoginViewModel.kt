package uk.co.itmms.androidArchitecture.screens.login

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import uk.co.itmms.androidArchitecture.domain.failures.FailureLogin
import uk.co.itmms.androidArchitecture.domain.usecases.NoParams
import uk.co.itmms.androidArchitecture.domain.usecases.login.UseCaseLoginLogin
import uk.co.itmms.androidArchitecture.domain.usecases.login.UseCaseLoginMonitor
import uk.co.itmms.androidArchitecture.screens.ViewModelBase
import uk.co.itmms.androidArchitecture.services.IServiceNavigation
import uk.co.itmms.androidArchitecture.services.Route
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    serviceNavigation: IServiceNavigation,
    useCaseLoginMonitor: UseCaseLoginMonitor,
    private val useCaseLoginLogin: UseCaseLoginLogin,
) : ViewModelBase<LoginViewModel.State>(
    serviceNavigation = serviceNavigation,
    initialState = State(),
) {
    data class StateData(
        val connected: Boolean = true,
        val loggingIn: Boolean = false,
        val username: String = "",
        val password: String = "",
        val fakeBackend: Boolean = true,
        val fakeAuthenticationExpire: Boolean = false,
    )

    data class State(
        val data: StateData = StateData(),
        val failureLogin: FailureLogin? = null,
    )

    sealed interface EventType {
        data class UpdateData(val stateData: StateData) : EventType
        data object ResetData: EventType
        data object Login : EventType
    }

    init {
        useCaseLoginMonitor.invoke(NoParams, viewModelScope) {
            it.fold({}) { result ->
                viewModelScope.launch {
                    result.update.collect { update ->
                        when (update.updateType) {
                            UseCaseLoginMonitor.UpdateType.Connected ->
                                localState = localState.copy(
                                    data = localState.data.copy(connected = update.value)
                                )
                            UseCaseLoginMonitor.UpdateType.Authentication -> {
                                if (!update.value) {
                                    backToLogin()
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    fun doEvent(eventType: EventType) {
        when (eventType) {
            is EventType.UpdateData -> doUpdateData(eventType.stateData)
            EventType.ResetData -> doResetData()
            EventType.Login -> doLogin()
        }
    }

    private fun doUpdateData(data: StateData) {
        localState = localState.copy(data = data)
    }

    private fun doResetData() {
        localState = localState.copy(
            data = localState.data.copy(
                username = "",
                password = "",
                loggingIn = false,
            )
        )
    }

    private fun doLogin() {
        val params = UseCaseLoginLogin.Params(
            username = localState.data.username,
            password = localState.data.password,
            fakeBackend = localState.data.fakeBackend,
            fakeAuthenticationExpire = localState.data.fakeAuthenticationExpire,
        )
        viewModelScope.launch {
            useCaseLoginLogin.invoke(params, viewModelScope) {
                it.fold(::displayError) {
                    localState = localState.copy(
                        data = localState.data.copy(
                            password = "",
                            loggingIn = false,
                        ),
                    )
                    openHome()
                }
            }
        }
    }

    private fun displayError(failureLogin: FailureLogin) {
        localState = localState.copy(
            data = localState.data.copy(loggingIn = false),
            failureLogin = failureLogin,
        )
    }

    private fun openHome() {
        serviceNavigation.open(Route.Home)
    }

    private fun backToLogin() {
        while (serviceNavigation.getCurrentRoute() != Route.Login) {
            serviceNavigation.popBack()
        }
    }
}