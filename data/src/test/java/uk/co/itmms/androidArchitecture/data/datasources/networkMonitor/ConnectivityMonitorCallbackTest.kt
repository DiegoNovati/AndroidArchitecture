package uk.co.itmms.androidArchitecture.data.datasources.networkMonitor

import android.content.Context
import android.net.ConnectivityManager
import uk.co.itmms.androidArchitecture.data.BaseDataRobolectricTest
import org.junit.Before
import org.junit.Test

class ConnectivityMonitorCallbackTest : BaseDataRobolectricTest() {

    private lateinit var connectivityMonitorCallback: ConnectivityMonitorCallback

    @Before
    fun setUp() {
        connectivityMonitorCallback = ConnectivityMonitorCallback(
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        )
    }

    @Test
    fun `integration test`() {
        // Note: we cannot test the change of the state, so we only test that the implementation
        // doesn't rise any error

        connectivityMonitorCallback.startMonitoring {}
        connectivityMonitorCallback.stopMonitoring()
    }
}