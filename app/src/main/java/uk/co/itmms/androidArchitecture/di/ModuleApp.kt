package uk.co.itmms.androidArchitecture.di

import uk.co.itmms.androidArchitecture.services.IServiceNavigation
import uk.co.itmms.androidArchitecture.services.ServiceNavigation
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
}

