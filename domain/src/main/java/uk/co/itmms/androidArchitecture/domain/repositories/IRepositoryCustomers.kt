package uk.co.itmms.androidArchitecture.domain.repositories

import arrow.core.Either
import uk.co.itmms.androidArchitecture.domain.entities.Customer

sealed class RepositoryBackendFailure {
    object ConnectionProblems : RepositoryBackendFailure()
    object BackendProblems : RepositoryBackendFailure()
}

interface IRepositoryCustomers {

    suspend fun getCustomerList(officeBid: String): Either<RepositoryBackendFailure, List<Customer>>
}