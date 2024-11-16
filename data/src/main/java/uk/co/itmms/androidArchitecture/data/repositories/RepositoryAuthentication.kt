package uk.co.itmms.androidArchitecture.data.repositories

import arrow.core.Either
import arrow.core.right
import uk.co.itmms.androidArchitecture.data.datasources.IDataSourceBackend
import uk.co.itmms.androidArchitecture.data.datasources.network.BackendErrorCode
import uk.co.itmms.androidArchitecture.data.datasources.network.BackendException
import uk.co.itmms.androidArchitecture.data.extensions.toGender
import uk.co.itmms.androidArchitecture.data.models.NetAuthLoginResponse
import uk.co.itmms.androidArchitecture.domain.entities.User
import uk.co.itmms.androidArchitecture.domain.failures.FailureRepositoryBackendAuthentication
import uk.co.itmms.androidArchitecture.domain.repositories.IRepositoryAuthentication

class RepositoryAuthentication(
    private val dataSourceBackend: IDataSourceBackend,
) : IRepositoryAuthentication {

    override suspend fun login(
        userName: String,
        password: String
    ): Either<FailureRepositoryBackendAuthentication, IRepositoryAuthentication.Result> =
        try {
            dataSourceBackend.login(
                username = userName,
                password = password,
            ).toResultLogin().right()
        } catch (e: BackendException) {
            Either.Left(e.toRepositoryAuthenticationFailure())
        }
}

internal fun NetAuthLoginResponse.toResultLogin(): IRepositoryAuthentication.Result =
    IRepositoryAuthentication.Result(
        user = User(
            id = this.id,
            username = this.username,
            email = this.email,
            firstName = this.firstName,
            lastName = this.lastName,
            gender = this.gender.toGender(),
            image = this.image,
        ),
        token = this.token,
    )

internal fun BackendException.toRepositoryAuthenticationFailure(): FailureRepositoryBackendAuthentication =
    when (this.errorCode) {
        BackendErrorCode.Http400 -> FailureRepositoryBackendAuthentication.LoginError

        BackendErrorCode.UnknownHost,
        BackendErrorCode.Timeout,
        BackendErrorCode.SSLError,
        BackendErrorCode.HttpUnmanaged,
        BackendErrorCode.IO -> FailureRepositoryBackendAuthentication.ConnectionError

        BackendErrorCode.Http401,
        BackendErrorCode.Http403,
        BackendErrorCode.Unexpected -> FailureRepositoryBackendAuthentication.BackendError
    }