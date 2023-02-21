package uk.co.itmms.androidArchitecture.data.datasources.networkMonitor

import android.content.Context
import android.net.ConnectivityManager
import uk.co.itmms.androidArchitecture.data.BaseDataRobolectricTest
import junit.framework.TestCase
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Ignore
import org.junit.Test
import org.robolectric.shadows.ShadowSettings

@Ignore("Unable to find a solution for the failures due to parallel running on the two SDKs")
class ConnectivityCheckerConnectedTest : BaseDataRobolectricTest() {

    private lateinit var connectivityChecker: ConnectivityChecker

    @Before
    fun setUp() {
        connectivityChecker = ConnectivityChecker(
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        )
    }

    @Test
    fun `testing with connectivity available()`() {
        ShadowSettings.setAirplaneMode(false)

        val actual = connectivityChecker.isConnected()

        assertTrue(actual)
    }

    @Test
    fun `testing in airplane mode`() = runBlocking {
        ShadowSettings.setAirplaneMode(true)

        val actual = connectivityChecker.isConnected()

        TestCase.assertFalse(actual)
    }
}