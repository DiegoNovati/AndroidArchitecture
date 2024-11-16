package uk.co.itmms.androidArchitecture.data.repositories

import arrow.core.right
import uk.co.itmms.androidArchitecture.data.datasources.IDataSourceBackend
import uk.co.itmms.androidArchitecture.data.datasources.IDataSourceDatabaseTodos
import uk.co.itmms.androidArchitecture.data.extensions.toDBTodoList
import uk.co.itmms.androidArchitecture.data.extensions.toTodoList
import uk.co.itmms.androidArchitecture.domain.repositories.IRepositoryTodos

class RepositoryTodos(
    private val dataSourceBackend: IDataSourceBackend,
    private val dataSourceDatabaseTodos: IDataSourceDatabaseTodos,
) : IRepositoryTodos {

    override suspend fun list(): IRepositoryTodos.Result =
        invokeRepository {
            dataSourceBackend.getTodos().right()
        }.fold({ failure ->
            IRepositoryTodos.Result(
                offline = true,
                todoList = dataSourceDatabaseTodos.list().toTodoList(),
                failure = failure,
            )
        }){ response ->
            val dbTodoList = response.todos.toDBTodoList()
            dataSourceDatabaseTodos.deleteAll()
            dataSourceDatabaseTodos.insert(dbTodoList)
            IRepositoryTodos.Result(
                offline = false,
                todoList = dbTodoList.toTodoList(),
                failure = null,
            )
        }
}