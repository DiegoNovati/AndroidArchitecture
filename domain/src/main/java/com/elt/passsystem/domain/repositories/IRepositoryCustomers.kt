package com.elt.passsystem.domain.repositories

import arrow.core.Either
import com.elt.passsystem.domain.entities.Customer

sealed class RepositoryBackendFailure {
    object ConnectionProblems : RepositoryBackendFailure()
    object BackendProblems : RepositoryBackendFailure()
}

interface IRepositoryCustomers {

    suspend fun getCustomerList(officeBid: String): Either<RepositoryBackendFailure, List<Customer>>
}