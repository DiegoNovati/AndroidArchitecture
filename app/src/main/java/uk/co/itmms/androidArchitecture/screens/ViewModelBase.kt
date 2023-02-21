package uk.co.itmms.androidArchitecture.screens

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

open class ViewModelBase<State>(
    initialState: State,
) : ViewModel() {

    protected var localState = initialState
        set(value) {
            field = value
            _state.value = value
        }
    private val _state = MutableLiveData(initialState)
    val state: LiveData<State> = _state
}