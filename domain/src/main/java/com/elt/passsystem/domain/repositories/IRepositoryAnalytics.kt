package com.elt.passsystem.domain.repositories

interface IRepositoryAnalytics {

    suspend fun setDevice(deviceId: String)
    suspend fun setUsername(deviceId: String)

    suspend fun logUseCase(useCaseName: String)
}