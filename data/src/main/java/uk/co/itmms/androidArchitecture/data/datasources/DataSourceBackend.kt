package uk.co.itmms.androidArchitecture.data.datasources

import okhttp3.Credentials
import uk.co.itmms.androidArchitecture.data.datasources.network.INetworkApi
import uk.co.itmms.androidArchitecture.data.extensions.toNetworkApiException
import uk.co.itmms.androidArchitecture.data.models.NetAuthenticateOffice
import uk.co.itmms.androidArchitecture.data.models.NetBookingsResponse
import uk.co.itmms.androidArchitecture.data.models.NetCustomersResponse

// This invoker returns a value or a NetworkApiException.
private suspend fun <T : Any> networkApiInvoker(passApi: suspend () -> T): T =
    try {
        passApi.invoke()
    } catch (e: Throwable) {
        throw e.toNetworkApiException()
    }

interface IDataSourceBackend {
    suspend fun authenticate(username: String, password: String): List<NetAuthenticateOffice>
    suspend fun logout()
    suspend fun getCustomerList(officeBid: String): NetCustomersResponse
    suspend fun getBookingList(officeBid: String): NetBookingsResponse
}

class DataSourceBackend(
    private val passApi: INetworkApi,
): IDataSourceBackend {

    internal var authorization = ""

    override suspend fun authenticate(username: String, password: String): List<NetAuthenticateOffice> {
        val authenticateResponse = networkApiInvoker {
            passApi.authenticate(auth = Credentials.basic(username, password))
        }
        authorization = "Bearer ${authenticateResponse.token}"
        return authenticateResponse.offices
    }

    override suspend fun logout() {
        authorization = ""
    }

    override suspend fun getCustomerList(officeBid: String): NetCustomersResponse =
        networkApiInvoker {
            passApi.getCustomerList(authorization, officeBid)
        }

    override suspend fun getBookingList(officeBid: String): NetBookingsResponse =
        networkApiInvoker {
            passApi.getBookingList(authorization, officeBid)
        }
}