package com.elt.passsystem.state

import com.elt.passsystem.domain.entities.LoginResult

class GlobalState {

    var loginResult: LoginResult? = null

    fun reset() {
        loginResult = null
    }
}