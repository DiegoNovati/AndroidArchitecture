package com.elt.passsystem.di

import com.elt.passsystem.data.DataInterface
import com.elt.passsystem.domain.repositories.*
import com.elt.passsystem.domain.usecases.authentication.UseCaseAuthenticationLogin
import com.elt.passsystem.domain.usecases.authentication.UseCaseAuthenticationLogout
import com.elt.passsystem.state.GlobalState
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
    fun provideGlobalState(): GlobalState =
        GlobalState()

    @Provides
    @Singleton
    fun provideRepositoryLogger(): IRepositoryLogger =
        DataInterface.repositoryLogger

    @Provides
    @Singleton
    fun provideRepositoryAnalytics(): IRepositoryAnalytics =
        DataInterface.repositoryAnalytics

    @Provides
    @Singleton
    fun provideRepositoryAuthentication(): IRepositoryAuthentication =
        DataInterface.repositoryAuthentication

    @Provides
    @Singleton
    fun provideRepositoryCustomers(): IRepositoryCustomers =
        DataInterface.repositoryCustomers

    @Provides
    @Singleton
    fun provideRepositoryBookings(): IRepositoryBookings =
        DataInterface.repositoryBookings

    @Provides
    fun provideUseCaseAuthenticationLogin(
        repositoryLogger: IRepositoryLogger,
        repositoryAnalytics: IRepositoryAnalytics,
        repositoryAuthentication: IRepositoryAuthentication,
        repositoryCustomers: IRepositoryCustomers,
        repositoryBookings: IRepositoryBookings,
    ): UseCaseAuthenticationLogin =
        UseCaseAuthenticationLogin(
            repositoryLogger, repositoryAnalytics, repositoryAuthentication, repositoryCustomers,
            repositoryBookings
        )

    @Provides
    fun provideUseCaseAuthenticationLogout(
        repositoryLogger: IRepositoryLogger,
        repositoryAnalytics: IRepositoryAnalytics,
        repositoryAuthentication: IRepositoryAuthentication,
    ): UseCaseAuthenticationLogout =
        UseCaseAuthenticationLogout(
            repositoryLogger, repositoryAnalytics, repositoryAuthentication
        )
}