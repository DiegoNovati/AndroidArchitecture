package com.elt.passsystem.data.repositories

import arrow.core.Either
import com.elt.passsystem.data.datasources.IDataSourceBackend
import com.elt.passsystem.data.datasources.network.PassApiErrorCode
import com.elt.passsystem.data.datasources.network.PassApiException
import com.elt.passsystem.domain.entities.Login
import com.elt.passsystem.domain.repositories.IRepositoryAuthentication

class RepositoryAuthentication(
    private val dataSourceBackend: IDataSourceBackend,
) : IRepositoryAuthentication {

    override suspend fun login(
        userName: String,
        password: String
    ): Either<IRepositoryAuthentication.RepositoryAuthenticationFailure, Login> {
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
                Login(
                    officeBid = officesList[0].bid,
                )
            )
        } catch (e: PassApiException) {
            return Either.Left(e.toRepositoryAuthenticationFailure())
        }
    }

    override suspend fun logout() {
        dataSourceBackend.logout()
    }
}

internal fun PassApiException.toRepositoryAuthenticationFailure(): IRepositoryAuthentication.RepositoryAuthenticationFailure =
    when (this.errorCode) {
        PassApiErrorCode.Http401 -> IRepositoryAuthentication.RepositoryAuthenticationFailure.LoginError

        PassApiErrorCode.UnknownHost,
        PassApiErrorCode.Timeout,
        PassApiErrorCode.SSLError,
        PassApiErrorCode.HttpUnmanaged,
        PassApiErrorCode.IO -> IRepositoryAuthentication.RepositoryAuthenticationFailure.ConnectionProblems

        PassApiErrorCode.Http400,
        PassApiErrorCode.Http403,
        PassApiErrorCode.NoDataChanges,
        PassApiErrorCode.Unexpected -> IRepositoryAuthentication.RepositoryAuthenticationFailure.BackendProblems
    }