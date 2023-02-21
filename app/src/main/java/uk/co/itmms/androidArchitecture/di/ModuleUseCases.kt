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
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ModuleUseCases {

    @Provides
    @Singleton
    fun provideUseCaseLoginFlow(): UseCaseLoginMonitor =
        DataInterface.useCaseLoginMonitor

    @Provides
    @Singleton
    fun provideUseCaseLoginLogin(): UseCaseLoginLogin =
        DataInterface.useCaseLoginLogin

    @Provides
    @Singleton
    fun provideUseCaseHomeInit(): UseCaseHomeInit =
        DataInterface.useCaseHomeInit

    @Provides
    @Singleton
    fun provideUseCaseHomeFlow(): UseCaseHomeMonitor =
        DataInterface.useCaseHomeMonitor

    @Provides
    @Singleton
    fun provideUseCaseHomeLogout(): UseCaseHomeLogout =
        DataInterface.useCaseHomeLogout
}