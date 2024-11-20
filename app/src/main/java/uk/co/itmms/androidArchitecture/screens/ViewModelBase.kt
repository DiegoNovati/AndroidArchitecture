package uk.co.itmms.androidArchitecture.screens

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import uk.co.itmms.androidArchitecture.services.IServiceNavigation

open class ViewModelBase<State>(
    protected val serviceNavigation: IServiceNavigation,
    initialState: State,
) : ViewModel() {

    protected var localState = initialState
        set(value) {
            field = value
            _state.value = value
        }
    private val _state = MutableLiveData(initialState)
    val state: LiveData<State> = _state

    fun popBack() {
        serviceNavigation.popBack()
    }
}