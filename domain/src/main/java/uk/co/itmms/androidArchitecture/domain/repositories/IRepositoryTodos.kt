package uk.co.itmms.androidArchitecture.domain.repositories

import uk.co.itmms.androidArchitecture.domain.entities.Todo
import uk.co.itmms.androidArchitecture.domain.failures.FailureRepositoryBackend

interface IRepositoryTodos {

    data class Result(
        val offline: Boolean,
        val todoList: List<Todo>,
        val failure: FailureRepositoryBackend? = null,
    )
    suspend fun list(): Result
}