package com.elt.passsystem.screens.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elt.passsystem.domain.entities.LoginResult
import com.elt.passsystem.domain.usecases.NoParams
import com.elt.passsystem.domain.usecases.authentication.UseCaseAuthenticationLogout
import com.elt.passsystem.services.IServiceActivityBus
import com.elt.passsystem.services.IServiceNavigation
import com.elt.passsystem.state.GlobalState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val serviceNavigation: IServiceNavigation,
    private val serviceActivityBus: IServiceActivityBus,
    private val useCaseAuthenticationLogout: UseCaseAuthenticationLogout,
    private val globalState: GlobalState,
) : ViewModel() {

    data class State(
        val connected: Boolean = false,
        val data: LoginResult = LoginResult(
            officeBid = "",
            customerList = listOf(),
            bookingList = listOf(),
        ),
    )

    private var localState = State()
    private val _state = MutableLiveData<State>()
    val state: LiveData<State> = _state

    init {
        serviceActivityBus.registerBackButtonPressedListener {
            logout()
        }
        serviceActivityBus.registerConnectivityStateChanged {
            localState = localState.copy(connected = it)
            updateState()
        }
        globalState.loginResult?.let {
            localState = localState.copy(data = it)
            updateState()
        }

        localState = localState.copy(connected = serviceActivityBus.getConnectivityState())
        updateState()
    }

    override fun onCleared() {
        serviceActivityBus.unregisterBackButtonPressedListener()
    }

    fun logout() {
        useCaseAuthenticationLogout.invoke(NoParams, viewModelScope) {
            serviceNavigation.popBack()
            globalState.reset()
        }
    }

    private fun updateState() {
        _state.value = localState
    }
}