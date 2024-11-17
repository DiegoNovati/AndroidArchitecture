package uk.co.itmms.androidArchitecture.domain.usecases.home

import arrow.core.Either
import uk.co.itmms.androidArchitecture.domain.failures.FailureHome
import uk.co.itmms.androidArchitecture.domain.repositories.IRepositoryAuthentication
import uk.co.itmms.androidArchitecture.domain.repositories.IRepositoryDevelopmentAnalytics
import uk.co.itmms.androidArchitecture.domain.repositories.IRepositoryDevelopmentLogger
import uk.co.itmms.androidArchitecture.domain.repositories.IRepositorySession
import uk.co.itmms.androidArchitecture.domain.usecases.NoParams
import uk.co.itmms.androidArchitecture.domain.usecases.UseCaseBase

class UseCaseHomeLogout(
    repositoryDevelopmentLogger: IRepositoryDevelopmentLogger,
    repositoryDevelopmentAnalytics: IRepositoryDevelopmentAnalytics,
    private val repositoryAuthentication: IRepositoryAuthentication,
    private val repositorySession: IRepositorySession,
): UseCaseBase<NoParams, Unit, FailureHome>(
    repositoryDevelopmentLogger, repositoryDevelopmentAnalytics,
) {
    override suspend fun run(params: NoParams): Either<FailureHome, Unit> {
        repositoryAuthentication.logout()
        repositorySession.clear()
        return Either.Right(Unit)
    }
}