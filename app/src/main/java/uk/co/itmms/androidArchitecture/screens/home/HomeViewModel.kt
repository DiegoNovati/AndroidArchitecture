package uk.co.itmms.androidArchitecture.screens.home

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import uk.co.itmms.androidArchitecture.domain.entities.Booking
import uk.co.itmms.androidArchitecture.domain.entities.Customer
import uk.co.itmms.androidArchitecture.domain.failures.FailureHome
import uk.co.itmms.androidArchitecture.domain.usecases.NoParams
import uk.co.itmms.androidArchitecture.domain.usecases.home.UseCaseHomeInit
import uk.co.itmms.androidArchitecture.domain.usecases.home.UseCaseHomeLogout
import uk.co.itmms.androidArchitecture.domain.usecases.home.UseCaseHomeMonitor
import uk.co.itmms.androidArchitecture.screens.ViewModelBase
import uk.co.itmms.androidArchitecture.services.IServiceNavigation
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    serviceNavigation: IServiceNavigation,
    useCaseHomeInit: UseCaseHomeInit,
    useCaseHomeMonitor: UseCaseHomeMonitor,
    private val useCaseHomeLogout: UseCaseHomeLogout,
) : ViewModelBase<HomeViewModel.State>(
    serviceNavigation,
    initialState = State(),
) {
    data class StateData(
        val connected: Boolean = false,
        val customerList: List<Customer> = listOf(),
        val bookingList: List<Booking> = listOf(),
    )

    data class State(
        val loading: Boolean = false,
        val data: StateData = StateData(),
        val failure: FailureHome? = null,
    )

    sealed interface EventType {
        data object Logout : EventType
        data object OnDismissFailure : EventType
    }

    fun doEvent(eventType: EventType) {
        when (eventType) {
            EventType.Logout -> doLogout()
            EventType.OnDismissFailure -> doFailure(null)
        }
    }

    init {
        useCaseHomeInit.invoke(NoParams, viewModelScope) {
            it.fold(::doFailure) { result ->
                localState = localState.copy(
                    data = localState.data.copy(
                        customerList = result.customerList,
                        bookingList = result.bookingList,
                    )
                )
            }
        }
        useCaseHomeMonitor.invoke(NoParams, viewModelScope) {
            it.fold(::doFailure) { result ->
                viewModelScope.launch {
                    result.connected.collect { connected ->
                        localState = localState.copy(
                            data = localState.data.copy(connected = connected)
                        )
                    }
                }
            }
        }
    }

    private fun doLogout() {
        useCaseHomeLogout.invoke(NoParams, viewModelScope) {
            popBack()
        }
    }

    private fun doFailure(failure: FailureHome?) {
        localState = localState.copy(failure = failure)
    }
}