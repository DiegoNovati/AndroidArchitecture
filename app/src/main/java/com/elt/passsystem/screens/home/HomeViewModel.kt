package com.elt.passsystem.screens.home

import androidx.lifecycle.ViewModel
import com.elt.passsystem.services.IServiceNavigation
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val serviceNavigation: IServiceNavigation,
) : ViewModel() {

    fun popBack() {
        serviceNavigation.popBack()
    }
}