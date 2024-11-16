package uk.co.itmms.androidArchitecture.data.repositories

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.impl.annotations.MockK
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertNull
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.runBlocking
import org.junit.Test
import uk.co.itmms.androidArchitecture.data.BaseDataTest
import uk.co.itmms.androidArchitecture.data.datasources.IDataSourceBackend
import uk.co.itmms.androidArchitecture.data.datasources.IDataSourceDatabaseProducts
import uk.co.itmms.androidArchitecture.data.datasources.network.BackendErrorCode
import uk.co.itmms.androidArchitecture.data.datasources.network.BackendException
import uk.co.itmms.androidArchitecture.data.extensions.toDBProduct
import uk.co.itmms.androidArchitecture.data.extensions.toDBProductList
import uk.co.itmms.androidArchitecture.data.extensions.toProductList
import uk.co.itmms.androidArchitecture.data.models.DBProduct
import uk.co.itmms.androidArchitecture.data.models.NetProductProduct
import uk.co.itmms.androidArchitecture.data.models.NetProductsResponse

class RepositoryProductsTest : BaseDataTest() {

    @MockK
    private lateinit var mockDataSourceBackend: IDataSourceBackend

    @MockK
    private lateinit var mockDataSourceDatabaseProducts: IDataSourceDatabaseProducts

    private lateinit var repositoryProducts: RepositoryProducts

    @Test
    fun `WHEN backend returns products THEN save them to database`() = runBlocking {
        val netProductResponse = createNetProductResponse()

        coEvery { mockDataSourceBackend.getProducts() } returns netProductResponse

        val actual = repositoryProducts.list()

        assertFalse(actual.offline)
        assertEquals(netProductResponse.products.toDBProductList().toProductList(), actual.productList)
        assertNull(actual.failure)

        coVerify(exactly = 1) {
            mockDataSourceBackend.getProducts()
            mockDataSourceDatabaseProducts.deleteAll()
            mockDataSourceDatabaseProducts.insert(any<List<DBProduct>>())
        }
        confirmVerified(mockDataSourceBackend, mockDataSourceDatabaseProducts)
    }

    @Test
    fun `WHEN backend fails THEN returns data from the database`() = runBlocking {
        val dbProductList = createDBProductList()

        coEvery { mockDataSourceBackend.getProducts() } throws BackendException(BackendErrorCode.IO)
        coEvery { mockDataSourceDatabaseProducts.list() } returns dbProductList

        val actual = repositoryProducts.list()

        assertTrue(actual.offline)
        assertEquals(dbProductList.toProductList(), actual.productList)
        assertNull(actual.failure)

        coVerify(exactly = 1) {
            mockDataSourceBackend.getProducts()
            mockDataSourceDatabaseProducts.list()
        }
        confirmVerified(mockDataSourceBackend, mockDataSourceDatabaseProducts)
    }

    private fun createDBProductList(): List<DBProduct> =
        listOf(
            createNetProductProduct(1).toDBProduct(),
            createNetProductProduct(2).toDBProduct(),
            createNetProductProduct(3).toDBProduct(),
        )

    private fun createNetProductResponse(): NetProductsResponse =
        NetProductsResponse(
            products = listOf(
                createNetProductProduct(1),
                createNetProductProduct(2),
                createNetProductProduct(3),
            ),
            total = 100,
            skip = 0,
            limit = 30,
        )

    private fun createNetProductProduct(id: Long): NetProductProduct =
        NetProductProduct(
            id = id,
            title = "title $id",
            description = "description $id",
            price = 1234,
            discountPercentage = 12.96,
            rating = 4.69,
            stock = 94,
            brand = "Apple",
            category = "smartphones",
            thumbnail = "https://i.dummyjson.com/data/products/1/thumbnail.jpg",
            images = listOf(
                "https://i.dummyjson.com/data/products/1/1.jpg",
                "https://i.dummyjson.com/data/products/1/2.jpg",
            ),
        )
}