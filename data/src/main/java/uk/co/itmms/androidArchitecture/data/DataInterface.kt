package uk.co.itmms.androidArchitecture.data

import android.content.Context
import android.net.ConnectivityManager
import uk.co.itmms.androidArchitecture.data.datasources.*
import uk.co.itmms.androidArchitecture.data.datasources.db.PassDatabase
import uk.co.itmms.androidArchitecture.data.datasources.db.openDatabase
import uk.co.itmms.androidArchitecture.data.datasources.logging.ILoggingConsole
import uk.co.itmms.androidArchitecture.data.datasources.logging.LoggingConsole
import uk.co.itmms.androidArchitecture.data.datasources.network.IBackend
import uk.co.itmms.androidArchitecture.data.datasources.network.createBackend
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

    private lateinit var passApi: IBackend

    /**
     * Function initializing all the engines used by the data module
     *
     * This method must be called by the Application in the onCreate method
     */
    fun initDataLayer(applicationContext: Context, releaseMode: Boolean) {
        this.applicationContext = applicationContext

        initFlipper(applicationContext, networkFlipperPlugin)

        passDatabase = openDatabase(applicationContext)
        passApi = createBackend(releaseMode, networkFlipperPlugin)
    }

    /**
     * Use cases exported by the module
     *
     * Note: use cases are created each time they are used, to avoid increasing usage of memory.
     */

    fun getUseCaseLoginMonitor(): UseCaseLoginMonitor =
        UseCaseLoginMonitor(
            repositoryDevelopmentLogger, repositoryDevelopmentAnalytics, repositoryNetworkMonitor,
            repositoryRuntime,
        )

    fun getUseCaseLoginLogin(): UseCaseLoginLogin =
        UseCaseLoginLogin(
            repositoryDevelopmentLogger,
            repositoryDevelopmentAnalytics,
            repositoryAuthentication,
            repositoryCustomers,
            repositoryBookings,
            repositoryRuntime,
        )

    fun getUseCaseHomeInit(): UseCaseHomeInit =
        UseCaseHomeInit(
            repositoryDevelopmentLogger, repositoryDevelopmentAnalytics, repositoryRuntime
        )

    fun getUseCaseHomeMonitor(): UseCaseHomeMonitor =
        UseCaseHomeMonitor(
            repositoryDevelopmentLogger,
            repositoryDevelopmentAnalytics,
            repositoryNetworkMonitor
        )


    fun getUseCaseHomeLogout(): UseCaseHomeLogout =
        UseCaseHomeLogout(
            repositoryDevelopmentLogger,
            repositoryDevelopmentAnalytics,
            repositoryAuthentication,
            repositoryRuntime,
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