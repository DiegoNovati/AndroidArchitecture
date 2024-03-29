package uk.co.itmms.androidArchitecture.data.repositories

import arrow.core.Either
import uk.co.itmms.androidArchitecture.data.datasources.network.PassApiErrorCode
import uk.co.itmms.androidArchitecture.data.datasources.network.PassApiException
import uk.co.itmms.androidArchitecture.domain.repositories.RepositoryBackendFailure
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.runBlocking
import org.junit.Test

class RepositoryUtilitiesTest {

    @Test
    fun `WHEN the default lambda is successful THEN returns a right value`() = runBlocking {
        val result = listOf("one", "two", "three")

        val actual = invokeRepository({
            Either.Right(result)
        }, {
            Either.Left(RepositoryBackendFailure.ConnectionProblems)
        })

        assertTrue(actual.isRight())
        actual.fold({}){
            assertEquals(result, it)
        }
    }

    @Test
    fun `WHEN the default lambda fails AND the exception is NoDataChanges AND onNoDataChanged is successful THEN returns a right value`() =
        runBlocking {
            val result = listOf("one", "two", "three")

            val actual = invokeRepository({
                throw PassApiException(errorCode = PassApiErrorCode.NoDataChanges)
            }, {
                Either.Right(result)
            })

            assertTrue(actual.isRight())
            actual.fold({}){
                assertEquals(result, it)
            }
        }

    @Test
    fun `WHEN the default lambda fails AND the exception is NoDataChanges AND onNoDataChanged fails with NoDataChanges THEN returns a left BackendProblems`() =
        runBlocking {
            val actual = invokeRepository<List<String>>({
                throw PassApiException(errorCode = PassApiErrorCode.NoDataChanges)
            }, {
                throw PassApiException(errorCode = PassApiErrorCode.NoDataChanges)
            })

            assertTrue(actual.isLeft())
            actual.fold({
                assertEquals(RepositoryBackendFailure.BackendProblems, it)
            }){}
        }

    @Test
    fun `WHEN the default lambda fails AND the exception is any other PassApiException THEN returns a left value`() =
        runBlocking {
            val actual = invokeRepository<List<String>>({
                throw PassApiException(errorCode = PassApiErrorCode.NoDataChanges)
            }, {
                throw PassApiException(errorCode = PassApiErrorCode.SSLError)
            })

            assertTrue(actual.isLeft())
            actual.fold({
                assertEquals(RepositoryBackendFailure.ConnectionProblems, it)
            }){}
        }
}