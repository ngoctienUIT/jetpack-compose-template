package com.ngoctientnt.template.di

import com.ngoctientnt.template.app.navigation.AppBackStack
import com.ngoctientnt.template.app.navigation.AppNavigator
import com.ngoctientnt.template.app.navigation.AppNavigatorImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NavigationModule {

    @Provides
    @Singleton
    fun provideAppBackStack(): AppBackStack = AppBackStack()

    @Provides
    @Singleton
    fun provideAppNavigator(appBackStack: AppBackStack): AppNavigator =
        AppNavigatorImpl(appBackStack)
}
