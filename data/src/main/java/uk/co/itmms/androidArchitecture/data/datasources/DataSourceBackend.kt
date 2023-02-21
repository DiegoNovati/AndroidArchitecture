package uk.co.itmms.androidArchitecture.data.datasources

import uk.co.itmms.androidArchitecture.data.datasources.network.IPassApi
import uk.co.itmms.androidArchitecture.data.extensions.toPassApiException
import uk.co.itmms.androidArchitecture.data.models.NetAuthenticateOffice
import uk.co.itmms.androidArchitecture.data.models.NetBookingsResponse
import uk.co.itmms.androidArchitecture.data.models.NetCustomersResponse
import okhttp3.Credentials

// This invoker returns a value or a PassApiException.
private suspend fun <T : Any> passApiInvoker(passApi: suspend () -> T): T =
    try {
        passApi.invoke()
    } catch (e: Throwable) {
        throw e.toPassApiException()
    }

interface IDataSourceBackend {
    suspend fun authenticate(username: String, password: String): List<NetAuthenticateOffice>
    suspend fun logout()
    suspend fun getCustomerList(officeBid: String): NetCustomersResponse
    suspend fun getBookingList(officeBid: String): NetBookingsResponse
}

class DataSourceBackend(
    private val passApi: IPassApi,
): IDataSourceBackend {

    internal var authorization = ""

    override suspend fun authenticate(username: String, password: String): List<NetAuthenticateOffice> {
        val authenticateResponse = passApiInvoker {
            passApi.authenticate(auth = Credentials.basic(username, password))
        }
        authorization = "Bearer ${authenticateResponse.token}"
        return authenticateResponse.offices
    }

    override suspend fun logout() {
        authorization = ""
    }

    override suspend fun getCustomerList(officeBid: String): NetCustomersResponse =
        passApiInvoker {
            passApi.getCustomerList(authorization, officeBid)
        }

    override suspend fun getBookingList(officeBid: String): NetBookingsResponse =
        passApiInvoker {
            passApi.getBookingList(authorization, officeBid)
        }
}