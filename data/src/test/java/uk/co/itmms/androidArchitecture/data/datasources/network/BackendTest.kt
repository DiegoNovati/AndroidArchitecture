package uk.co.itmms.androidArchitecture.data.datasources.network

import uk.co.itmms.androidArchitecture.data.BaseDataRobolectricTest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import uk.co.itmms.androidArchitecture.data.models.NetAuthLoginRequest

class BackendTest: BaseDataRobolectricTest() {

    private val username = "hbingley1"
    private val password = "CQutx25i8r"

    private lateinit var backend: IBackend

    @Before
    fun setUp() {
        // Setting 'releaseMode = false' allows to log the communication with the backend
        backend = createBackend(releaseMode = true, null)
    }

    @Test
    fun `integration tests`() = runBlocking<Unit>(Dispatchers.IO) {
        val loginRequest = NetAuthLoginRequest(
            username = username,
            password = password,
        )
        backend.login(loginRequest)
        backend.getProducts()
        backend.getTodos()
    }
}