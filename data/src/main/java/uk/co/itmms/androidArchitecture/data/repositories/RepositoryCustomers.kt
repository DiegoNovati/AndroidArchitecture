package uk.co.itmms.androidArchitecture.data.repositories

import arrow.core.Either
import uk.co.itmms.androidArchitecture.data.datasources.IDataSourceBackend
import uk.co.itmms.androidArchitecture.data.datasources.IDataSourceDatabaseCustomers
import uk.co.itmms.androidArchitecture.data.extensions.toCustomerList
import uk.co.itmms.androidArchitecture.data.extensions.toDBCustomerList
import uk.co.itmms.androidArchitecture.domain.entities.Customer
import uk.co.itmms.androidArchitecture.domain.repositories.IRepositoryCustomers
import uk.co.itmms.androidArchitecture.domain.repositories.RepositoryBackendFailure

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