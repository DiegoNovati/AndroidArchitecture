package com.elt.passsystem.data

import android.content.Context
import com.elt.passsystem.data.datasources.*
import com.elt.passsystem.data.datasources.console.ConsoleApi
import com.elt.passsystem.data.datasources.console.IConsoleApi
import com.elt.passsystem.data.datasources.db.PassDatabase
import com.elt.passsystem.data.datasources.db.openDatabase
import com.elt.passsystem.data.datasources.network.IPassApi
import com.elt.passsystem.data.datasources.network.createPassApi
import com.elt.passsystem.data.repositories.*
import com.elt.passsystem.domain.repositories.*

/**
 * This object exports the repositories needed by the use cases and avoid the use of any
 * Dependency Injection engine
 */
object DataInterface {

    private lateinit var passDatabase: PassDatabase

    private lateinit var passApi: IPassApi

    /**
     * Function initializing all the engines used by the data module
     *
     * This method must be called by the Application in the onCreate method
     */
    fun initDataLayer(applicationContext: Context, releaseMode: Boolean) {
        passDatabase = openDatabase(applicationContext)
        passApi = createPassApi(releaseMode)
    }

    val repositoryAnalytics: IRepositoryAnalytics by lazy {
        RepositoryAnalytics(dataSourceAnalytics)
    }

    val repositoryLogger: IRepositoryLogger by lazy {
        RepositoryLogger(dataSourceLogger)
    }

    val repositoryAuthentication: IRepositoryAuthentication by lazy {
        RepositoryAuthentication(dataSourceBackend)
    }

    val repositoryCustomers: IRepositoryCustomers by lazy {
        RepositoryCustomers(dataSourceBackend, dataSourceDatabaseCustomers)
    }

    val repositoryBookings: IRepositoryBookings by lazy {
        RepositoryBookings(dataSourceBackend, dataSourceDatabaseBookings)
    }

    private val dataSourceAnalytics: IDataSourceAnalytics by lazy {
        DataSourceAnalyticsConsole(consoleApi)
    }

    private val dataSourceLogger: IDataSourceLogger by lazy {
        DataSourceLoggerConsole(consoleApi)
    }

    private val consoleApi: IConsoleApi by lazy {
        ConsoleApi()
    }

    private val dataSourceBackend: IDataSourceBackend by lazy {
        DataSourceBackend(passApi)
    }

    private val dataSourceDatabaseCustomers: IDataSourceDatabaseCustomers by lazy {
        DataSourceDatabaseCustomers(passDatabase)
    }

    private val dataSourceDatabaseBookings: IDataSourceDatabaseBookings by lazy {
        DataSourceDatabaseBookings(passDatabase)
    }
}