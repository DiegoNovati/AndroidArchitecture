package uk.co.itmms.androidArchitecture.activities

import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import dagger.hilt.android.lifecycle.HiltViewModel
import uk.co.itmms.androidArchitecture.services.IServiceNavigation
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
        serviceNavigation.updateCurrentRoute()
    }
}