package com.elt.passsystem.domain.repositories

interface IRepositoryLogger {

    suspend fun setDevice(deviceId: String)
    suspend fun setUsername(deviceId: String)

    suspend fun logInfo(useCaseName: String, message: String)
    suspend fun logError(useCaseName: String, params: String, failure: String)
    suspend fun logUnexpectedThrowable(useCaseName: String, params: String, e: Throwable)
    suspend fun logUnexpectedFailure(useCaseName: String, params: String, failure: String)
}