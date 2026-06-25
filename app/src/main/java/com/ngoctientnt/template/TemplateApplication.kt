package com.ngoctientnt.template

import android.app.Application
import coil.ImageLoader
import coil.ImageLoaderFactory
import com.ngoctientnt.template.core.startup.ApplicationBootstrap
import com.ngoctientnt.template.core.startup.SqlCipherInitializer
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class TemplateApplication : Application(), ImageLoaderFactory {

    @Inject
    lateinit var applicationBootstrap: ApplicationBootstrap

    @Inject
    lateinit var imageLoader: ImageLoader

    override fun newImageLoader(): ImageLoader = imageLoader

    override fun onCreate() {
        SqlCipherInitializer.loadIfNeeded(!BuildConfig.DEBUG)
        super.onCreate()
        applicationBootstrap.start()
    }
}
