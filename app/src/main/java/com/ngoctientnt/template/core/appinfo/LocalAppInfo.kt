package com.ngoctientnt.template.core.appinfo

import androidx.compose.runtime.compositionLocalOf
import com.ngoctientnt.template.core.appinfo.model.AppInfo

val LocalAppInfo = compositionLocalOf<AppInfo?> { null }

val LocalAppInfoManager = compositionLocalOf<AppInfoManager?> { null }
