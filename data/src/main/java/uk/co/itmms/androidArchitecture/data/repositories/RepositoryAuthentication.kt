package uk.co.itmms.androidArchitecture.data.repositories

import arrow.core.Either
import uk.co.itmms.androidArchitecture.data.datasources.IDataSourceBackend
import uk.co.itmms.androidArchitecture.data.datasources.network.NetworkApiErrorCode
import uk.co.itmms.androidArchitecture.data.datasources.network.NetworkApiException
import uk.co.itmms.androidArchitecture.domain.repositories.IRepositoryAuthentication

class RepositoryAuthentication(
    private val dataSourceBackend: IDataSourceBackend,
) : IRepositoryAuthentication {

    override suspend fun login(
        userName: String,
        password: String
    ): Either<IRepositoryAuthentication.RepositoryAuthenticationFailure, IRepositoryAuthentication.ResultLogin> {
        try {
            val officesList = dataSourceBackend.authenticate(
                username = userName,
                password = password,
            )
            if (officesList.isEmpty()) {
                return Either.Left(IRepositoryAuthentication.RepositoryAuthenticationFailure.LoginError)
            }
            if (!officesList[0].v2) {
                return Either.Left(IRepositoryAuthentication.RepositoryAuthenticationFailure.LoginError)
            }
            return Either.Right(
                IRepositoryAuthentication.ResultLogin(
                    officeBid = officesList[0].bid,
                )
            )
        } catch (e: NetworkApiException) {
            return Either.Left(e.toRepositoryAuthenticationFailure())
        }
    }

    override suspend fun logout() {
        dataSourceBackend.logout()
    }
}

internal fun NetworkApiException.toRepositoryAuthenticationFailure(): IRepositoryAuthentication.RepositoryAuthenticationFailure =
    when (this.errorCode) {
        NetworkApiErrorCode.Http401 -> IRepositoryAuthentication.RepositoryAuthenticationFailure.LoginError

        NetworkApiErrorCode.UnknownHost,
        NetworkApiErrorCode.Timeout,
        NetworkApiErrorCode.SSLError,
        NetworkApiErrorCode.HttpUnmanaged,
        NetworkApiErrorCode.IO -> IRepositoryAuthentication.RepositoryAuthenticationFailure.ConnectionProblems

        NetworkApiErrorCode.Http400,
        NetworkApiErrorCode.Http403,
        NetworkApiErrorCode.NoDataChanges,
        NetworkApiErrorCode.Unexpected -> IRepositoryAuthentication.RepositoryAuthenticationFailure.BackendProblems
    }