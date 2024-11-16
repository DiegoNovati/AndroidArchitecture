package uk.co.itmms.androidArchitecture.screens.home

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import uk.co.itmms.androidArchitecture.domain.entities.Booking
import uk.co.itmms.androidArchitecture.domain.entities.Customer
import uk.co.itmms.androidArchitecture.domain.usecases.NoParams
import uk.co.itmms.androidArchitecture.domain.usecases.home.UseCaseHomeInit
import uk.co.itmms.androidArchitecture.domain.usecases.home.UseCaseHomeLogout
import uk.co.itmms.androidArchitecture.domain.usecases.home.UseCaseHomeMonitor
import uk.co.itmms.androidArchitecture.screens.ViewModelBase
import uk.co.itmms.androidArchitecture.services.IServiceNavigation
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val serviceNavigation: IServiceNavigation,
    useCaseHomeInit: UseCaseHomeInit,
    useCaseHomeMonitor: UseCaseHomeMonitor,
    private val useCaseHomeLogout: UseCaseHomeLogout,
) : ViewModelBase<HomeViewModel.State>(
    initialState = State(),
) {

    data class StateData(
        val connected: Boolean = false,
        val customerList: List<Customer> = listOf(),
        val bookingList: List<Booking> = listOf(),
    )

    data class State(
        val data: StateData = StateData(),
    )

    sealed interface EventType {
        object Logout : EventType
    }

    init {
        useCaseHomeInit.invoke(NoParams, viewModelScope) {
            it.fold({}) { result ->
                localState = localState.copy(
                    data = localState.data.copy(
                        customerList = result.customerList,
                        bookingList = result.bookingList,
                    )
                )
            }
        }
        useCaseHomeMonitor.invoke(NoParams, viewModelScope) {
            it.fold({}) { result ->
                result.connected.onEach { connected ->
                    localState = localState.copy(
                        data = localState.data.copy(connected = connected)
                    )
                }.launchIn(viewModelScope)
            }
        }
    }

    fun doEvent(eventType: EventType) {
        when (eventType) {
            EventType.Logout -> doLogout()
        }
    }

    private fun doLogout() {
        useCaseHomeLogout.invoke(NoParams, viewModelScope) {
            serviceNavigation.popBack()
        }
    }
}