package com.elt.passsystem.domain.usecases.authentication

import arrow.core.Either
import com.elt.passsystem.domain.repositories.IRepositoryAnalytics
import com.elt.passsystem.domain.repositories.IRepositoryAuthentication
import com.elt.passsystem.domain.repositories.IRepositoryLogger
import com.elt.passsystem.domain.usecases.Failure
import com.elt.passsystem.domain.usecases.NoParams
import com.elt.passsystem.domain.usecases.UseCaseSingle

class UseCaseAuthenticationLogout(
    repositoryLogger: IRepositoryLogger,
    repositoryAnalytics: IRepositoryAnalytics,
    private val repositoryAuthentication: IRepositoryAuthentication,
): UseCaseSingle<NoParams, Unit>(
    repositoryLogger, repositoryAnalytics
) {
    override suspend fun run(params: NoParams): Either<Failure, Unit> {
        repositoryAuthentication.logout()
        return Either.Right(Unit)
    }
}