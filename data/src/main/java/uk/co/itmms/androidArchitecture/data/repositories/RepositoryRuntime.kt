package uk.co.itmms.androidArchitecture.data.repositories

import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import uk.co.itmms.androidArchitecture.domain.repositories.IRepositoryRuntime

class RepositoryRuntime(
    private val timeoutSeconds: Int = 10,
) : IRepositoryRuntime {
    private val _authenticated = MutableStateFlow(false)
    private val authenticated: StateFlow<Boolean> = _authenticated.asStateFlow()

    override fun isAuthenticatedFlow(): StateFlow<Boolean> =
        authenticated

    override fun setAuthenticated(value: Boolean) {
        _authenticated.value = value
    }

    @OptIn(DelicateCoroutinesApi::class)
    override suspend fun setFakeAuthenticationExpire(value: Boolean) {
        if (!value)
            return
        currentCoroutineContext().apply {

        }
        GlobalScope.launch {
            delay(timeoutSeconds * 1000L)
            setAuthenticated(false)
        }
    }
}