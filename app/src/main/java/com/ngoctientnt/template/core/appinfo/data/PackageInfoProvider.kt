package com.ngoctientnt.template.core.appinfo.data

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import com.ngoctientnt.template.BuildConfig
import com.ngoctientnt.template.core.appinfo.model.AppPackageInfo
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PackageInfoProvider @Inject constructor(
    @ApplicationContext private val context: Context,
) {
    fun read(): AppPackageInfo {
        val packageManager = context.packageManager
        val packageName = context.packageName

        val packageInfo = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            packageManager.getPackageInfo(
                packageName,
                PackageManager.PackageInfoFlags.of(0),
            )
        } else {
            @Suppress("DEPRECATION")
            packageManager.getPackageInfo(packageName, 0)
        }

        val appName = packageManager.getApplicationLabel(
            packageManager.getApplicationInfo(packageName, 0),
        ).toString()

        val versionCode = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            packageInfo.longVersionCode
        } else {
            @Suppress("DEPRECATION")
            packageInfo.versionCode.toLong()
        }

        return AppPackageInfo(
            appName = appName,
            packageName = packageName,
            versionName = packageInfo.versionName.orEmpty(),
            versionCode = versionCode,
            buildType = BuildConfig.BUILD_TYPE,
            environment = BuildConfig.ENVIRONMENT,
        )
    }
}
