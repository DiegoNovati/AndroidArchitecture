package uk.co.itmms.androidArchitecture.data.repositories

import arrow.core.Either
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.runBlocking
import org.junit.Test
import uk.co.itmms.androidArchitecture.data.datasources.network.BackendErrorCode
import uk.co.itmms.androidArchitecture.data.datasources.network.BackendException
import uk.co.itmms.androidArchitecture.domain.failures.FailureRepositoryBackend

class RepositoryUtilitiesTest {

    @Test
    fun `WHEN the default lambda is successful THEN returns a right value`() = runBlocking {
        val result = listOf("one", "two", "three")

        val actual = invokeRepository {
            Either.Right(result)
        }

        assertTrue(actual.isRight())
        actual.fold({}){
            assertEquals(result, it)
        }
    }

    @Test
    fun `WHEN the default lambda fails AND the exception is any other PassApiException THEN returns a left value`() =
        runBlocking {
            val actual = invokeRepository<List<String>> {
                throw BackendException(errorCode = BackendErrorCode.SSLError)
            }

            assertTrue(actual.isLeft())
            actual.fold({
                assertEquals(FailureRepositoryBackend.ConnectionError, it)
            }){}
        }
}