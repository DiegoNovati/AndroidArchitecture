package com.elt.passsystem.extensions

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.elt.passsystem.R
import com.elt.passsystem.domain.entities.AuthenticationLoginFailure
import com.elt.passsystem.domain.entities.UnexpectedError

@Composable
fun AuthenticationLoginFailure.toErrorMessage(): String {
    return when (this) {
        AuthenticationLoginFailure.ConnectionProblems -> stringResource(id = R.string.loginErrorConnectionProblems)
        AuthenticationLoginFailure.BackendProblems -> stringResource(id = R.string.loginErrorBackendProblems)
        AuthenticationLoginFailure.LoginError -> stringResource(id = R.string.loginErrorLoginError)
        is UnexpectedError -> this.e.localizedMessage ?: stringResource(id = R.string.loginErrorUnexpectedError)
    }
}