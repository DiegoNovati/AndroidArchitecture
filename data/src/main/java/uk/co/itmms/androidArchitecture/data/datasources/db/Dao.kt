package uk.co.itmms.androidArchitecture.data.datasources.db

import androidx.room.*
import uk.co.itmms.androidArchitecture.data.models.DBBooking
import uk.co.itmms.androidArchitecture.data.models.DBCustomer
import uk.co.itmms.androidArchitecture.data.models.DBProduct
import uk.co.itmms.androidArchitecture.data.models.DBTodo
import uk.co.itmms.androidArchitecture.data.models.TableBookings
import uk.co.itmms.androidArchitecture.data.models.TableCustomers

@Dao
interface DaoCrud<in Type> {
    @Insert
    suspend fun insert(type: Type)

    @Insert
    suspend fun insert(list: List<Type>)

    @Update
    suspend fun update(type: Type)

    @Delete
    suspend fun delete(type: Type)

    @Delete
    suspend fun delete(list: List<Type>)
}

@Dao
interface DaoProducts: DaoCrud<DBProduct> {
    @Query("SELECT count(*) from Product")
    suspend fun count(): Int

    @Query("SELECT * FROM Product")
    suspend fun list(): List<DBProduct>

    @Query("SELECT * FROM Product WHERE id = :id")
    suspend fun getById(id: Long): DBProduct?
}

@Dao
interface DaoTodos: DaoCrud<DBTodo> {
    @Query("SELECT count(*) from Todo")
    suspend fun count(): Int

    @Query("SELECT * FROM Todo")
    suspend fun list(): List<DBTodo>

    @Query("SELECT * FROM Todo WHERE id = :id")
    suspend fun getById(id: Long): DBTodo?
}