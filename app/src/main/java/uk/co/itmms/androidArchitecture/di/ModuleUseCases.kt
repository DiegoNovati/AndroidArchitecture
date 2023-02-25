package uk.co.itmms.androidArchitecture.di

import uk.co.itmms.androidArchitecture.data.DataInterface
import uk.co.itmms.androidArchitecture.domain.usecases.home.UseCaseHomeMonitor
import uk.co.itmms.androidArchitecture.domain.usecases.home.UseCaseHomeInit
import uk.co.itmms.androidArchitecture.domain.usecases.home.UseCaseHomeLogout
import uk.co.itmms.androidArchitecture.domain.usecases.login.UseCaseLoginMonitor
import uk.co.itmms.androidArchitecture.domain.usecases.login.UseCaseLoginLogin
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object ModuleUseCases {

    @Provides
    fun provideUseCaseLoginFlow(): UseCaseLoginMonitor =
        DataInterface.getUseCaseLoginMonitor()

    @Provides
    fun provideUseCaseLoginLogin(): UseCaseLoginLogin =
        DataInterface.getUseCaseLoginLogin()

    @Provides
    fun provideUseCaseHomeInit(): UseCaseHomeInit =
        DataInterface.getUseCaseHomeInit()

    @Provides
    fun provideUseCaseHomeFlow(): UseCaseHomeMonitor =
        DataInterface.getUseCaseHomeMonitor()

    @Provides
    fun provideUseCaseHomeLogout(): UseCaseHomeLogout =
        DataInterface.getUseCaseHomeLogout()
}