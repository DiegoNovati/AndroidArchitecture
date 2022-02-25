package com.elt.passsystem.data.repositories

import com.elt.passsystem.data.BaseDataTest
import com.elt.passsystem.data.datasources.IDataSourceConnectivityMonitor
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
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
        val actual = repositoryNetworkMonitor.monitor()

        assertTrue(actual.isRight())
        actual.fold({}){
            assertEquals(connected, it.first())
        }

        coVerify(exactly = 1) {
            mockDataSourceConnectivityMonitor.monitor()
        }
    }
}