package com.elt.passsystem.data

import android.content.Context
import android.net.ConnectivityManager
import com.elt.passsystem.data.datasources.*
import com.elt.passsystem.data.datasources.console.ConsoleApi
import com.elt.passsystem.data.datasources.console.IConsoleApi
import com.elt.passsystem.data.datasources.db.PassDatabase
import com.elt.passsystem.data.datasources.db.openDatabase
import com.elt.passsystem.data.datasources.network.IPassApi
import com.elt.passsystem.data.datasources.network.createPassApi
import com.elt.passsystem.data.datasources.networkMonitor.ConnectivityChecker
import com.elt.passsystem.data.datasources.networkMonitor.ConnectivityMonitorCallback
import com.elt.passsystem.data.datasources.networkMonitor.IConnectivityChecker
import com.elt.passsystem.data.datasources.networkMonitor.IConnectivityMonitorCallback
import com.elt.passsystem.data.external.initFlipper
import com.elt.passsystem.data.repositories.*
import com.elt.passsystem.domain.repositories.*
import com.elt.passsystem.domain.usecases.authentication.UseCaseAuthenticationLogin
import com.elt.passsystem.domain.usecases.authentication.UseCaseAuthenticationLogout
import com.elt.passsystem.domain.usecases.networkMonitor.UseCaseNetworkMonitor
import com.facebook.flipper.plugins.network.NetworkFlipperPlugin

/**
 * This object exports the function to initialize the engines used by this module and all the use
 * cases
 *
 * In this module we don't use any Dependency Injection engine because it is not needed
 */
object DataInterface {

    /**
     * The Application context can be kept because it cannot change.
     */
    private lateinit var applicationContext: Context

    private lateinit var passDatabase: PassDatabase

    private lateinit var passApi: IPassApi

    /**
     * Function initializing all the engines used by the data module
     *
     * This method must be called by the Application in the onCreate method
     */
    fun initDataLayer(applicationContext: Context, releaseMode: Boolean) {
        this.applicationContext = applicationContext

        initFlipper(applicationContext, networkFlipperPlugin)

        passDatabase = openDatabase(applicationContext)
        passApi = createPassApi(releaseMode, networkFlipperPlugin)
    }

    /**
     * Use cases exported by the module
     */

    val useCaseNetworkMonitor: UseCaseNetworkMonitor by lazy {
        UseCaseNetworkMonitor(repositoryLogger, repositoryAnalytics, repositoryNetworkMonitor)
    }

    val useCaseAuthenticationLogin: UseCaseAuthenticationLogin by lazy {
        UseCaseAuthenticationLogin(
            repositoryLogger, repositoryAnalytics, repositoryAuthentication, repositoryCustomers,
            repositoryBookings
        )
    }

    val useCaseAuthenticationLogout: UseCaseAuthenticationLogout by lazy {
        UseCaseAuthenticationLogout(
            repositoryLogger, repositoryAnalytics, repositoryAuthentication
        )
    }

    /**
     * Repositories and data sources used by the use cases
     */

    private val repositoryAnalytics: IRepositoryAnalytics by lazy {
        RepositoryAnalytics(dataSourceAnalytics)
    }

    private val repositoryLogger: IRepositoryLogger by lazy {
        RepositoryLogger(dataSourceLogger)
    }

    private val repositoryNetworkMonitor: IRepositoryNetworkMonitor by lazy {
        RepositoryNetworkMonitor(dataSourceConnectivityMonitor)
    }

    private val repositoryAuthentication: IRepositoryAuthentication by lazy {
        RepositoryAuthentication(dataSourceBackend)
    }

    private val repositoryCustomers: IRepositoryCustomers by lazy {
        RepositoryCustomers(dataSourceBackend, dataSourceDatabaseCustomers)
    }

    private val repositoryBookings: IRepositoryBookings by lazy {
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

    private val dataSourceConnectivityMonitor: IDataSourceConnectivityMonitor by lazy {
        DataSourceConnectivityMonitor(connectivityChecker, connectivityMonitorCallback)
    }

    private val connectivityChecker: IConnectivityChecker by lazy {
        ConnectivityChecker(connectivityManager)
    }

    private val connectivityMonitorCallback: IConnectivityMonitorCallback by lazy {
        ConnectivityMonitorCallback(connectivityManager)
    }

    private val connectivityManager: ConnectivityManager by lazy {
        applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    }

    private val networkFlipperPlugin: NetworkFlipperPlugin by lazy {
        NetworkFlipperPlugin()
    }
}