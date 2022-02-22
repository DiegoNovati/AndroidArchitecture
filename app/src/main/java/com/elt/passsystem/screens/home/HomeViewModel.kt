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

    private val _state: MutableLiveData<LoginResult> by lazy {
        MutableLiveData()
    }
    val state: LiveData<LoginResult> = _state

    init {
        serviceActivityBus.registerBackButtonPressedListener {
            logout()
        }
        globalState.loginResult?.let {
            updateState(it)
        }
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

    private fun updateState(loginResult: LoginResult) {
        _state.value = loginResult
    }
}