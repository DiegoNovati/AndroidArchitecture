package com.elt.passsystem.data

import android.content.Context
import com.elt.passsystem.data.datasources.*
import com.elt.passsystem.data.datasources.console.ConsoleApi
import com.elt.passsystem.data.datasources.console.IConsoleApi
import com.elt.passsystem.data.datasources.db.PassDatabase
import com.elt.passsystem.data.datasources.db.openDatabase
import com.elt.passsystem.data.repositories.RepositoryAnalytics
import com.elt.passsystem.data.repositories.RepositoryLogger
import com.elt.passsystem.domain.repositories.IRepositoryAnalytics
import com.elt.passsystem.domain.repositories.IRepositoryLogger

/**
 * This object exports the repositories needed by the use cases and avoid the use of any
 * Dependency Injection engine
 */
object DataInterface {

    private lateinit var passDatabase: PassDatabase

    fun initDataLayer(applicationContext: Context, releaseMode: Boolean) {
        passDatabase = openDatabase(applicationContext)
    }

    val repositoryAnalytics: IRepositoryAnalytics by lazy {
        RepositoryAnalytics(dataSourceAnalytics)
    }

    val repositoryLogger: IRepositoryLogger by lazy {
        RepositoryLogger(dataSourceLogger)
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

    private val dataSourceDatabaseCustomers: IDataSourceDatabaseCustomers by lazy {
        DataSourceDatabaseCustomers(passDatabase)
    }

    private val dataSourceDatabaseBookings: IDataSourceDatabaseBookings by lazy {
        DataSourceDatabaseBookings(passDatabase)
    }
}