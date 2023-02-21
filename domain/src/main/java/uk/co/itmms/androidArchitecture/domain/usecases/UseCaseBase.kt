package uk.co.itmms.androidArchitecture.domain.usecases

import arrow.core.Either
import uk.co.itmms.androidArchitecture.domain.failures.UnexpectedError
import uk.co.itmms.androidArchitecture.domain.repositories.IRepositoryDevelopmentAnalytics
import uk.co.itmms.androidArchitecture.domain.repositories.IRepositoryDevelopmentLogger
import kotlinx.coroutines.*
import org.json.JSONObject
import java.util.*

object NoParams

/**
 * Base class for every use case implementing a request/response case or, in other words, a use
 * case that ends when the response has been returned
 *
 * The Failure type must be a 'sealed interface' (see Failures.kt file)
 */
abstract class UseCaseBase<Params, Result, Failure>(
    private val repositoryDevelopmentLogger: IRepositoryDevelopmentLogger,
    private val repositoryDevelopmentAnalytics: IRepositoryDevelopmentAnalytics,
) where Params : Any, Result : Any, Failure : Any {

    abstract suspend fun run(params: Params): Either<Failure, Result>

    fun invoke(
        params: Params,
        scope: CoroutineScope,
        onResult: (result: Either<Failure, Result>) -> Unit,
    ): Job =
        scope.launch(Dispatchers.IO) {
            val startMilliSec = Date().time
            try {
                val result = run(params)
                analyticsUseCase(getExecutionTime(startMilliSec))
                scope.launch(Dispatchers.Main) {
                    onResult(result)
                }
            } catch (e: CancellationException) {
                // Exception raised when a flow is cancelled
            } catch (e: Throwable) {
                logUnexpectedThrowable(params, getExecutionTime(startMilliSec), e)
                scope.launch(Dispatchers.Main) {
                    @Suppress("UNCHECKED_CAST")
                    onResult(Either.Left(UnexpectedError(e) as Failure))
                }
            }
        }

    internal suspend fun analyticsUseCase(milliSec: Long) {
        repositoryDevelopmentAnalytics.logUseCase(javaClass.simpleName, milliSec)
    }

    internal suspend fun logUnexpectedThrowable(params: Params, milliSec: Long, e: Throwable) {
        repositoryDevelopmentLogger.logIssue(
            javaClass.simpleName,
            createJson(
                params = params.toString(),
                milliSec = milliSec,
                error = e.stackTraceToString()
            ),
        )
        repositoryDevelopmentLogger.send()
    }
}

private fun createJson(params: String, milliSec: Long, error: String): String {
    val jsonObject = JSONObject()
    jsonObject.put("params", params)
    jsonObject.put("milliSec", milliSec)
    jsonObject.put("error", error)
    return jsonObject.toString()
}

private fun getExecutionTime(startMilliSec: Long): Long =
    Date().time - startMilliSec