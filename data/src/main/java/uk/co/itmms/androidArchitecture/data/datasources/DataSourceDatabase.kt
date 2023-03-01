package uk.co.itmms.androidArchitecture.data.datasources

import uk.co.itmms.androidArchitecture.data.datasources.db.IAppDatabase
import uk.co.itmms.androidArchitecture.data.models.DBProduct
import uk.co.itmms.androidArchitecture.data.models.DBTodo

interface IDataSourceDatabaseProducts {
    suspend fun list(): List<DBProduct>
    suspend fun get(id: Long): DBProduct?
    suspend fun insert(product: DBProduct)
    suspend fun insert(list: List<DBProduct>)
    suspend fun update(product: DBProduct)
    suspend fun update(list: List<DBProduct>)
    suspend fun delete(product: DBProduct)
    suspend fun deleteAll()
}

interface IDataSourceDatabaseTodos {
    suspend fun list(): List<DBTodo>
    suspend fun get(id: Long): DBTodo?
    suspend fun insert(todo: DBTodo)
    suspend fun insert(list: List<DBTodo>)
    suspend fun update(todo: DBTodo)
    suspend fun update(list: List<DBTodo>)
    suspend fun delete(todo: DBTodo)
    suspend fun deleteAll()
}

class DataSourceDatabaseProducts(
    private val appDatabase: IAppDatabase,
) : IDataSourceDatabaseProducts {
    override suspend fun list(): List<DBProduct> =
        appDatabase.daoProducts().list()

    override suspend fun get(id: Long): DBProduct? =
        appDatabase.daoProducts().getById(id)

    override suspend fun insert(product: DBProduct) =
        appDatabase.daoProducts().insert(product)

    override suspend fun insert(list: List<DBProduct>) =
        appDatabase.daoProducts().insert(list)

    override suspend fun update(product: DBProduct) =
        appDatabase.daoProducts().update(product)

    override suspend fun update(list: List<DBProduct>) =
        list.forEach {
            appDatabase.daoProducts().update(it)
        }

    override suspend fun delete(product: DBProduct) =
        appDatabase.daoProducts().delete(product)

    override suspend fun deleteAll() =
        list().forEach {
            appDatabase.daoProducts().delete(it)
        }
}

class DataSourceDatabaseTodos(
    private val appDatabase: IAppDatabase,
) : IDataSourceDatabaseTodos {
    override suspend fun list(): List<DBTodo> =
        appDatabase.daoTodos().list()

    override suspend fun get(id: Long): DBTodo? =
        appDatabase.daoTodos().getById(id)

    override suspend fun insert(todo: DBTodo) =
        appDatabase.daoTodos().insert(todo)

    override suspend fun insert(list: List<DBTodo>) =
        appDatabase.daoTodos().insert(list)

    override suspend fun update(todo: DBTodo) =
        appDatabase.daoTodos().update(todo)

    override suspend fun update(list: List<DBTodo>) =
        list.forEach {
            appDatabase.daoTodos().update(it)
        }

    override suspend fun delete(todo: DBTodo) =
        appDatabase.daoTodos().delete(todo)

    override suspend fun deleteAll() =
        list().forEach {
            appDatabase.daoTodos().delete(it)
        }
}