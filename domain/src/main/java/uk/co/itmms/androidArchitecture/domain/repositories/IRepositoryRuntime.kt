package uk.co.itmms.androidArchitecture.domain.repositories

import kotlinx.coroutines.flow.StateFlow

/**
 * This class should contain all the data shared between screens
 */
interface IRepositoryRuntime {

    fun isAuthenticatedFlow(): StateFlow<Boolean>
    fun setAuthenticated(value: Boolean)

    suspend fun setFakeAuthenticationExpire(value: Boolean)
}