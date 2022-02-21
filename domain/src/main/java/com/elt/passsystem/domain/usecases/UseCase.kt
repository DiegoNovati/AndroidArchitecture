package com.elt.passsystem.domain.usecases

import arrow.core.Either
import com.elt.passsystem.domain.repositories.IRepositoryAnalytics
import com.elt.passsystem.domain.repositories.IRepositoryLogger
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch

object NoParams

sealed class Failure {
    data class UnexpectedError(val e: Throwable) : Failure()
    abstract class FeatureFailure : Failure()
}

/**
 * Base class for every use case implementing a request/response case or, in other words, a use
 * case that ends when the response has been returned
 */
abstract class UseCaseSingle<Params, Result>(
    repositoryLogger: IRepositoryLogger,
    repositoryAnalytics: IRepositoryAnalytics,
) : UseCaseBase<Params, Result>(repositoryLogger, repositoryAnalytics)
        where Params : Any, Result : Any {

    abstract suspend fun run(params: Params): Either<Failure, Result>

    override suspend fun exec(
        useCaseBase: UseCaseBase<Params, Result>,
        params: Params,
        coroutineScope: CoroutineScope,
        onResult: (result: Either<Failure, Result>) -> Unit
    ) {
        val result = run(params)
        result.fold({
            logError(params, it.toString())
        }) {}
        coroutineScope.launch(Dispatchers.Main) {
            onResult(result)
        }
    }
}

// TODO:
//  1. Verify the correctness of the implementation
//  2. Add tests
/**
 * Base class for every use case that returns periodically a value (i.e.: Internet connection status
 * (online/offline)). The use case never ends, it must be cancelled
 */
abstract class UseCaseStream<Params, Result>(
    repositoryLogger: IRepositoryLogger,
    repositoryAnalytics: IRepositoryAnalytics,
) : UseCaseBase<Params, Result>(repositoryLogger, repositoryAnalytics)
        where Params : Any, Result : Any {

    abstract suspend fun run(params: Params): Either<Failure, Flow<Result>>

    override suspend fun exec(
        useCaseBase: UseCaseBase<Params, Result>,
        params: Params,
        coroutineScope: CoroutineScope,
        onResult: (result: Either<Failure, Result>) -> Unit
    ) {
        val result = run(params)
        result.fold({
            logError(params, it.toString())
            coroutineScope.launch(Dispatchers.Main) {
                onResult(Either.Left(it))
            }
        }) { flow ->
            flow
                .catch { exception ->
                    logUnexpectedThrowable(params, exception)
                    coroutineScope.launch(Dispatchers.Main) {
                        onResult(Either.Left(Failure.UnexpectedError(exception)))
                    }
                }
                .collect { value ->
                    coroutineScope.launch(Dispatchers.Main) {
                        onResult(Either.Right(value))
                    }
                }
        }
    }
}

abstract class UseCaseBase<Params, Result>(
    private val repositoryLogger: IRepositoryLogger,
    private val repositoryAnalytics: IRepositoryAnalytics,
) where Params : Any, Result : Any {

    internal suspend fun logError(params: Params, failure: String) {
        repositoryLogger.logError(javaClass.simpleName, params.toString(), failure)
    }

    internal suspend fun analyticsUseCase() {
        repositoryAnalytics.logUseCase(javaClass.simpleName)
    }

    internal suspend fun logInfo(message: String) {
        repositoryLogger.logInfo(javaClass.simpleName, message)
    }

    internal suspend fun logUnexpectedThrowable(params: Params, e: Throwable) {
        repositoryLogger.logUnexpectedThrowable(javaClass.simpleName, params.toString(), e)
    }

    internal suspend fun logUnexpectedFailure(params: Params, failure: String) {
        repositoryLogger.logUnexpectedFailure(javaClass.simpleName, params.toString(), failure)
    }

    abstract suspend fun exec(
        useCaseBase: UseCaseBase<Params, Result>,
        params: Params,
        coroutineScope: CoroutineScope,
        onResult: (result: Either<Failure, Result>) -> Unit
    )

    fun invoke(
        params: Params,
        scope: CoroutineScope,
        onResult: (result: Either<Failure, Result>) -> Unit,
    ): Job =
        scope.launch(Dispatchers.IO) {
            analyticsUseCase()
            try {
                exec(this@UseCaseBase, params, this, onResult)
            } catch (e: CancellationException) {
                logInfo("job cancelled")
            } catch (e: Throwable) {
                logUnexpectedThrowable(params, e)
                launch(Dispatchers.Main) {
                    onResult(Either.Left(Failure.UnexpectedError(e)))
                }
            }
        }
}