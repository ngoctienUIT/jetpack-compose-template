package com.ngoctientnt.template.core.appinfo

import com.ngoctientnt.template.core.appinfo.data.DeviceInfoProvider
import com.ngoctientnt.template.core.appinfo.data.PackageInfoProvider
import com.ngoctientnt.template.core.appinfo.data.UdidStore
import com.ngoctientnt.template.core.appinfo.model.AppInfo
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppInfoRepository @Inject constructor(
    private val deviceInfoProvider: DeviceInfoProvider,
    private val packageInfoProvider: PackageInfoProvider,
    private val udidStore: UdidStore,
) {
    suspend fun loadAppInfo(): AppInfo {
        return AppInfo(
            device = deviceInfoProvider.read(),
            packageInfo = packageInfoProvider.read(),
            udid = udidStore.getOrCreate(),
        )
    }
}
