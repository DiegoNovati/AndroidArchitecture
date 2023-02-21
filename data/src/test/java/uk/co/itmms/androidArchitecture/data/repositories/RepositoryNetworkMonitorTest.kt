package uk.co.itmms.androidArchitecture.data.repositories

import uk.co.itmms.androidArchitecture.data.BaseDataTest
import uk.co.itmms.androidArchitecture.data.datasources.IDataSourceConnectivityMonitor
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class RepositoryNetworkMonitorTest : BaseDataTest() {

    @MockK
    private lateinit var mockDataSourceConnectivityMonitor: IDataSourceConnectivityMonitor

    private lateinit var repositoryNetworkMonitor: RepositoryNetworkMonitor

    @Before
    fun setUp() {
        repositoryNetworkMonitor = RepositoryNetworkMonitor(
            mockDataSourceConnectivityMonitor
        )
    }

    @Test
    fun `testing monitor`() = runBlocking {
        val connected = true

        coEvery { mockDataSourceConnectivityMonitor.monitor() } returns flow {
            emit(connected)
        }
        val actual = repositoryNetworkMonitor.monitor().first()

        assertEquals(connected, actual)

        coVerify(exactly = 1) {
            mockDataSourceConnectivityMonitor.monitor()
        }
    }
}