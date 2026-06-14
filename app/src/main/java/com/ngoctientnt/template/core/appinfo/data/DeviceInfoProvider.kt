package com.ngoctientnt.template.core.appinfo.data

import android.content.Context
import android.os.Build
import android.provider.Settings
import android.util.DisplayMetrics
import android.view.WindowManager
import com.ngoctientnt.template.core.appinfo.config.AppInfoConfig
import com.ngoctientnt.template.core.appinfo.model.DeviceInfo
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DeviceInfoProvider @Inject constructor(
    @ApplicationContext private val context: Context,
    private val config: AppInfoConfig,
) {
    fun read(): DeviceInfo {
        val displayMetrics = if (config.includeScreenMetrics) {
            readDisplayMetrics()
        } else {
            DisplayMetrics()
        }

        return DeviceInfo(
            brand = Build.BRAND.orEmpty(),
            manufacturer = Build.MANUFACTURER.orEmpty(),
            model = Build.MODEL.orEmpty(),
            device = Build.DEVICE.orEmpty(),
            product = Build.PRODUCT.orEmpty(),
            hardware = Build.HARDWARE.orEmpty(),
            osVersion = Build.VERSION.RELEASE.orEmpty(),
            sdkInt = Build.VERSION.SDK_INT,
            androidId = if (config.includeAndroidId) readAndroidId() else null,
            isPhysicalDevice = isPhysicalDevice(),
            screenWidthPx = displayMetrics.widthPixels,
            screenHeightPx = displayMetrics.heightPixels,
            screenDensity = displayMetrics.density,
            locale = Locale.getDefault().toLanguageTag(),
        )
    }

    private fun readAndroidId(): String? {
        return runCatching {
            Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
        }.getOrNull()?.takeIf { it.isNotBlank() && it != "9774d56d682e549c" }
    }

    private fun readDisplayMetrics(): DisplayMetrics {
        val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        return DisplayMetrics().also { metrics ->
            @Suppress("DEPRECATION")
            windowManager.defaultDisplay.getRealMetrics(metrics)
        }
    }

    private fun isPhysicalDevice(): Boolean {
        return !(Build.FINGERPRINT.startsWith("generic")
            || Build.FINGERPRINT.startsWith("unknown")
            || Build.MODEL.contains("google_sdk", ignoreCase = true)
            || Build.MODEL.contains("Emulator", ignoreCase = true)
            || Build.MODEL.contains("Android SDK built for x86", ignoreCase = true)
            || Build.MANUFACTURER.contains("Genymotion", ignoreCase = true)
            || Build.HARDWARE.contains("goldfish", ignoreCase = true)
            || Build.HARDWARE.contains("ranchu", ignoreCase = true))
    }
}
