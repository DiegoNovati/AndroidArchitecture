package com.elt.passsystem.screens.login

import androidx.lifecycle.ViewModel
import com.elt.passsystem.services.IServiceNavigation
import com.elt.passsystem.services.Route
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val serviceNavigation: IServiceNavigation,
) : ViewModel() {

    fun navigateToHome() =
        serviceNavigation.open(Route.Home)
}