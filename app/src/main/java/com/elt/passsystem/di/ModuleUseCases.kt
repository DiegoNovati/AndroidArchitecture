package com.elt.passsystem.di

import com.elt.passsystem.data.DataInterface
import com.elt.passsystem.domain.repositories.IRepositoryAnalytics
import com.elt.passsystem.domain.repositories.IRepositoryLogger
import com.elt.passsystem.domain.usecases.authentication.UseCaseAuthenticationLogin
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
    fun provideRepositoryLogger(): IRepositoryLogger =
        DataInterface.repositoryLogger

    @Provides
    @Singleton
    fun provideRepositoryAnalytics(): IRepositoryAnalytics =
        DataInterface.repositoryAnalytics

    @Provides
    fun provideUseCaseAuthenticationLogin(
        repositoryLogger: IRepositoryLogger,
        repositoryAnalytics: IRepositoryAnalytics,
    ): UseCaseAuthenticationLogin =
        UseCaseAuthenticationLogin(
            repositoryLogger, repositoryAnalytics
        )
}