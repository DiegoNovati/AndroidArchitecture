package uk.co.itmms.androidArchitecture.domain.repositories

import uk.co.itmms.androidArchitecture.domain.entities.Product
import uk.co.itmms.androidArchitecture.domain.failures.FailureRepositoryBackend

interface IRepositoryProducts {

    data class Result(
        val offline: Boolean,
        val productList: List<Product>,
        val failure: FailureRepositoryBackend? = null,
    )
    suspend fun list(): Result
}