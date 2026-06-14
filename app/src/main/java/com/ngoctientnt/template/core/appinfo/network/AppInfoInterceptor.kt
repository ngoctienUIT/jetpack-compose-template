package com.ngoctientnt.template.core.appinfo.network

import com.ngoctientnt.template.core.appinfo.AppInfoManager
import com.ngoctientnt.template.core.appinfo.config.AppInfoConfig
import com.ngoctientnt.template.core.config.NetworkConfig
import com.ngoctientnt.template.core.logging.AppLogger
import javax.inject.Inject
import okhttp3.Interceptor
import okhttp3.Response

class AppInfoInterceptor @Inject constructor(
    private val appInfoManager: AppInfoManager,
    private val appInfoConfig: AppInfoConfig,
    private val networkConfig: NetworkConfig,
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        if (!networkConfig.attachAppInfoHeaders || !appInfoConfig.includeHttpHeaders) {
            return chain.proceed(chain.request())
        }

        val appInfo = appInfoManager.peekAppInfo()
        if (appInfo == null) {
            AppLogger.w(TAG, "AppInfo not ready — proceeding without metadata headers")
            return chain.proceed(chain.request())
        }

        val requestBuilder = chain.request().newBuilder()
            .header("User-Agent", appInfo.toUserAgent())

        appInfo.toHttpHeaders(appInfoConfig).forEach { (key, value) ->
            requestBuilder.header(key, value)
        }

        return chain.proceed(requestBuilder.build())
    }

    companion object {
        private const val TAG = "AppInfoInterceptor"
    }
}
