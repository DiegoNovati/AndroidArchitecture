package com.elt.passsystem.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elt.passsystem.domain.usecases.NoParams
import com.elt.passsystem.domain.usecases.authentication.UseCaseAuthenticationLogout
import com.elt.passsystem.services.IServiceNavigation
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val serviceNavigation: IServiceNavigation,
    private val useCaseAuthenticationLogout: UseCaseAuthenticationLogout,
) : ViewModel() {

    fun logout() {
        useCaseAuthenticationLogout.invoke(NoParams, viewModelScope) {
            serviceNavigation.popBack()
        }
    }
}