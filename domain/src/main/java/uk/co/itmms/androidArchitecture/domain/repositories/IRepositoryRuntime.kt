package uk.co.itmms.androidArchitecture.domain.repositories

import kotlinx.coroutines.flow.StateFlow

interface IRepositoryRuntime {

    suspend fun clear()

    suspend fun setIsFakeBackend(isFakeBackend: Boolean)
    suspend fun getIsFakeBackend(): Boolean


    fun isAuthenticatedFlow(): StateFlow<Boolean>
    fun setAuthenticated(value: Boolean)

    suspend fun setFakeAuthenticationExpire(value: Boolean)
}