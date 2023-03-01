package uk.co.itmms.androidArchitecture.data.extensions

import uk.co.itmms.androidArchitecture.data.models.DBTodo
import uk.co.itmms.androidArchitecture.data.models.NetTodoTodo
import uk.co.itmms.androidArchitecture.data.models.NetTodosResponse
import uk.co.itmms.androidArchitecture.domain.entities.Todo

fun NetTodoTodo.toDBTodo(): DBTodo =
    DBTodo(
        id = id,
        todo = todo,
        completed = completed,
    )

fun List<NetTodoTodo>.toDBTodoList(): List<DBTodo> =
    this.map { it.toDBTodo() }

fun NetTodosResponse.toDBTodoList(): List<DBTodo> =
    this.todos.toDBTodoList()

fun DBTodo.toTodo(): Todo =
    Todo(
        id = id,
        todo = todo,
        completed = completed,
    )

fun List<DBTodo>.toTodoList(): List<Todo> =
    this.map { it.toTodo() }