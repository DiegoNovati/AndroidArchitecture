package com.elt.passsystem.data.repositories

import arrow.core.Either
import com.elt.passsystem.data.datasources.IDataSourceBackend
import com.elt.passsystem.data.datasources.IDataSourceDatabaseCustomers
import com.elt.passsystem.data.extensions.toCustomerList
import com.elt.passsystem.data.extensions.toDBCustomerList
import com.elt.passsystem.domain.entities.Customer
import com.elt.passsystem.domain.repositories.IRepositoryCustomers
import com.elt.passsystem.domain.repositories.RepositoryBackendFailure

class RepositoryCustomers(
    private val dataSourceBackend: IDataSourceBackend,
    private val dataSourceDatabaseCustomers: IDataSourceDatabaseCustomers,
) : IRepositoryCustomers {

    override suspend fun getCustomerList(
        officeBid: String
    ): Either<RepositoryBackendFailure, List<Customer>> =
        invokeRepository({
            val netCustomersResponse = dataSourceBackend.getCustomerList(officeBid)
            val dbCustomerList = netCustomersResponse.customers.toDBCustomerList()
            dataSourceDatabaseCustomers.deleteAll()
            dataSourceDatabaseCustomers.insert(dbCustomerList)
            Either.Right(dbCustomerList.toCustomerList())
        }){
            Either.Right(dataSourceDatabaseCustomers.list().toCustomerList())
        }
}