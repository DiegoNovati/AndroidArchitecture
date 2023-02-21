package uk.co.itmms.androidArchitecture.domain.repositories

interface IRepositoryDevelopmentAnalytics {

    suspend fun setDevice(deviceId: String)
    suspend fun setProperty(name: String, value: String?)

    suspend fun logEvent(eventName: String, eventParamList: List<Pair<String, Any>> = emptyList())

    suspend fun logUseCase(useCaseName: String, milliSec: Long)
}