package uk.co.itmms.androidArchitecture.domain.usecases.home

import arrow.core.Either
import uk.co.itmms.androidArchitecture.domain.failures.FailureHome
import uk.co.itmms.androidArchitecture.domain.repositories.IRepositoryDevelopmentAnalytics
import uk.co.itmms.androidArchitecture.domain.repositories.IRepositoryAuthentication
import uk.co.itmms.androidArchitecture.domain.repositories.IRepositoryDevelopmentLogger
import uk.co.itmms.androidArchitecture.domain.repositories.IRepositoryRuntime
import uk.co.itmms.androidArchitecture.domain.usecases.NoParams
import uk.co.itmms.androidArchitecture.domain.usecases.UseCaseBase

class UseCaseHomeLogout(
    repositoryLogger: IRepositoryDevelopmentLogger,
    repositoryAnalytics: IRepositoryDevelopmentAnalytics,
    private val repositoryAuthentication: IRepositoryAuthentication,
    private val repositoryRuntime: IRepositoryRuntime,
): UseCaseBase<NoParams, Unit, FailureHome>(
    repositoryLogger, repositoryAnalytics
) {
    override suspend fun run(params: NoParams): Either<FailureHome, Unit> {
        repositoryAuthentication.logout()
        repositoryRuntime.clear()
        return Either.Right(Unit)
    }
}