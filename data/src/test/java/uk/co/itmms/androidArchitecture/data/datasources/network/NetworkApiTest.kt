package uk.co.itmms.androidArchitecture.data.datasources.network

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import okhttp3.Credentials
import org.junit.Before
import org.junit.Ignore
import org.junit.Test
import uk.co.itmms.androidArchitecture.data.BaseDataRobolectricTest

@Ignore("Backend not available anymore")
class NetworkApiTest: BaseDataRobolectricTest() {

    private val username = "DavidB811543"
    private val password = "Qwaszx12"

    private lateinit var networkApi: INetworkApi

    @Before
    fun setUp() {
        // Setting 'releaseMode = false' allows to log the communication with the backend
        networkApi = createNetworkApi(releaseMode = true, null)
    }

    @Test
    fun `integration tests`() = runBlocking<Unit>(Dispatchers.IO) {
        val auth = Credentials.basic(username, password)
        val authenticateResponse = networkApi.authenticate(auth = auth)
        val authorization = "Bearer ${authenticateResponse.token}"
        val officeBid = authenticateResponse.offices[0].bid

        networkApi.getCustomerList(authorization, officeBid)

        networkApi.getBookingList(authorization, officeBid)
    }
}