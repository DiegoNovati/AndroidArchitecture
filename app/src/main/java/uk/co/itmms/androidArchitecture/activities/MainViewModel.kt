package uk.co.itmms.androidArchitecture.activities

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import uk.co.itmms.androidArchitecture.services.IServiceNavigation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val serviceNavigation: IServiceNavigation,
): ViewModel() {
    fun getServiceNavigation(): IServiceNavigation =
        serviceNavigation

    fun setNavController(navController: NavHostController) =
        serviceNavigation.setNavController(navController)

    fun backButtonPressed() {
        viewModelScope.launch {
            serviceNavigation.updateCurrentRoute()
        }
    }
}