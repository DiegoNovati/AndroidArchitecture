package com.elt.passsystem.di

import com.elt.passsystem.services.IServiceActivityBus
import com.elt.passsystem.services.IServiceNavigation
import com.elt.passsystem.services.ServiceActivityBus
import com.elt.passsystem.services.ServiceNavigation
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ModuleApp {

    // Here we link the interfaces with the classes injected that implements them

    @Binds
    @Singleton
    abstract fun bindServiceNavigation(
        serviceNavigation: ServiceNavigation
    ): IServiceNavigation

    @Binds
    @Singleton
    abstract fun bindServiceActivityBus(
        serviceNavigation: ServiceActivityBus
    ): IServiceActivityBus
}

