package uk.co.itmms.androidArchitecture.data.repositories

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.impl.annotations.MockK
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertNull
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.runBlocking
import org.junit.Test
import uk.co.itmms.androidArchitecture.data.BaseDataTest
import uk.co.itmms.androidArchitecture.data.datasources.IDataSourceBackend
import uk.co.itmms.androidArchitecture.data.datasources.IDataSourceDatabaseTodos
import uk.co.itmms.androidArchitecture.data.datasources.network.BackendErrorCode
import uk.co.itmms.androidArchitecture.data.datasources.network.BackendException
import uk.co.itmms.androidArchitecture.data.extensions.toDBTodo
import uk.co.itmms.androidArchitecture.data.extensions.toDBTodoList
import uk.co.itmms.androidArchitecture.data.extensions.toTodoList
import uk.co.itmms.androidArchitecture.data.models.DBTodo
import uk.co.itmms.androidArchitecture.data.models.NetTodoTodo
import uk.co.itmms.androidArchitecture.data.models.NetTodosResponse

class RepositoryTodosTest : BaseDataTest() {

    @MockK
    private lateinit var mockDataSourceBackend: IDataSourceBackend

    @MockK
    private lateinit var mockDataSourceDatabaseTodos: IDataSourceDatabaseTodos

    private lateinit var repositoryTodos: RepositoryTodos

    @Test
    fun `WHEN backend returns todos THEN save them to database`() = runBlocking {
        val netTodoResponse = createNetTodoResponse()

        coEvery { mockDataSourceBackend.getTodos() } returns netTodoResponse

        val actual = repositoryTodos.list()

        assertFalse(actual.offline)
        assertEquals(netTodoResponse.todos.toDBTodoList().toTodoList(), actual.todoList)
        assertNull(actual.failure)

        coVerify(exactly = 1) {
            mockDataSourceBackend.getTodos()
            mockDataSourceDatabaseTodos.deleteAll()
            mockDataSourceDatabaseTodos.insert(any<List<DBTodo>>())
        }
        confirmVerified(mockDataSourceBackend, mockDataSourceDatabaseTodos)
    }

    @Test
    fun `WHEN backend fails THEN returns data from the database`() = runBlocking {
        val dbTodoList = createDBTodoList()

        coEvery { mockDataSourceBackend.getTodos() } throws BackendException(BackendErrorCode.IO)
        coEvery { mockDataSourceDatabaseTodos.list() } returns dbTodoList

        val actual = repositoryTodos.list()

        assertTrue(actual.offline)
        assertEquals(dbTodoList.toTodoList(), actual.todoList)
        assertNull(actual.failure)

        coVerify(exactly = 1) {
            mockDataSourceBackend.getTodos()
            mockDataSourceDatabaseTodos.list()
        }
        confirmVerified(mockDataSourceBackend, mockDataSourceDatabaseTodos)
    }

    private fun createDBTodoList(): List<DBTodo> =
        listOf(
            createNetTodoTodo(1).toDBTodo(),
            createNetTodoTodo(2).toDBTodo(),
            createNetTodoTodo(3).toDBTodo(),
        )

    private fun createNetTodoResponse(): NetTodosResponse =
        NetTodosResponse(
            todos = listOf(
                createNetTodoTodo(1),
                createNetTodoTodo(2),
                createNetTodoTodo(3),
            ),
            total = 100,
            skip = 0,
            limit = 30,
        )

    private fun createNetTodoTodo(id: Long): NetTodoTodo =
        NetTodoTodo(
            id = id,
            todo = "todo message",
            completed = false,
        )
}