package uk.co.itmms.androidArchitecture.data

import android.content.Context
import android.net.ConnectivityManager
import com.facebook.flipper.plugins.network.NetworkFlipperPlugin
import uk.co.itmms.androidArchitecture.data.datasources.DataSourceBackend
import uk.co.itmms.androidArchitecture.data.datasources.DataSourceConnectivityMonitor
import uk.co.itmms.androidArchitecture.data.datasources.DataSourceDatabaseProducts
import uk.co.itmms.androidArchitecture.data.datasources.DataSourceDatabaseTodos
import uk.co.itmms.androidArchitecture.data.datasources.DataSourceDevelopmentAnalyticsConsole
import uk.co.itmms.androidArchitecture.data.datasources.DataSourceDevelopmentLoggerConsole
import uk.co.itmms.androidArchitecture.data.datasources.IDataSourceBackend
import uk.co.itmms.androidArchitecture.data.datasources.IDataSourceConnectivityMonitor
import uk.co.itmms.androidArchitecture.data.datasources.IDataSourceDatabaseProducts
import uk.co.itmms.androidArchitecture.data.datasources.IDataSourceDatabaseTodos
import uk.co.itmms.androidArchitecture.data.datasources.IDataSourceDevelopmentAnalytics
import uk.co.itmms.androidArchitecture.data.datasources.IDataSourceDevelopmentLogger
import uk.co.itmms.androidArchitecture.data.datasources.db.AppDatabase
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
import uk.co.itmms.androidArchitecture.data.repositories.RepositoryAuthentication
import uk.co.itmms.androidArchitecture.data.repositories.RepositoryDevelopmentAnalytics
import uk.co.itmms.androidArchitecture.data.repositories.RepositoryDevelopmentLogger
import uk.co.itmms.androidArchitecture.data.repositories.RepositoryNetworkMonitor
import uk.co.itmms.androidArchitecture.data.repositories.RepositoryProducts
import uk.co.itmms.androidArchitecture.data.repositories.RepositoryRuntime
import uk.co.itmms.androidArchitecture.data.repositories.RepositoryTodos
import uk.co.itmms.androidArchitecture.domain.repositories.IRepositoryAuthentication
import uk.co.itmms.androidArchitecture.domain.repositories.IRepositoryDevelopmentAnalytics
import uk.co.itmms.androidArchitecture.domain.repositories.IRepositoryDevelopmentLogger
import uk.co.itmms.androidArchitecture.domain.repositories.IRepositoryNetworkMonitor
import uk.co.itmms.androidArchitecture.domain.repositories.IRepositoryProducts
import uk.co.itmms.androidArchitecture.domain.repositories.IRepositoryRuntime
import uk.co.itmms.androidArchitecture.domain.repositories.IRepositoryTodos
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

    private lateinit var appDatabase: AppDatabase

    private lateinit var passApi: IBackend

    /**
     * Function initializing all the engines used by the data module
     *
     * This method must be called by the Application in the onCreate method
     */
    fun initDataLayer(applicationContext: Context, releaseMode: Boolean) {
        this.applicationContext = applicationContext

        initFlipper(applicationContext, networkFlipperPlugin)

        appDatabase = openDatabase(applicationContext)
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

    private val repositoryProducts: IRepositoryProducts by lazy {
        RepositoryProducts(dataSourceBackend, dataSourceDatabaseProducts)
    }

    private val repositoryTodos: IRepositoryTodos by lazy {
        RepositoryTodos(dataSourceBackend, dataSourceDatabaseTodos)
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

    private val dataSourceDatabaseProducts: IDataSourceDatabaseProducts by lazy {
        DataSourceDatabaseProducts(appDatabase)
    }

    private val dataSourceDatabaseTodos: IDataSourceDatabaseTodos by lazy {
        DataSourceDatabaseTodos(appDatabase)
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