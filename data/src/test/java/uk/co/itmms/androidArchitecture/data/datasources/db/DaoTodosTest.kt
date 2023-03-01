package uk.co.itmms.androidArchitecture.data.datasources.db

import uk.co.itmms.androidArchitecture.data.BaseDataDaoTest
import junit.framework.TestCase.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.junit.Test
import uk.co.itmms.androidArchitecture.data.models.DBTodo

class DaoTodosTest : BaseDataDaoTest() {

    @Test
    fun `testing insert and count`() = runBlocking(Dispatchers.IO) {
        val todo = createTodo(1)

        daoTodos.insert(todo)
        val actual = daoTodos.count()

        assertEquals(1, actual)
    }

    @Test
    fun `testing insert list and count`() = runBlocking(Dispatchers.IO) {
        val todoList = listOf(
            createTodo(1),
            createTodo(2),
        )

        daoTodos.insert(todoList)
        val actual = daoTodos.count()

        assertEquals(todoList.size, actual)
    }

    @Test
    fun `testing list`() = runBlocking(Dispatchers.IO) {
        val todo1 = createTodo(1)
        val todo2 = createTodo(2)
        val todoList = listOf(
            todo1,
            todo2,
        )

        daoTodos.insert(todoList)
        val actual = daoTodos.list()

        assertEquals(2, actual.size)
        assertEquals(todo1.id, actual[0].id)
        assertEquals(todo2.id, actual[1].id)
    }

    @Test
    fun `testing getById with existing and not existing id`() = runBlocking(Dispatchers.IO) {
        val todo = createTodo(1)

        daoTodos.insert(todo)
        var actual = daoTodos.getById(todo.id)

        assertNotNull(actual)

        actual = daoTodos.getById(999999)

        assertNull(actual)
    }

    @Test
    fun `testing update with existing todo`() = runBlocking(Dispatchers.IO) {
        val todo = createTodo(1)
        val newCompleted = true

        daoTodos.insert(todo)
        val dbTodos = daoTodos.getById(todo.id)!!.copy(completed = newCompleted)
        daoTodos.update(dbTodos)

        assertEquals(1, daoTodos.count())
        assertEquals(newCompleted, daoTodos.getById(todo.id)!!.completed)
    }

    @Test
    fun `testing update with NOT existing todo`() = runBlocking(Dispatchers.IO) {
        val todo = createTodo(1)

        daoTodos.update(todo)

        assertEquals(0, daoTodos.count())
        assertNull(daoTodos.getById(todo.id))
    }

    @Test
    fun `test delete single`() = runBlocking(Dispatchers.IO) {
        val todo = createTodo(3)
        val todoList = listOf(
            createTodo(1),
            createTodo(2),
            todo,
            createTodo(4),
        )

        daoTodos.insert(todoList)
        daoTodos.delete(todo)

        assertEquals(todoList.size - 1, daoTodos.count())
        assertNull(daoTodos.getById(todo.id))
    }

    @Test
    fun `test delete multiple`() = runBlocking(Dispatchers.IO) {
        val todo1 = createTodo(1)
        val todo3 = createTodo(3)
        val todoList = listOf(
            todo1,
            createTodo(2),
            todo3,
            createTodo(4),
        )
        val todoToDeleteList = listOf(
            todo1,
            todo3
        )

        daoTodos.insert(todoList)
        daoTodos.delete(todoToDeleteList)

        assertEquals(todoList.size - todoToDeleteList.size, daoTodos.count())
        assertNull(daoTodos.getById(todo1.id))
        assertNull(daoTodos.getById(todo3.id))
    }

    private fun createTodo(id: Long): DBTodo =
        DBTodo(
            id = id,
            todo = "Do something nice for someone I care about",
            completed = false,
        )
}