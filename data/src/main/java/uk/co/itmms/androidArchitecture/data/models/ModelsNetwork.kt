package uk.co.itmms.androidArchitecture.data.models

data class NetAuthLoginRequest(
    val username: String,
    val password: String,
)

data class NetAuthLoginResponse(
    val id: Long,
    val username: String,
    val email: String,
    val firstName: String,
    val lastName: String,
    val gender: String,
    val image: String,
    val token: String,
)

data class NetProductsResponse(
    val products: List<NetProductProduct>,
    val total: Int,
    val skip: Int,
    val limit: Int,
)

data class NetProductProduct(
    val id: Long,
    val title: String,
    val description: String,
    val price: Long,
    val discountPercentage: Double,
    val rating: Double,
    val stock: Long,
    val brand: String,
    val category: String,
    val thumbnail: String,
    val images: List<String>
)

data class NetTodosResponse(
    val todos: List<NetTodoTodo>,
    val total: Int,
    val skip: Int,
    val limit: Int,
)
data class NetTodoTodo(
    val id: Long,
    val todo: String,
    val completed: Boolean,
)