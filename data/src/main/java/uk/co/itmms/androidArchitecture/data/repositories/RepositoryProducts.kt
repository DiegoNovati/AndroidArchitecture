package uk.co.itmms.androidArchitecture.data.repositories

import arrow.core.right
import uk.co.itmms.androidArchitecture.data.datasources.IDataSourceBackend
import uk.co.itmms.androidArchitecture.data.datasources.IDataSourceDatabaseProducts
import uk.co.itmms.androidArchitecture.data.extensions.toDBProductList
import uk.co.itmms.androidArchitecture.data.extensions.toProductList
import uk.co.itmms.androidArchitecture.domain.repositories.IRepositoryProducts

class RepositoryProducts(
    private val dataSourceBackend: IDataSourceBackend,
    private val dataSourceDatabaseProducts: IDataSourceDatabaseProducts,
) : IRepositoryProducts {

    override suspend fun list(): IRepositoryProducts.Result =
        invokeRepository {
            dataSourceBackend.getProducts().right()
        }.fold({ failure ->
            IRepositoryProducts.Result(
                offline = true,
                productList = dataSourceDatabaseProducts.list().toProductList(),
                failure = failure,
            )
        }){ response ->
            val dbProductList = response.products.toDBProductList()
            dataSourceDatabaseProducts.deleteAll()
            dataSourceDatabaseProducts.insert(dbProductList)
            IRepositoryProducts.Result(
                offline = false,
                productList = dbProductList.toProductList(),
                failure = null,
            )
        }
}