package com.elt.passsystem.di

import com.elt.passsystem.data.DataInterface
import com.elt.passsystem.domain.usecases.authentication.UseCaseAuthenticationLogin
import com.elt.passsystem.domain.usecases.authentication.UseCaseAuthenticationLogout
import com.elt.passsystem.domain.usecases.networkMonitor.UseCaseNetworkMonitor
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
    fun provideUseCaseNetworkMonitor(): UseCaseNetworkMonitor =
        DataInterface.useCaseNetworkMonitor

    @Provides
    @Singleton
    fun provideUseCaseAuthenticationLogin(): UseCaseAuthenticationLogin =
        DataInterface.useCaseAuthenticationLogin


    @Provides
    @Singleton
    fun provideUseCaseAuthenticationLogout(): UseCaseAuthenticationLogout =
        DataInterface.useCaseAuthenticationLogout
}