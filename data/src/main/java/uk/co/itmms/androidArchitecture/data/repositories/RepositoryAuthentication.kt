package uk.co.itmms.androidArchitecture.data.repositories

import arrow.core.Either
import uk.co.itmms.androidArchitecture.data.datasources.IDataSourceBackend
import uk.co.itmms.androidArchitecture.data.datasources.network.BackendErrorCode
import uk.co.itmms.androidArchitecture.data.datasources.network.BackendException
import uk.co.itmms.androidArchitecture.domain.repositories.IRepositoryAuthentication

class RepositoryAuthentication(
    private val dataSourceBackend: IDataSourceBackend,
) : IRepositoryAuthentication {

    override suspend fun login(
        userName: String,
        password: String
    ): Either<IRepositoryAuthentication.RepositoryAuthenticationFailure, IRepositoryAuthentication.ResultLogin> {
        try {
//            val officesList = dataSourceBackend.authenticate(
//                username = userName,
//                password = password,
//            )
//            if (officesList.isEmpty()) {
//                return Either.Left(IRepositoryAuthentication.RepositoryAuthenticationFailure.LoginError)
//            }
//            if (!officesList[0].v2) {
//                return Either.Left(IRepositoryAuthentication.RepositoryAuthenticationFailure.LoginError)
//            }
//            return Either.Right(
//                IRepositoryAuthentication.ResultLogin(
//                    officeBid = officesList[0].bid,
//                )
//            )
            return Either.Right(
                IRepositoryAuthentication.ResultLogin(
                    officeBid = "bid",
                )
            )
        } catch (e: BackendException) {
            return Either.Left(e.toRepositoryAuthenticationFailure())
        }
    }

    override suspend fun logout() {
        //dataSourceBackend.logout()
    }
}

internal fun BackendException.toRepositoryAuthenticationFailure(): IRepositoryAuthentication.RepositoryAuthenticationFailure =
    when (this.errorCode) {
        BackendErrorCode.Http401 -> IRepositoryAuthentication.RepositoryAuthenticationFailure.LoginError

        BackendErrorCode.UnknownHost,
        BackendErrorCode.Timeout,
        BackendErrorCode.SSLError,
        BackendErrorCode.HttpUnmanaged,
        BackendErrorCode.IO -> IRepositoryAuthentication.RepositoryAuthenticationFailure.ConnectionProblems

        BackendErrorCode.Http400,
        BackendErrorCode.Http403,
        BackendErrorCode.NoDataChanges,
        BackendErrorCode.Unexpected -> IRepositoryAuthentication.RepositoryAuthenticationFailure.BackendProblems
    }