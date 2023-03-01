package uk.co.itmms.androidArchitecture.data.datasources

import uk.co.itmms.androidArchitecture.data.BaseDataTest
import uk.co.itmms.androidArchitecture.data.datasources.db.DaoProducts
import uk.co.itmms.androidArchitecture.data.datasources.db.IAppDatabase
import uk.co.itmms.androidArchitecture.data.models.DBProduct
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class DataSourceDatabaseProductsTest: BaseDataTest() {

    @MockK
    private lateinit var mockAppDatabase: IAppDatabase

    @MockK
    private lateinit var mockDaoProducts: DaoProducts

    private lateinit var dataSourceDatabaseProduct: DataSourceDatabaseProducts

    @Before
    fun setUp() {
        dataSourceDatabaseProduct = DataSourceDatabaseProducts(
            mockAppDatabase
        )

        coEvery { mockAppDatabase.daoProducts() } returns mockDaoProducts
    }

    @Test
    fun `testing list`() = runBlocking {
        val mockList = listOf<DBProduct>(mockk(), mockk())
        coEvery { mockDaoProducts.list() } returns mockList

        val actual = dataSourceDatabaseProduct.list()

        assertEquals(mockList, actual)
        coVerify(exactly = 1) {
            mockDaoProducts.list()
        }
        confirmVerified(mockDaoProducts)
    }

    @Test
    fun `testing get`() = runBlocking {
        val id = 1234L
        val mockProduct = mockk<DBProduct>()

        coEvery { mockDaoProducts.getById(any()) } returns mockProduct

        val actual = dataSourceDatabaseProduct.get(id)

        assertEquals(mockProduct, actual)

        coVerify(exactly = 1) {
            mockDaoProducts.getById(id)
        }
        confirmVerified(mockDaoProducts)
    }

    @Test
    fun `testing insert`() = runBlocking {
        val mockProduct = mockk<DBProduct>()

        dataSourceDatabaseProduct.insert(mockProduct)

        coVerify(exactly = 1) {
            mockDaoProducts.insert(mockProduct)
        }
        confirmVerified(mockDaoProducts)
    }

    @Test
    fun `testing insert list`() = runBlocking {
        val mockList = listOf<DBProduct>(mockk(), mockk())

        dataSourceDatabaseProduct.insert(mockList)

        coVerify(exactly = 1) {
            mockDaoProducts.insert(mockList)
        }
        confirmVerified(mockDaoProducts)
    }

    @Test
    fun `testing update`() = runBlocking {
        val mockProduct = mockk<DBProduct>()

        dataSourceDatabaseProduct.update(mockProduct)

        coVerify(exactly = 1) {
            mockDaoProducts.update(mockProduct)
        }
        confirmVerified(mockDaoProducts)
    }

    @Test
    fun `testing update list`() = runBlocking {
        val mockProduct1 = mockk<DBProduct>()
        val mockProduct2 = mockk<DBProduct>()
        val mockList = listOf(mockProduct1, mockProduct2)

        dataSourceDatabaseProduct.update(mockList)

        coVerify(exactly = 1) {
            mockDaoProducts.update(mockProduct1)
            mockDaoProducts.update(mockProduct2)
        }
        confirmVerified(mockDaoProducts)
    }

    @Test
    fun `testing delete`() = runBlocking {
        val mockProduct = mockk<DBProduct>()

        dataSourceDatabaseProduct.delete(mockProduct)

        coVerify(exactly = 1) {
            mockDaoProducts.delete(mockProduct)
        }
        confirmVerified(mockDaoProducts)
    }

    @Test
    fun `testing deleteAll`() = runBlocking {
        val mockProduct1 = mockk<DBProduct>()
        val mockProduct2 = mockk<DBProduct>()
        val mockList = listOf(mockProduct1, mockProduct2)

        coEvery { mockDaoProducts.list() } returns mockList

        dataSourceDatabaseProduct.deleteAll()

        coVerify(exactly = 1) {
            mockDaoProducts.list()
            mockDaoProducts.delete(mockProduct1)
            mockDaoProducts.delete(mockProduct2)
        }
    }
}