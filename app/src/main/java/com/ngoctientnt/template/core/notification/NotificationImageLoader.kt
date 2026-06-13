package com.ngoctientnt.template.core.notification

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import javax.inject.Inject
import javax.inject.Singleton
import okhttp3.OkHttpClient
import okhttp3.Request

@Singleton
class NotificationImageLoader @Inject constructor(
    private val okHttpClient: OkHttpClient,
) {
    fun load(url: String): Bitmap? = runCatching {
        val request = Request.Builder().url(url).build()
        okHttpClient.newCall(request).execute().use { response ->
            if (!response.isSuccessful) return null
            val bytes = response.body?.bytes() ?: return null
            BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
        }
    }.onFailure { error ->
        Log.w(TAG, "Failed to load notification image: $url", error)
    }.getOrNull()

    fun scaleForLargeIcon(bitmap: Bitmap, maxSize: Int = LARGE_ICON_MAX_SIZE): Bitmap {
        if (bitmap.width <= maxSize && bitmap.height <= maxSize) return bitmap

        val ratio = minOf(
            maxSize.toFloat() / bitmap.width,
            maxSize.toFloat() / bitmap.height,
        )
        val width = (bitmap.width * ratio).toInt()
        val height = (bitmap.height * ratio).toInt()
        return Bitmap.createScaledBitmap(bitmap, width, height, true)
    }

    companion object {
        private const val TAG = "NotificationImageLoader"
        private const val LARGE_ICON_MAX_SIZE = 256
    }
}
