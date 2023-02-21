package uk.co.itmms.androidArchitecture.domain.repositories

interface IRepositoryDevelopmentLogger {

    suspend fun setDevice(deviceId: String)
    suspend fun setUsername(username: String)
    suspend fun setProperty(name: String, value: Any)

    suspend fun logIssue(tag: String, message: String)
    suspend fun logDebug(tag: String, message: String)
    suspend fun logInfo(tag: String, message: String)
    suspend fun logWarning(tag: String, message: String)
    suspend fun logError(tag: String, message: String)

    suspend fun send()
}