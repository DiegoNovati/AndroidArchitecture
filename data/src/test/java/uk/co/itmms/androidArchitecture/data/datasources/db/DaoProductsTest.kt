package uk.co.itmms.androidArchitecture.data.datasources.db

import uk.co.itmms.androidArchitecture.data.BaseDataDaoTest
import junit.framework.TestCase.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.junit.Test
import uk.co.itmms.androidArchitecture.data.models.DBProduct

class DaoProductsTest : BaseDataDaoTest() {

    @Test
    fun `testing insert and count`() = runBlocking(Dispatchers.IO) {
        val product = createProduct(1)

        daoProducts.insert(product)
        val actual = daoProducts.count()

        assertEquals(1, actual)
    }

    @Test
    fun `testing insert list and count`() = runBlocking(Dispatchers.IO) {
        val productList = listOf(
            createProduct(1),
            createProduct(2),
        )

        daoProducts.insert(productList)
        val actual = daoProducts.count()

        assertEquals(productList.size, actual)
    }

    @Test
    fun `testing list`() = runBlocking(Dispatchers.IO) {
        val product1 = createProduct(1)
        val product2 = createProduct(2)
        val productList = listOf(
            product1,
            product2,
        )

        daoProducts.insert(productList)
        val actual = daoProducts.list()

        assertEquals(2, actual.size)
        assertEquals(product1.id, actual[0].id)
        assertEquals(product2.id, actual[1].id)
    }

    @Test
    fun `testing getById with existing and not existing id`() = runBlocking(Dispatchers.IO) {
        val product = createProduct(1)

        daoProducts.insert(product)
        var actual = daoProducts.getById(product.id)

        assertNotNull(actual)

        actual = daoProducts.getById(999999)

        assertNull(actual)
    }

    @Test
    fun `testing update with existing product`() = runBlocking(Dispatchers.IO) {
        val product = createProduct(1)
        val newStock = 101L

        daoProducts.insert(product)
        val dbProducts = daoProducts.getById(product.id)!!.copy(stock = newStock)
        daoProducts.update(dbProducts)

        assertEquals(1, daoProducts.count())
        assertEquals(newStock, daoProducts.getById(product.id)!!.stock)
    }

    @Test
    fun `testing update with NOT existing product`() = runBlocking(Dispatchers.IO) {
        val product = createProduct(1)

        daoProducts.update(product)

        assertEquals(0, daoProducts.count())
        assertNull(daoProducts.getById(product.id))
    }

    @Test
    fun `test delete single`() = runBlocking(Dispatchers.IO) {
        val product = createProduct(3)
        val productList = listOf(
            createProduct(1),
            createProduct(2),
            product,
            createProduct(4),
        )

        daoProducts.insert(productList)
        daoProducts.delete(product)

        assertEquals(productList.size - 1, daoProducts.count())
        assertNull(daoProducts.getById(product.id))
    }

    @Test
    fun `test delete multiple`() = runBlocking(Dispatchers.IO) {
        val product1 = createProduct(1)
        val product3 = createProduct(3)
        val productList = listOf(
            product1,
            createProduct(2),
            product3,
            createProduct(4),
        )
        val productToDeleteList = listOf(
            product1,
            product3
        )

        daoProducts.insert(productList)
        daoProducts.delete(productToDeleteList)

        assertEquals(productList.size - productToDeleteList.size, daoProducts.count())
        assertNull(daoProducts.getById(product1.id))
        assertNull(daoProducts.getById(product3.id))
    }

    private fun createProduct(id: Long): DBProduct =
        DBProduct(
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
            imageList = "https://i.dummyjson.com/data/products/1/1.jpg,https://i.dummyjson.com/data/products/1/2.jpg",
        )
}