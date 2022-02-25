package com.elt.passsystem.domain.usecases.networkMonitor

import arrow.core.Either
import com.elt.passsystem.domain.entities.NoFailure
import com.elt.passsystem.domain.repositories.IRepositoryAnalytics
import com.elt.passsystem.domain.repositories.IRepositoryLogger
import com.elt.passsystem.domain.repositories.IRepositoryNetworkMonitor
import com.elt.passsystem.domain.usecases.NoParams
import com.elt.passsystem.domain.usecases.UseCaseStream
import kotlinx.coroutines.flow.Flow

class UseCaseNetworkMonitor(
    repositoryLogger: IRepositoryLogger,
    repositoryAnalytics: IRepositoryAnalytics,
    private val repositoryNetworkMonitor: IRepositoryNetworkMonitor,
): UseCaseStream<NoParams, Boolean, NoFailure>(
    repositoryLogger, repositoryAnalytics
) {
    override suspend fun run(params: NoParams): Either<NoFailure, Flow<Boolean>> =
        monitor()

    internal suspend fun monitor(): Either<NoFailure, Flow<Boolean>> =
        repositoryNetworkMonitor
            .monitor()
            .fold({
                Either.Left(NoFailure.NoError)
            }){
                Either.Right(it)
            }
}