package com.elt.passsystem.domain.usecases

import arrow.core.Either
import com.elt.passsystem.domain.BaseDomainTest
import com.elt.passsystem.domain.entities.AuthenticationLoginFailure
import com.elt.passsystem.domain.entities.BaseFailure
import com.elt.passsystem.domain.entities.UnexpectedError
import com.elt.passsystem.domain.repositories.IRepositoryAnalytics
import com.elt.passsystem.domain.repositories.IRepositoryLogger
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.impl.annotations.MockK
import junit.framework.TestCase.*
import kotlinx.coroutines.*
import kotlinx.coroutines.test.setMain
import org.junit.Test

@ExperimentalCoroutinesApi
class UseCaseSingleTest : BaseDomainTest() {

    @MockK
    private lateinit var mockRepositoryLogger: IRepositoryLogger

    @MockK
    private lateinit var mockRepositoryAnalytics: IRepositoryAnalytics

    private val params = "my param"

    @Test
    fun `WHEN the run is successful THEN it returns the success`() = runBlocking {
        Dispatchers.setMain((Dispatchers.Unconfined))
        var actual: Either<AuthenticationLoginFailure, Boolean>? = null
        val onResult = { onResult: Either<AuthenticationLoginFailure, Boolean> ->
            actual = onResult
        }

        withContext(Dispatchers.Default) {
            FakeUseCaseSingle(mockRepositoryLogger, mockRepositoryAnalytics).invoke(
                params,
                this,
                onResult = onResult
            )
        }

        assertNotNull(actual)
        actual?.fold({
            fail("It's not a failure")
        }) {
            assertTrue(it)
        }

        coVerify(exactly = 1) {
            mockRepositoryAnalytics.logUseCase(any())
        }
        confirmVerified(mockRepositoryLogger)
    }

    @Test
    fun `WHEN the run returns a failure THEN it returns the failure`() = runBlocking {
        Dispatchers.setMain((Dispatchers.Unconfined))

        var actual: Either<AuthenticationLoginFailure, Boolean>? = null
        val onResult = { onResult: Either<AuthenticationLoginFailure, Boolean> ->
            actual = onResult
        }

        withContext(Dispatchers.Default) {
            FailureUseCaseSingle(mockRepositoryLogger, mockRepositoryAnalytics)
                .invoke(params, this, onResult = onResult)
        }

        assertNotNull(actual)
        actual?.fold({
        }) {
            fail("Failure expected")
        }

        coVerify(exactly = 1) {
            mockRepositoryAnalytics.logUseCase(any())
            mockRepositoryLogger.logError(any(), any(), any())
        }
        confirmVerified(mockRepositoryLogger)
    }

    @DelicateCoroutinesApi
    @Test
    fun `WHEN the useCase is taking too much time THEN it can be killed`() {
        val job = SlowUseCaseSingle(mockRepositoryLogger, mockRepositoryAnalytics).invoke(
            params,
            GlobalScope,
            onResult = {})

        assertTrue(job.isActive)

        job.cancel()

        assertFalse(job.isActive)
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
            ExceptionUseCaseSingle(mockRepositoryLogger, mockRepositoryAnalytics).invoke(
                params,
                this,
                onResult = onResult
            )
        }

        assertNotNull(actual)
        actual?.fold({
            assertEquals(UnexpectedError(params), it)
        }) {
            fail("Failure expected")
        }

        coVerify(exactly = 1) {
            mockRepositoryAnalytics.logUseCase(any())
            mockRepositoryLogger.logUnexpectedThrowable(any(), any(), any())
        }
        confirmVerified(mockRepositoryLogger)
    }

    @Test
    fun `WHEN logError invoked THEN it uses the repository`() = runBlocking {
        Dispatchers.setMain(Dispatchers.Unconfined)

        val failure = "a failure"
        FakeUseCaseSingle(mockRepositoryLogger, mockRepositoryAnalytics).logError(params, failure)

        coVerify(exactly = 1) {
            mockRepositoryLogger.logError("FakeUseCaseSingle", params, failure)
        }
        confirmVerified(mockRepositoryLogger)
    }

    @Test
    fun `WHEN analyticsUseCase invoked THEN it uses the repository`() = runBlocking {
        Dispatchers.setMain(Dispatchers.Unconfined)

        FakeUseCaseSingle(mockRepositoryLogger, mockRepositoryAnalytics).analyticsUseCase()

        coVerify(exactly = 1) {
            mockRepositoryAnalytics.logUseCase("FakeUseCaseSingle")
        }
        confirmVerified(mockRepositoryAnalytics)
    }

    @Test
    fun `WHEN logInfo invoked THEN it uses the repository`() = runBlocking {
        Dispatchers.setMain(Dispatchers.Unconfined)

        val message = "a message"
        FakeUseCaseSingle(mockRepositoryLogger, mockRepositoryAnalytics).logInfo(message)

        coVerify(exactly = 1) {
            mockRepositoryLogger.logInfo("FakeUseCaseSingle", message)
        }
        confirmVerified(mockRepositoryLogger)
    }

    @Test
    fun `WHEN logUnexpectedThrowable invoked THEN it uses the repository`() = runBlocking {
        Dispatchers.setMain(Dispatchers.Unconfined)

        val throwable = RuntimeException("unexpected exception")
        FakeUseCaseSingle(
            mockRepositoryLogger,
            mockRepositoryAnalytics
        ).logUnexpectedThrowable(params, throwable)

        coVerify(exactly = 1) {
            mockRepositoryLogger.logUnexpectedThrowable("FakeUseCaseSingle", params, throwable)
        }
        confirmVerified(mockRepositoryLogger)
    }

    @Test
    fun `WHEN logUnexpectedFailure invoked THEN it uses the repository`() = runBlocking {
        Dispatchers.setMain(Dispatchers.Unconfined)

        val failure = "a failure"
        FakeUseCaseSingle(
            mockRepositoryLogger,
            mockRepositoryAnalytics
        ).logUnexpectedFailure(params, failure)

        coVerify(exactly = 1) {
            mockRepositoryLogger.logUnexpectedFailure("FakeUseCaseSingle", params, failure)
        }
        confirmVerified(mockRepositoryLogger)
    }

    class FakeUseCaseSingle(
        repositoryLogger: IRepositoryLogger,
        repositoryAnalytics: IRepositoryAnalytics,
    ) :
        UseCaseSingle<String, Boolean, AuthenticationLoginFailure>(repositoryLogger, repositoryAnalytics) {
        override suspend fun run(params: String): Either<AuthenticationLoginFailure, Boolean> =
            Either.Right(params.isNotEmpty())
    }

    class FailureUseCaseSingle(
        repositoryLogger: IRepositoryLogger,
        repositoryAnalytics: IRepositoryAnalytics,
    ) :
        UseCaseSingle<String, Boolean, AuthenticationLoginFailure>(repositoryLogger, repositoryAnalytics) {
        override suspend fun run(params: String): Either<AuthenticationLoginFailure, Boolean> =
            Either.Left(AuthenticationLoginFailure.ConnectionProblems)
    }

    class SlowUseCaseSingle(
        repositoryLogger: IRepositoryLogger,
        repositoryAnalytics: IRepositoryAnalytics,
    ) :
        UseCaseSingle<String, Boolean, BaseFailure>(repositoryLogger, repositoryAnalytics) {
        override suspend fun run(params: String): Either<BaseFailure, Boolean> {
            delay(10000)
            return Either.Right(params.isNotEmpty())
        }
    }

    class ExceptionUseCaseSingle(
        repositoryLogger: IRepositoryLogger,
        repositoryAnalytics: IRepositoryAnalytics,
    ) :
        UseCaseSingle<Exception, Boolean, BaseFailure>(repositoryLogger, repositoryAnalytics) {
        override suspend fun run(params: Exception): Either<BaseFailure, Boolean> {
            throw params
        }
    }
}