package uk.co.itmms.androidArchitecture.domain.usecases

import arrow.core.Either
import uk.co.itmms.androidArchitecture.domain.BaseDomainTest
import uk.co.itmms.androidArchitecture.domain.failures.BaseFailure
import uk.co.itmms.androidArchitecture.domain.failures.FailureTest
import uk.co.itmms.androidArchitecture.domain.failures.UnexpectedError
import uk.co.itmms.androidArchitecture.domain.repositories.IRepositoryDevelopmentAnalytics
import uk.co.itmms.androidArchitecture.domain.repositories.IRepositoryDevelopmentLogger
import io.mockk.coVerify
import io.mockk.confirmVerified
import junit.framework.TestCase
import kotlinx.coroutines.*
import kotlinx.coroutines.test.setMain
import org.junit.Test

@ExperimentalCoroutinesApi
class UseCaseBaseTest : BaseDomainTest() {

    private val params = "my param"
    private val milliSec = 1234L

    @Test
    fun `WHEN the run is successful THEN it returns the success`() = runBlocking {
        Dispatchers.setMain((Dispatchers.Unconfined))
        var actual: Either<FailureTest.UnknownProblem, Boolean>? = null
        val onResult = { onResult: Either<FailureTest.UnknownProblem, Boolean> ->
            actual = onResult
        }

        withContext(Dispatchers.Default) {
            FakeUseCaseBase(mockRepositoryDevelopmentLogger, mockRepositoryDevelopmentAnalytics).invoke(
                params,
                this,
                onResult = onResult
            )
        }

        TestCase.assertNotNull(actual)
        actual?.fold({
            TestCase.fail("It's not a failure")
        }) {
            TestCase.assertTrue(it)
        }

        coVerify(exactly = 1) {
            mockRepositoryDevelopmentAnalytics.logUseCase(any(), any())
        }
        confirmVerified(mockRepositoryDevelopmentLogger)
    }

    @Test
    fun `WHEN the run returns a failure THEN it returns the failure`() = runBlocking {
        Dispatchers.setMain((Dispatchers.Unconfined))

        var actual: Either<FailureTest.UnknownProblem, Boolean>? = null
        val onResult = { onResult: Either<FailureTest.UnknownProblem, Boolean> ->
            actual = onResult
        }

        withContext(Dispatchers.Default) {
            FailureUseCaseBase(mockRepositoryDevelopmentLogger, mockRepositoryDevelopmentAnalytics)
                .invoke(params, this, onResult = onResult)
        }

        TestCase.assertNotNull(actual)
        actual?.fold({
        }) {
            TestCase.fail("Failure expected")
        }

        coVerify(exactly = 1) {
            mockRepositoryDevelopmentAnalytics.logUseCase(any(), any())
        }
        confirmVerified(mockRepositoryDevelopmentLogger)
    }

    @DelicateCoroutinesApi
    @Test
    fun `WHEN the useCase is taking too much time THEN it can be killed`() {
        val job = SlowUseCaseBase(mockRepositoryDevelopmentLogger, mockRepositoryDevelopmentAnalytics).invoke(
            params,
            GlobalScope,
            onResult = {})

        TestCase.assertTrue(job.isActive)

        job.cancel()

        TestCase.assertFalse(job.isActive)
    }

    @Test
    fun `WHEN the run raises an exception THEN it returns the exception`() = runBlocking {
        Dispatchers.setMain(Dispatchers.Unconfined)

        var actual: Either<BaseFailure, Boolean>? = null
        val onResult = { onResult: Either<BaseFailure, Boolean> ->
            actual = onResult
        }

        val params = RuntimeException("Simulating an exception")

        withContext(Dispatchers.Default) {
            ExceptionUseCaseBase(mockRepositoryDevelopmentLogger, mockRepositoryDevelopmentAnalytics).invoke(
                params,
                this,
                onResult = onResult
            )
        }

        TestCase.assertNotNull(actual)
        actual?.fold({
            TestCase.assertEquals(UnexpectedError(params), it)
        }) {
            TestCase.fail("Failure expected")
        }

        coVerify(exactly = 1) {
            mockRepositoryDevelopmentLogger.logIssue(any(), any())
            mockRepositoryDevelopmentLogger.send()
        }
        confirmVerified(mockRepositoryDevelopmentLogger)
    }

    @Test
    fun `WHEN analyticsUseCase invoked THEN it uses the repository`() = runBlocking {
        Dispatchers.setMain(Dispatchers.Unconfined)

        FakeUseCaseBase(mockRepositoryDevelopmentLogger, mockRepositoryDevelopmentAnalytics).analyticsUseCase(milliSec)

        coVerify(exactly = 1) {
            mockRepositoryDevelopmentAnalytics.logUseCase("FakeUseCaseBase", milliSec)
        }
        confirmVerified(mockRepositoryDevelopmentAnalytics)
    }

    @Test
    fun `WHEN logUnexpectedThrowable invoked THEN it uses the repository`() = runBlocking {
        Dispatchers.setMain(Dispatchers.Unconfined)

        val throwable = RuntimeException("unexpected exception")
        FakeUseCaseBase(
            mockRepositoryDevelopmentLogger,
            mockRepositoryDevelopmentAnalytics
        ).logUnexpectedThrowable(params, milliSec, throwable)

        coVerify(exactly = 1) {
            mockRepositoryDevelopmentLogger.logIssue("FakeUseCaseBase", any())
            mockRepositoryDevelopmentLogger.send()
        }
        confirmVerified(mockRepositoryDevelopmentLogger)
    }

    class FakeUseCaseBase(
        repositoryLogger: IRepositoryDevelopmentLogger,
        repositoryAnalytics: IRepositoryDevelopmentAnalytics,
    ) :
        UseCaseBase<String, Boolean, FailureTest.UnknownProblem>(repositoryLogger, repositoryAnalytics) {
        override suspend fun run(params: String): Either<FailureTest.UnknownProblem, Boolean> =
            Either.Right(params.isNotEmpty())
    }

    class FailureUseCaseBase(
        repositoryLogger: IRepositoryDevelopmentLogger,
        repositoryAnalytics: IRepositoryDevelopmentAnalytics,
    ) :
        UseCaseBase<String, Boolean, FailureTest.UnknownProblem>(repositoryLogger, repositoryAnalytics) {
        override suspend fun run(params: String): Either<FailureTest.UnknownProblem, Boolean> =
            Either.Left(FailureTest.UnknownProblem)
    }

    class SlowUseCaseBase(
        repositoryLogger: IRepositoryDevelopmentLogger,
        repositoryAnalytics: IRepositoryDevelopmentAnalytics,
    ) :
        UseCaseBase<String, Boolean, BaseFailure>(repositoryLogger, repositoryAnalytics) {
        override suspend fun run(params: String): Either<BaseFailure, Boolean> {
            delay(10000)
            return Either.Right(params.isNotEmpty())
        }
    }

    class ExceptionUseCaseBase(
        repositoryLogger: IRepositoryDevelopmentLogger,
        repositoryAnalytics: IRepositoryDevelopmentAnalytics,
    ) :
        UseCaseBase<Exception, Boolean, BaseFailure>(repositoryLogger, repositoryAnalytics) {
        override suspend fun run(params: Exception): Either<BaseFailure, Boolean> {
            throw params
        }
    }
}