package uk.co.itmms.androidArchitecture.data.datasources

import uk.co.itmms.androidArchitecture.data.BaseDataTest
import uk.co.itmms.androidArchitecture.data.datasources.db.DaoTodos
import uk.co.itmms.androidArchitecture.data.datasources.db.IAppDatabase
import uk.co.itmms.androidArchitecture.data.models.DBTodo
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class DataSourceDatabaseTodosTest: BaseDataTest() {

    @MockK
    private lateinit var mockAppDatabase: IAppDatabase

    @MockK
    private lateinit var mockDaoTodos: DaoTodos

    private lateinit var dataSourceDatabaseTodo: DataSourceDatabaseTodos

    @Before
    fun setUp() {
        dataSourceDatabaseTodo = DataSourceDatabaseTodos(
            mockAppDatabase
        )

        coEvery { mockAppDatabase.daoTodos() } returns mockDaoTodos
    }

    @Test
    fun `testing list`() = runBlocking {
        val mockList = listOf<DBTodo>(mockk(), mockk())
        coEvery { mockDaoTodos.list() } returns mockList

        val actual = dataSourceDatabaseTodo.list()

        assertEquals(mockList, actual)
        coVerify(exactly = 1) {
            mockDaoTodos.list()
        }
        confirmVerified(mockDaoTodos)
    }

    @Test
    fun `testing get`() = runBlocking {
        val id = 1234L
        val mockTodo = mockk<DBTodo>()

        coEvery { mockDaoTodos.getById(any()) } returns mockTodo

        val actual = dataSourceDatabaseTodo.get(id)

        assertEquals(mockTodo, actual)

        coVerify(exactly = 1) {
            mockDaoTodos.getById(id)
        }
        confirmVerified(mockDaoTodos)
    }

    @Test
    fun `testing insert`() = runBlocking {
        val mockTodo = mockk<DBTodo>()

        dataSourceDatabaseTodo.insert(mockTodo)

        coVerify(exactly = 1) {
            mockDaoTodos.insert(mockTodo)
        }
        confirmVerified(mockDaoTodos)
    }

    @Test
    fun `testing insert list`() = runBlocking {
        val mockList = listOf<DBTodo>(mockk(), mockk())

        dataSourceDatabaseTodo.insert(mockList)

        coVerify(exactly = 1) {
            mockDaoTodos.insert(mockList)
        }
        confirmVerified(mockDaoTodos)
    }

    @Test
    fun `testing update`() = runBlocking {
        val mockTodo = mockk<DBTodo>()

        dataSourceDatabaseTodo.update(mockTodo)

        coVerify(exactly = 1) {
            mockDaoTodos.update(mockTodo)
        }
        confirmVerified(mockDaoTodos)
    }

    @Test
    fun `testing update list`() = runBlocking {
        val mockTodo1 = mockk<DBTodo>()
        val mockTodo2 = mockk<DBTodo>()
        val mockList = listOf(mockTodo1, mockTodo2)

        dataSourceDatabaseTodo.update(mockList)

        coVerify(exactly = 1) {
            mockDaoTodos.update(mockTodo1)
            mockDaoTodos.update(mockTodo2)
        }
        confirmVerified(mockDaoTodos)
    }

    @Test
    fun `testing delete`() = runBlocking {
        val mockTodo = mockk<DBTodo>()

        dataSourceDatabaseTodo.delete(mockTodo)

        coVerify(exactly = 1) {
            mockDaoTodos.delete(mockTodo)
        }
        confirmVerified(mockDaoTodos)
    }

    @Test
    fun `testing deleteAll`() = runBlocking {
        val mockTodo1 = mockk<DBTodo>()
        val mockTodo2 = mockk<DBTodo>()
        val mockList = listOf(mockTodo1, mockTodo2)

        coEvery { mockDaoTodos.list() } returns mockList

        dataSourceDatabaseTodo.deleteAll()

        coVerify(exactly = 1) {
            mockDaoTodos.list()
            mockDaoTodos.delete(mockTodo1)
            mockDaoTodos.delete(mockTodo2)
        }
    }
}