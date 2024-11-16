package uk.co.itmms.androidArchitecture.data.repositories

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import uk.co.itmms.androidArchitecture.domain.repositories.IRepositoryRuntime
import java.util.Timer
import java.util.TimerTask

class RepositoryRuntime : IRepositoryRuntime {
    private val _authenticated = MutableStateFlow(false)
    private val authenticated: StateFlow<Boolean> = _authenticated.asStateFlow()

    private var isFakeBackend: Boolean = false

    override suspend fun clear() {
        isFakeBackend = false
    }

    override suspend fun setIsFakeBackend(isFakeBackend: Boolean) {
        this.isFakeBackend = isFakeBackend
    }

    override suspend fun getIsFakeBackend(): Boolean =
        isFakeBackend

    override fun isAuthenticatedFlow(): StateFlow<Boolean> =
        authenticated

    override fun setAuthenticated(value: Boolean) {
        _authenticated.value = value
    }

    override suspend fun setFakeAuthenticationExpire(value: Boolean) {
        if (!value)
            return

        // Simulating the expiring of a token
        val timer = Timer()
        timer.schedule(object : TimerTask() {
            override fun run() {
                timer.cancel()
                setAuthenticated(false)
            }
        }, 10000, 10000)
    }
}