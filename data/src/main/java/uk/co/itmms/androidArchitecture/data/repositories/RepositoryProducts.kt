package uk.co.itmms.androidArchitecture.data.repositories

import arrow.core.Either
import arrow.core.right
import uk.co.itmms.androidArchitecture.data.datasources.IDataSourceBackend
import uk.co.itmms.androidArchitecture.data.datasources.IDataSourceDatabaseProducts
import uk.co.itmms.androidArchitecture.data.extensions.toDBProductList
import uk.co.itmms.androidArchitecture.data.extensions.toProductList
import uk.co.itmms.androidArchitecture.domain.entities.Product
import uk.co.itmms.androidArchitecture.domain.failures.FailureRepositoryBackend
import uk.co.itmms.androidArchitecture.domain.repositories.IRepositoryProducts

class RepositoryProducts(
    private val dataSourceBackend: IDataSourceBackend,
    private val dataSourceDatabaseProducts: IDataSourceDatabaseProducts,
) : IRepositoryProducts {

    override suspend fun list(): Either<FailureRepositoryBackend, List<Product>> =
        invokeRepository({
            val netProductsResponse = dataSourceBackend.getProducts()

            val dbProductList = netProductsResponse.products.toDBProductList()
            dataSourceDatabaseProducts.deleteAll()
            dataSourceDatabaseProducts.insert(dbProductList)
            dbProductList.toProductList().right()
        }){
            dataSourceDatabaseProducts.list().toProductList().right()
        }
}