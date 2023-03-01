package uk.co.itmms.androidArchitecture.data.extensions

import uk.co.itmms.androidArchitecture.data.models.DBProduct
import uk.co.itmms.androidArchitecture.data.models.NetProductProduct
import uk.co.itmms.androidArchitecture.data.models.NetProductsResponse
import uk.co.itmms.androidArchitecture.domain.entities.Product

fun NetProductProduct.toDBProduct(): DBProduct =
    DBProduct(
        id = id,
        title = title,
        description = description,
        price = price,
        discountPercentage = discountPercentage,
        rating = rating,
        stock = stock,
        brand = brand,
        category = category,
        thumbnail = thumbnail,
        imageList = images.joinToString(","),
    )

fun List<NetProductProduct>.toDBProductList(): List<DBProduct> =
    this.map { it.toDBProduct() }

fun NetProductsResponse.toDBProductList(): List<DBProduct> =
    this.products.toDBProductList()

fun DBProduct.toProduct(): Product =
    Product(
        id = id,
        title = title,
        description = description,
        price = price,
        discountPercentage = discountPercentage,
        rating = rating,
        stock = stock,
        brand = brand,
        category = category,
        thumbnail = thumbnail,
        images = imageList.split(",").filter { it.isNotBlank() }
    )

fun List<DBProduct>.toProductList(): List<Product> =
    this.map { it.toProduct() }