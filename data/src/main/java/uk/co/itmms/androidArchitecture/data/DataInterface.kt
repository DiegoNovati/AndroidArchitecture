package uk.co.itmms.androidArchitecture.data

import android.content.Context
import android.net.ConnectivityManager
import com.facebook.flipper.plugins.network.NetworkFlipperPlugin
import uk.co.itmms.androidArchitecture.data.datasources.DataSourceBackend
import uk.co.itmms.androidArchitecture.data.datasources.DataSourceConnectivityMonitor
import uk.co.itmms.androidArchitecture.data.datasources.DataSourceDatabaseBookings
import uk.co.itmms.androidArchitecture.data.datasources.DataSourceDatabaseCustomers
import uk.co.itmms.androidArchitecture.data.datasources.DataSourceDevelopmentAnalyticsConsole
import uk.co.itmms.androidArchitecture.data.datasources.DataSourceDevelopmentLoggerConsole
import uk.co.itmms.androidArchitecture.data.datasources.IDataSourceBackend
import uk.co.itmms.androidArchitecture.data.datasources.IDataSourceConnectivityMonitor
import uk.co.itmms.androidArchitecture.data.datasources.IDataSourceDatabaseBookings
import uk.co.itmms.androidArchitecture.data.datasources.IDataSourceDatabaseCustomers
import uk.co.itmms.androidArchitecture.data.datasources.IDataSourceDevelopmentAnalytics
import uk.co.itmms.androidArchitecture.data.datasources.IDataSourceDevelopmentLogger
import uk.co.itmms.androidArchitecture.data.datasources.db.AppDatabase
import uk.co.itmms.androidArchitecture.data.datasources.db.openDatabase
import uk.co.itmms.androidArchitecture.data.datasources.logging.ILoggingConsole
import uk.co.itmms.androidArchitecture.data.datasources.logging.LoggingConsole
import uk.co.itmms.androidArchitecture.data.datasources.network.INetworkApi
import uk.co.itmms.androidArchitecture.data.datasources.network.createNetworkApi
import uk.co.itmms.androidArchitecture.data.datasources.networkMonitor.ConnectivityChecker
import uk.co.itmms.androidArchitecture.data.datasources.networkMonitor.ConnectivityMonitorCallback
import uk.co.itmms.androidArchitecture.data.datasources.networkMonitor.IConnectivityChecker
import uk.co.itmms.androidArchitecture.data.datasources.networkMonitor.IConnectivityMonitorCallback
import uk.co.itmms.androidArchitecture.data.external.initFlipper
import uk.co.itmms.androidArchitecture.data.repositories.RepositoryAuthentication
import uk.co.itmms.androidArchitecture.data.repositories.RepositoryBookings
import uk.co.itmms.androidArchitecture.data.repositories.RepositoryCustomers
import uk.co.itmms.androidArchitecture.data.repositories.RepositoryDevelopmentAnalytics
import uk.co.itmms.androidArchitecture.data.repositories.RepositoryDevelopmentLogger
import uk.co.itmms.androidArchitecture.data.repositories.RepositoryNetworkMonitor
import uk.co.itmms.androidArchitecture.data.repositories.RepositoryRuntime
import uk.co.itmms.androidArchitecture.data.repositories.RepositorySession
import uk.co.itmms.androidArchitecture.domain.repositories.IRepositoryAuthentication
import uk.co.itmms.androidArchitecture.domain.repositories.IRepositoryBookings
import uk.co.itmms.androidArchitecture.domain.repositories.IRepositoryCustomers
import uk.co.itmms.androidArchitecture.domain.repositories.IRepositoryDevelopmentAnalytics
import uk.co.itmms.androidArchitecture.domain.repositories.IRepositoryDevelopmentLogger
import uk.co.itmms.androidArchitecture.domain.repositories.IRepositoryNetworkMonitor
import uk.co.itmms.androidArchitecture.domain.repositories.IRepositoryRuntime
import uk.co.itmms.androidArchitecture.domain.repositories.IRepositorySession
import uk.co.itmms.androidArchitecture.domain.usecases.home.UseCaseHomeInit
import uk.co.itmms.androidArchitecture.domain.usecases.home.UseCaseHomeLogout
import uk.co.itmms.androidArchitecture.domain.usecases.home.UseCaseHomeMonitor
import uk.co.itmms.androidArchitecture.domain.usecases.login.UseCaseLoginLogin
import uk.co.itmms.androidArchitecture.domain.usecases.login.UseCaseLoginMonitor

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

    private lateinit var passDatabase: AppDatabase

    private lateinit var passApi: INetworkApi

    /**
     * Function initializing all the engines used by the data module
     *
     * This method must be called by the Application in the onCreate method
     */
    fun initDataLayer(applicationContext: Context, releaseMode: Boolean) {
        this.applicationContext = applicationContext

        initFlipper(applicationContext, networkFlipperPlugin)

        passDatabase = openDatabase(applicationContext)
        passApi = createNetworkApi(releaseMode, networkFlipperPlugin)
    }

    /**
     * Use cases exported by the module
     *
     * Note: use cases are created each time they are used, to avoid increasing usage of memory.
     */

    fun getUseCaseLoginMonitor(): UseCaseLoginMonitor =
        UseCaseLoginMonitor(
            repositoryDevelopmentLogger = repositoryDevelopmentLogger,
            repositoryDevelopmentAnalytics = repositoryDevelopmentAnalytics,
            repositoryNetworkMonitor = repositoryNetworkMonitor,
            repositoryRuntime = repositoryRuntime,
            repositorySession = repositorySession,
        )

    fun getUseCaseLoginLogin(): UseCaseLoginLogin =
        UseCaseLoginLogin(
            repositoryDevelopmentLogger = repositoryDevelopmentLogger,
            repositoryDevelopmentAnalytics = repositoryDevelopmentAnalytics,
            repositoryAuthentication = repositoryAuthentication,
            repositoryCustomers = repositoryCustomers,
            repositoryBookings = repositoryBookings,
            repositoryRuntime = repositoryRuntime,
            repositorySession = repositorySession,
        )

    fun getUseCaseHomeInit(): UseCaseHomeInit =
        UseCaseHomeInit(
            repositoryDevelopmentLogger = repositoryDevelopmentLogger,
            repositoryDevelopmentAnalytics = repositoryDevelopmentAnalytics,
            repositorySession = repositorySession,
        )

    fun getUseCaseHomeMonitor(): UseCaseHomeMonitor =
        UseCaseHomeMonitor(
            repositoryDevelopmentLogger = repositoryDevelopmentLogger,
            repositoryDevelopmentAnalytics = repositoryDevelopmentAnalytics,
            repositoryNetworkMonitor = repositoryNetworkMonitor,
        )


    fun getUseCaseHomeLogout(): UseCaseHomeLogout =
        UseCaseHomeLogout(
            repositoryDevelopmentLogger = repositoryDevelopmentLogger,
            repositoryDevelopmentAnalytics = repositoryDevelopmentAnalytics,
            repositoryAuthentication = repositoryAuthentication,
            repositorySession = repositorySession,
        )

    /**
     * Repositories and data sources used by the use cases
     *
     * Note: repositories and data sources are created once the first time they are used and never
     *       recreated
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

    private val repositorySession: IRepositorySession by lazy {
        RepositorySession()
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