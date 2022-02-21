package com.elt.passsystem.activities

import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import com.elt.passsystem.services.IServiceNavigation
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val serviceNavigation: IServiceNavigation,
): ViewModel() {

    fun getServiceNavigation(): IServiceNavigation =
        serviceNavigation

    fun setNavController(navController: NavHostController) =
        serviceNavigation.setNavController(navController)

    fun backButtonPressed() =
        serviceNavigation.updateCurrentRoute()
}