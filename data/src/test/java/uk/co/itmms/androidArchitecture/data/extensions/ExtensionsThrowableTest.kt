package uk.co.itmms.androidArchitecture.data.extensions

import uk.co.itmms.androidArchitecture.data.BaseDataRobolectricTest
import uk.co.itmms.androidArchitecture.data.datasources.network.NotModifiedException
import uk.co.itmms.androidArchitecture.data.datasources.network.BackendErrorCode
import junit.framework.TestCase.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import java.net.SocketTimeoutException

/**
 * The test using Robolectric is required by HttpException (available only with Android SDK)
 */
class ExtensionsThrowableTest: BaseDataRobolectricTest() {

    @Test
    fun testFullStackTraceToString() {
        val message = "exception message"
        val t = RuntimeException(message)

        val actual = t.fullStackTraceToString()

        assertTrue(actual.contains(message))
        assertTrue(actual.contains("RuntimeException"))
    }

    @Test
    fun `SocketTimeoutException to PassApiException`() {
        val actual = SocketTimeoutException().toBackendException()

        assertEquals(actual.errorCode, BackendErrorCode.Timeout)
        assertEquals(actual.errorMessage, "")
        assertNull(actual.errorDisplay)
    }

    @Test
    fun `IOException to PassApiException`() {
        var actual = IOException("Unexpected error").toBackendException()

        assertEquals(actual.errorCode, BackendErrorCode.IO)
        assertEquals(actual.errorMessage, "Unexpected error")
        assertNull(actual.errorDisplay)

        actual = IOException(NotModifiedException()).toBackendException()

        assertEquals(actual.errorCode, BackendErrorCode.NoDataChanges)
        assertEquals(actual.errorMessage, "Data didn't change")
        assertNull(actual.errorDisplay)
    }

    @Test
    // This test uses HttpException and it available only with Robolectric or Android tests
    fun `HttpException with displayError to PassApiException`() {
        val errorMessage = "error message"
        val errorDisplayMessage = "error display message"
        val httpException = createHttpException(401, errorMessage, errorDisplayMessage)

        val actual = httpException.toBackendException()

        assertEquals(actual.errorCode, BackendErrorCode.Http401)
        assertEquals(actual.errorMessage, errorMessage)
        assertEquals(actual.errorDisplay, errorDisplayMessage)
    }

    @Test
    // This test uses HttpException and it available only with Robolectric or Android tests
    fun `HttpException without displayError to PassApiException`() {
        val errorMessage = "error message"
        val httpException = createHttpException(400, errorMessage)

        val actual = httpException.toBackendException()

        assertEquals(actual.errorCode, BackendErrorCode.Http400)
        assertEquals(actual.errorMessage, errorMessage)
        assertNull(actual.errorDisplay)
    }

    @Test
    fun `any other Exception to PassApiException`() {
        val errorMessage = "error message"
        val actual = RuntimeException(errorMessage).toBackendException()

        assertEquals(actual.errorCode, BackendErrorCode.Unexpected)
        assertEquals(actual.errorMessage, errorMessage)
        assertNull(actual.errorDisplay)
    }

    private fun createHttpException(errorCode: Int, errorMessage: String,
                                    errorDisplayMessage: String? = null): HttpException {
        val body = errorDisplayMessage?.let {
            """
                {
                    "error": "$errorMessage",
                    "errorDisplay": "$errorDisplayMessage"
                }
            """
        } ?: run {
            """
                {
                    "error": "$errorMessage"
                }
            """
        }
        val responseBody = body.toResponseBody("application/json".toMediaTypeOrNull())
        return HttpException(Response.error<Any>(errorCode, responseBody))
    }
}