package uk.co.itmms.androidArchitecture.domain.repositories

import arrow.core.Either
import uk.co.itmms.androidArchitecture.domain.entities.Product
import uk.co.itmms.androidArchitecture.domain.failures.FailureRepositoryBackend

interface IRepositoryProducts {
    suspend fun list(): Either<FailureRepositoryBackend, List<Product>>
}