package uk.co.itmms.androidArchitecture.domain.repositories

import arrow.core.Either
import uk.co.itmms.androidArchitecture.domain.entities.Todo
import uk.co.itmms.androidArchitecture.domain.failures.FailureRepositoryBackend

interface IRepositoryTodos {
    suspend fun list(): Either<FailureRepositoryBackend, List<Todo>>
}