package uk.co.itmms.androidArchitecture.domain.usecases.login

import arrow.core.Either
import arrow.core.right
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import uk.co.itmms.androidArchitecture.domain.failures.FailureLogin
import uk.co.itmms.androidArchitecture.domain.repositories.IRepositoryDevelopmentAnalytics
import uk.co.itmms.androidArchitecture.domain.repositories.IRepositoryDevelopmentLogger
import uk.co.itmms.androidArchitecture.domain.repositories.IRepositoryNetworkMonitor
import uk.co.itmms.androidArchitecture.domain.repositories.IRepositoryRuntime
import uk.co.itmms.androidArchitecture.domain.repositories.IRepositorySession
import uk.co.itmms.androidArchitecture.domain.usecases.NoParams
import uk.co.itmms.androidArchitecture.domain.usecases.UseCaseBase

class UseCaseLoginMonitor(
    repositoryDevelopmentLogger: IRepositoryDevelopmentLogger,
    repositoryDevelopmentAnalytics: IRepositoryDevelopmentAnalytics,
    private val repositoryNetworkMonitor: IRepositoryNetworkMonitor,
    private val repositoryRuntime: IRepositoryRuntime,
    private val repositorySession: IRepositorySession,
) : UseCaseBase<NoParams, UseCaseLoginMonitor.Result, FailureLogin>(
    repositoryDevelopmentLogger, repositoryDevelopmentAnalytics
) {
    enum class UpdateType {
        Connected, Authentication
    }

    data class Update(
        val updateType: UpdateType,
        val value: Boolean,
    )

    data class Result(
        val update: Flow<Update>,
    )

    override suspend fun run(params: NoParams): Either<FailureLogin, Result> {
        val connectedFlow = repositoryNetworkMonitor.monitor()
        val authenticatedFlow = repositoryRuntime.isAuthenticatedFlow()
        return Result(
            update = merge(
                connectedFlow.map {
                    Update(UpdateType.Connected, it)
                },
                authenticatedFlow.map {
                    if (!it) {
                        repositorySession.clear()
                    }
                    Update(UpdateType.Authentication, it)
                }
            ),
        ).right()
    }
}