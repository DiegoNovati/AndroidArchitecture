package com.elt.passsystem.screens.login

sealed class ScreenTestTag(val name: String)

sealed class LoginScreenTestTag {
    object TitleText: ScreenTestTag("title")
    object UsernameTextField: ScreenTestTag("username")
    object PasswordTextField: ScreenTestTag("password")
    object LoginButton: ScreenTestTag("login")
    object ResetButton: ScreenTestTag("reset")
    object ErrorText: ScreenTestTag("errorMessage")
}