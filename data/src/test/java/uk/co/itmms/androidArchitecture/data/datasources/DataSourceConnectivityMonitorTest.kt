package uk.co.itmms.androidArchitecture.data.datasources

import uk.co.itmms.androidArchitecture.data.BaseDataTest
import uk.co.itmms.androidArchitecture.data.datasources.networkMonitor.IConnectivityChecker
import uk.co.itmms.androidArchitecture.data.datasources.networkMonitor.IConnectivityMonitorCallback
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class DataSourceConnectivityMonitorTest : BaseDataTest() {

    @MockK
    private lateinit var mockConnectivityChecker: IConnectivityChecker

    @MockK
    private lateinit var mockConnectivityMonitorCallback: IConnectivityMonitorCallback

    private lateinit var dataSourceConnectivityMonitor: DataSourceConnectivityMonitor

    @Before
    fun setUp() {
        dataSourceConnectivityMonitor = DataSourceConnectivityMonitor(
            mockConnectivityChecker, mockConnectivityMonitorCallback,
        )
    }

    @Test
    fun `testing monitor`() = runBlocking {
        every { mockConnectivityChecker.isConnected() } returns true
        every { mockConnectivityMonitorCallback.startMonitoring(any()) } answers {
            firstArg<(Boolean) -> Unit>().invoke(false)
            firstArg<(Boolean) -> Unit>().invoke(true)
            firstArg<(Boolean) -> Unit>().invoke(false)
        }

        val actual = dataSourceConnectivityMonitor.monitor().take(4).toList()

        assertEquals(false, actual[0])  // startMonitoring - first value
        assertEquals(true,  actual[1])  // startMonitoring - second value
        assertEquals(false, actual[2])  // startMonitoring - third value
        assertEquals(true,  actual[3])  // isConnected

        verify(exactly = 1) {
            mockConnectivityMonitorCallback.startMonitoring(any())
            mockConnectivityChecker.isConnected()
            mockConnectivityMonitorCallback.stopMonitoring()
        }
    }
}