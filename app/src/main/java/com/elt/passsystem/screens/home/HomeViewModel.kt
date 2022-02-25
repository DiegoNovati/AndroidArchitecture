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

    data class HomeState(
        val connected: Boolean,
        val data: LoginResult,
    )

    private val _state: MutableLiveData<HomeState> by lazy {
        MutableLiveData()
    }
    val state: LiveData<HomeState> = _state

    private var homeState = HomeState(
        connected = true,
        data = LoginResult("", listOf(), listOf()),
    )

    init {
        serviceActivityBus.registerBackButtonPressedListener {
            logout()
        }
        serviceActivityBus.registerConnectivityStateChanged {
            updateState(homeState.copy(connected = it))
        }
        globalState.loginResult?.let {
            updateState(homeState.copy(data = it))
        }

        updateState(homeState.copy(connected = serviceActivityBus.getConnectivityState()))
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

    private fun updateState(homeState: HomeState) {
        this.homeState = homeState

        _state.value = homeState
    }
}