package uk.co.itmms.androidArchitecture.data.datasources.network

import uk.co.itmms.androidArchitecture.data.BaseDataRobolectricTest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import okhttp3.Credentials
import org.junit.Before
import org.junit.Ignore
import org.junit.Test

@Ignore("Backend not available anymore")
class PassApiTest: BaseDataRobolectricTest() {

    private val username = "DavidB811543"
    private val password = "Qwaszx12"

    private lateinit var passApi: IPassApi

    @Before
    fun setUp() {
        // Setting 'releaseMode = false' allows to log the communication with the backend
        passApi = createPassApi(releaseMode = true, null)
    }

    @Test
    fun `integration tests`() = runBlocking<Unit>(Dispatchers.IO) {
        val auth = Credentials.basic(username, password)
        val authenticateResponse = passApi.authenticate(auth = auth)
        val authorization = "Bearer ${authenticateResponse.token}"
        val officeBid = authenticateResponse.offices[0].bid

        passApi.getCustomerList(authorization, officeBid)

        passApi.getBookingList(authorization, officeBid)
    }
}