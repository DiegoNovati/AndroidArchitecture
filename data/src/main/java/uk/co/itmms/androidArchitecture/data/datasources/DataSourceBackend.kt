package uk.co.itmms.androidArchitecture.data.datasources

import uk.co.itmms.androidArchitecture.data.datasources.network.IBackend
import uk.co.itmms.androidArchitecture.data.extensions.toBackendException
import uk.co.itmms.androidArchitecture.data.models.NetAuthLoginRequest
import uk.co.itmms.androidArchitecture.data.models.NetAuthLoginResponse
import uk.co.itmms.androidArchitecture.data.models.NetProductsResponse
import uk.co.itmms.androidArchitecture.data.models.NetTodosResponse

// This invoker returns a value or a PassApiException.
private suspend fun <T : Any> backendInvoker(passApi: suspend () -> T): T =
    try {
        passApi.invoke()
    } catch (e: Throwable) {
        throw e.toBackendException()
    }

interface IDataSourceBackend {
    suspend fun login(username: String, password: String): NetAuthLoginResponse
    suspend fun getProducts(): NetProductsResponse
    suspend fun getTodos(): NetTodosResponse
}

class DataSourceBackend(
    private val backend: IBackend,
): IDataSourceBackend {
    override suspend fun login(username: String, password: String): NetAuthLoginResponse =
        backendInvoker {
            backend.login(NetAuthLoginRequest(
                username = username,
                password = password,
            ))
        }

    override suspend fun getProducts(): NetProductsResponse =
        backendInvoker {
            backend.getProducts()
        }

    override suspend fun getTodos(): NetTodosResponse =
        backendInvoker {
            backend.getTodos()
        }
}