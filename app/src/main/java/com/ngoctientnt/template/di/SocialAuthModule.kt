package com.ngoctientnt.template.di

import com.ngoctientnt.template.core.auth.social.DefaultSocialAuthGateway
import com.ngoctientnt.template.core.auth.social.SocialAuthGateway
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class SocialAuthModule {

    @Binds
    @Singleton
    abstract fun bindSocialAuthGateway(
        defaultSocialAuthGateway: DefaultSocialAuthGateway,
    ): SocialAuthGateway
}

@dagger.hilt.EntryPoint
@InstallIn(SingletonComponent::class)
interface SocialAuthLauncherEntryPoint {
    fun socialAuthLauncher(): com.ngoctientnt.template.ui.component.auth.SocialAuthLauncher
}
