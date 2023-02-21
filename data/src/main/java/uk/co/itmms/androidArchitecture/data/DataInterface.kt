package uk.co.itmms.androidArchitecture.data

import android.content.Context
import android.net.ConnectivityManager
import uk.co.itmms.androidArchitecture.data.datasources.*
import uk.co.itmms.androidArchitecture.data.datasources.db.PassDatabase
import uk.co.itmms.androidArchitecture.data.datasources.db.openDatabase
import uk.co.itmms.androidArchitecture.data.datasources.logging.ILoggingConsole
import uk.co.itmms.androidArchitecture.data.datasources.logging.LoggingConsole
import uk.co.itmms.androidArchitecture.data.datasources.network.IPassApi
import uk.co.itmms.androidArchitecture.data.datasources.network.createPassApi
import uk.co.itmms.androidArchitecture.data.datasources.networkMonitor.ConnectivityChecker
import uk.co.itmms.androidArchitecture.data.datasources.networkMonitor.ConnectivityMonitorCallback
import uk.co.itmms.androidArchitecture.data.datasources.networkMonitor.IConnectivityChecker
import uk.co.itmms.androidArchitecture.data.datasources.networkMonitor.IConnectivityMonitorCallback
import uk.co.itmms.androidArchitecture.data.external.initFlipper
import uk.co.itmms.androidArchitecture.data.repositories.*
import uk.co.itmms.androidArchitecture.domain.repositories.*
import uk.co.itmms.androidArchitecture.domain.usecases.home.UseCaseHomeMonitor
import uk.co.itmms.androidArchitecture.domain.usecases.home.UseCaseHomeInit
import uk.co.itmms.androidArchitecture.domain.usecases.home.UseCaseHomeLogout
import uk.co.itmms.androidArchitecture.domain.usecases.login.UseCaseLoginMonitor
import uk.co.itmms.androidArchitecture.domain.usecases.login.UseCaseLoginLogin
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

    val useCaseLoginMonitor: UseCaseLoginMonitor by lazy {
        UseCaseLoginMonitor(
            repositoryDevelopmentLogger, repositoryDevelopmentAnalytics, repositoryNetworkMonitor,
            repositoryRuntime,
        )
    }

    val useCaseLoginLogin: UseCaseLoginLogin by lazy {
        UseCaseLoginLogin(
            repositoryDevelopmentLogger,
            repositoryDevelopmentAnalytics,
            repositoryAuthentication,
            repositoryCustomers,
            repositoryBookings,
            repositoryRuntime,
        )
    }

    val useCaseHomeInit: UseCaseHomeInit by lazy {
        UseCaseHomeInit(
            repositoryDevelopmentLogger, repositoryDevelopmentAnalytics, repositoryRuntime
        )
    }

    val useCaseHomeMonitor: UseCaseHomeMonitor by lazy {
        UseCaseHomeMonitor(
            repositoryDevelopmentLogger,
            repositoryDevelopmentAnalytics,
            repositoryNetworkMonitor
        )
    }

    val useCaseHomeLogout: UseCaseHomeLogout by lazy {
        UseCaseHomeLogout(
            repositoryDevelopmentLogger,
            repositoryDevelopmentAnalytics,
            repositoryAuthentication,
            repositoryRuntime,
        )
    }

    /**
     * Repositories and data sources used by the use cases
     */

    private val repositoryDevelopmentAnalytics: IRepositoryDevelopmentAnalytics by lazy {
        RepositoryDevelopmentAnalytics(
            listOf(
                dataSourceDevelopmentAnalyticsConsole,
            )
        )
    }

    private val repositoryDevelopmentLogger: IRepositoryDevelopmentLogger by lazy {
        RepositoryDevelopmentLogger(
            listOf(
                dataSourceDevelopmentLoggerConsole,
            )
        )
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

    private val repositoryRuntime: IRepositoryRuntime by lazy {
        RepositoryRuntime()
    }

    private val dataSourceDevelopmentAnalyticsConsole: IDataSourceDevelopmentAnalytics by lazy {
        DataSourceDevelopmentAnalyticsConsole(loggingConsole)
    }

    private val dataSourceDevelopmentLoggerConsole: IDataSourceDevelopmentLogger by lazy {
        DataSourceDevelopmentLoggerConsole(loggingConsole)
    }

    private val loggingConsole: ILoggingConsole by lazy {
        LoggingConsole()
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