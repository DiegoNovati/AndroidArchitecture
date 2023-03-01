package uk.co.itmms.androidArchitecture.data.repositories

import arrow.core.Either
import arrow.core.right
import uk.co.itmms.androidArchitecture.data.datasources.IDataSourceBackend
import uk.co.itmms.androidArchitecture.data.datasources.IDataSourceDatabaseTodos
import uk.co.itmms.androidArchitecture.data.extensions.toDBTodoList
import uk.co.itmms.androidArchitecture.data.extensions.toTodoList
import uk.co.itmms.androidArchitecture.domain.entities.Todo
import uk.co.itmms.androidArchitecture.domain.failures.FailureRepositoryBackend
import uk.co.itmms.androidArchitecture.domain.repositories.IRepositoryTodos

class RepositoryTodos(
    private val dataSourceBackend: IDataSourceBackend,
    private val dataSourceDatabaseTodos: IDataSourceDatabaseTodos,
) : IRepositoryTodos {

    override suspend fun list(): Either<FailureRepositoryBackend, List<Todo>> =
        invokeRepository({
            val netTodosResponse = dataSourceBackend.getTodos()

            val dbTodoList = netTodosResponse.todos.toDBTodoList()
            dataSourceDatabaseTodos.deleteAll()
            dataSourceDatabaseTodos.insert(dbTodoList)
            dbTodoList.toTodoList().right()
        }){
            dataSourceDatabaseTodos.list().toTodoList().right()
        }
}