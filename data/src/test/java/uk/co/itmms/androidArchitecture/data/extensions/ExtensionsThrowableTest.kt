package uk.co.itmms.androidArchitecture.data.extensions

import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNull
import junit.framework.TestCase.assertTrue
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Response
import uk.co.itmms.androidArchitecture.data.BaseDataRobolectricTest
import uk.co.itmms.androidArchitecture.data.datasources.network.NotModifiedException
import uk.co.itmms.androidArchitecture.data.datasources.network.NetworkApiErrorCode
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
    fun `SocketTimeoutException to NetworkApiException`() {
        val actual = SocketTimeoutException().toNetworkApiException()

        assertEquals(actual.errorCode, NetworkApiErrorCode.Timeout)
        assertEquals(actual.errorMessage, "")
        assertNull(actual.errorDisplay)
    }

    @Test
    fun `IOException to NetworkApiException`() {
        var actual = IOException("Unexpected error").toNetworkApiException()

        assertEquals(actual.errorCode, NetworkApiErrorCode.IO)
        assertEquals(actual.errorMessage, "Unexpected error")
        assertNull(actual.errorDisplay)

        actual = IOException(NotModifiedException()).toNetworkApiException()

        assertEquals(actual.errorCode, NetworkApiErrorCode.NoDataChanges)
        assertEquals(actual.errorMessage, "Data didn't change")
        assertNull(actual.errorDisplay)
    }

    @Test
    // This test uses HttpException and it available only with Robolectric or Android tests
    fun `HttpException with displayError to NetworkApiException`() {
        val errorMessage = "error message"
        val errorDisplayMessage = "error display message"
        val httpException = createHttpException(401, errorMessage, errorDisplayMessage)

        val actual = httpException.toNetworkApiException()

        assertEquals(actual.errorCode, NetworkApiErrorCode.Http401)
        assertEquals(actual.errorMessage, errorMessage)
        assertEquals(actual.errorDisplay, errorDisplayMessage)
    }

    @Test
    // This test uses HttpException and it available only with Robolectric or Android tests
    fun `HttpException without displayError to NetworkApiException`() {
        val errorMessage = "error message"
        val httpException = createHttpException(400, errorMessage)

        val actual = httpException.toNetworkApiException()

        assertEquals(actual.errorCode, NetworkApiErrorCode.Http400)
        assertEquals(actual.errorMessage, errorMessage)
        assertNull(actual.errorDisplay)
    }

    @Test
    fun `any other Exception to NetworkApiException`() {
        val errorMessage = "error message"
        val actual = RuntimeException(errorMessage).toNetworkApiException()

        assertEquals(actual.errorCode, NetworkApiErrorCode.Unexpected)
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