package uk.co.itmms.androidArchitecture.domain.failures

data class UnexpectedError(val e: Throwable) : BaseFailure()

// Sealed interfaces in Kotlin
//   The following article inspired the implementation of the failures for each use case:
//
//   https://jorgecastillo.dev/sealed-interfaces-kotlin

/**
 * Warning: BaseFailure must inherit from EVERY sealed interface used by each use cases. In case of
 *          missing, the App will crash when the use case have to deal with an unexpected exception.
 */
sealed class BaseFailure: FailureTest, FailureLogin, FailureHome

sealed interface FailureTest {
    data object UnknownProblem : FailureTest
}

sealed interface FailureLogin {
    data object ConnectionProblems : FailureLogin
    data object BackendProblems : FailureLogin
    data object LoginError : FailureLogin
}

sealed interface FailureHome