package uk.co.itmms.androidArchitecture.data.repositories

import arrow.core.Either
import arrow.core.right
import uk.co.itmms.androidArchitecture.data.datasources.IDataSourceBackend
import uk.co.itmms.androidArchitecture.data.datasources.network.BackendErrorCode
import uk.co.itmms.androidArchitecture.data.datasources.network.BackendException
import uk.co.itmms.androidArchitecture.data.extensions.toGender
import uk.co.itmms.androidArchitecture.data.models.NetAuthLoginResponse
import uk.co.itmms.androidArchitecture.domain.entities.User
import uk.co.itmms.androidArchitecture.domain.repositories.IRepositoryAuthentication

class RepositoryAuthentication(
    private val dataSourceBackend: IDataSourceBackend,
) : IRepositoryAuthentication {

    override suspend fun login(
        userName: String,
        password: String
    ): Either<IRepositoryAuthentication.RepositoryAuthenticationFailure, IRepositoryAuthentication.ResultLogin> =
        try {
            dataSourceBackend.login(
                username = userName,
                password = password,
            ).toResultLogin().right()
        } catch (e: BackendException) {
            Either.Left(e.toRepositoryAuthenticationFailure())
        }
}

internal fun NetAuthLoginResponse.toResultLogin(): IRepositoryAuthentication.ResultLogin =
    IRepositoryAuthentication.ResultLogin(
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

internal fun BackendException.toRepositoryAuthenticationFailure(): IRepositoryAuthentication.RepositoryAuthenticationFailure =
    when (this.errorCode) {
        BackendErrorCode.Http400 -> IRepositoryAuthentication.RepositoryAuthenticationFailure.LoginError

        BackendErrorCode.UnknownHost,
        BackendErrorCode.Timeout,
        BackendErrorCode.SSLError,
        BackendErrorCode.HttpUnmanaged,
        BackendErrorCode.IO -> IRepositoryAuthentication.RepositoryAuthenticationFailure.ConnectionProblems

        BackendErrorCode.Http401,
        BackendErrorCode.Http403,
        BackendErrorCode.NoDataChanges,
        BackendErrorCode.Unexpected -> IRepositoryAuthentication.RepositoryAuthenticationFailure.BackendProblems
    }