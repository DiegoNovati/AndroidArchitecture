package uk.co.itmms.androidArchitecture.domain.usecases.home

import arrow.core.Either
import arrow.core.right
import kotlinx.coroutines.flow.Flow
import uk.co.itmms.androidArchitecture.domain.failures.FailureHome
import uk.co.itmms.androidArchitecture.domain.repositories.IRepositoryDevelopmentAnalytics
import uk.co.itmms.androidArchitecture.domain.repositories.IRepositoryDevelopmentLogger
import uk.co.itmms.androidArchitecture.domain.repositories.IRepositoryNetworkMonitor
import uk.co.itmms.androidArchitecture.domain.usecases.NoParams
import uk.co.itmms.androidArchitecture.domain.usecases.UseCaseBase

class UseCaseHomeMonitor(
    repositoryDevelopmentLogger: IRepositoryDevelopmentLogger,
    repositoryDevelopmentAnalytics: IRepositoryDevelopmentAnalytics,
    private val repositoryNetworkMonitor: IRepositoryNetworkMonitor,
) : UseCaseBase<NoParams, UseCaseHomeMonitor.Result, FailureHome>(
    repositoryDevelopmentLogger, repositoryDevelopmentAnalytics,
) {
    data class Result(
        val connected: Flow<Boolean>,
    )
    override suspend fun run(params: NoParams): Either<FailureHome, Result> =
        Result(connected = repositoryNetworkMonitor.monitor()).right()
}