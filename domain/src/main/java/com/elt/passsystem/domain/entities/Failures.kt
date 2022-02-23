package com.elt.passsystem.domain.entities

data class UnexpectedError(val e: Throwable) : BaseFailure()

// Sealed interfaces in Kotlin
//   The following article inspired the implementation of the failures for each use case:
//
//   https://jorgecastillo.dev/sealed-interfaces-kotlin

/**
 * Warning: BaseFailure must inherit from EVERY sealed interface used by each use cases. In case of
 *          missing, the App will crash when the use case have to deal with an unexpected exception.
 */
sealed class BaseFailure: AuthenticationLoginFailure

sealed interface AuthenticationLoginFailure {
    object ConnectionProblems : AuthenticationLoginFailure
    object BackendProblems : AuthenticationLoginFailure
    object LoginError : AuthenticationLoginFailure
}