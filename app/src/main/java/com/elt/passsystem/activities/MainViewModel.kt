package com.elt.passsystem.activities

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.elt.passsystem.domain.usecases.NoParams
import com.elt.passsystem.domain.usecases.networkMonitor.UseCaseNetworkMonitor
import com.elt.passsystem.services.IServiceActivityBus
import com.elt.passsystem.services.IServiceNavigation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val serviceNavigation: IServiceNavigation,
    private val serviceActivityBus: IServiceActivityBus,
    useCaseNetworkMonitor: UseCaseNetworkMonitor,
): ViewModel() {

    init {
        useCaseNetworkMonitor.invoke(NoParams, viewModelScope) {
            it.fold({}){
                viewModelScope.launch {
                    serviceActivityBus.notifyConnectivityStateChanged(it)
                }
            }
        }
    }

    fun getServiceNavigation(): IServiceNavigation =
        serviceNavigation

    fun setNavController(navController: NavHostController) =
        serviceNavigation.setNavController(navController)

    fun backButtonPressed() {
        viewModelScope.launch {
            serviceActivityBus.notifyBackButtonPressed()
            serviceNavigation.updateCurrentRoute()
        }
    }
}