package com.ngoctientnt.template.core.notification

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Build
import androidx.core.app.NotificationCompat
import com.ngoctientnt.template.MainActivity
import com.ngoctientnt.template.R
import com.ngoctientnt.template.core.config.NotificationRoutes
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NotificationHelper @Inject constructor(
    @ApplicationContext private val context: Context,
    private val notificationImageLoader: NotificationImageLoader,
) {
    private val notificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return

        val channel = android.app.NotificationChannel(
            CHANNEL_ID,
            context.getString(R.string.notification_channel_name),
            NotificationManager.IMPORTANCE_DEFAULT,
        ).apply {
            description = context.getString(R.string.notification_channel_description)
        }
        notificationManager.createNotificationChannel(channel)
    }

    fun showNotification(
        title: String,
        body: String,
        data: Map<String, String> = emptyMap(),
        imageUrl: String? = null,
    ) {
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            putExtra(EXTRA_FROM_NOTIFICATION, true)
            data.forEach { (key, value) -> putExtra(key, value) }
        }

        val notificationId = (System.currentTimeMillis() % Int.MAX_VALUE).toInt()
        val pendingIntent = PendingIntent.getActivity(
            context,
            notificationId,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
        )

        val imageBitmap = imageUrl?.let(notificationImageLoader::load)
        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_stat_notification)
            .setContentTitle(title)
            .setContentText(body)
            .setStyle(buildNotificationStyle(body, imageBitmap))
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        imageBitmap?.let { bitmap ->
            builder.setLargeIcon(notificationImageLoader.scaleForLargeIcon(bitmap))
        }

        notificationManager.notify(notificationId, builder.build())
    }

    private fun buildNotificationStyle(
        body: String,
        imageBitmap: Bitmap?,
    ): NotificationCompat.Style {
        if (imageBitmap == null) {
            return NotificationCompat.BigTextStyle().bigText(body)
        }

        return NotificationCompat.BigPictureStyle()
            .bigPicture(imageBitmap)
            .bigLargeIcon(null as Bitmap?)
    }

    companion object {
        const val CHANNEL_ID = "default_notification_channel"
        const val EXTRA_FROM_NOTIFICATION = "extra_from_notification"
        const val EXTRA_ROUTE = NotificationRoutes.ROUTE
        const val EXTRA_DETAIL_ID = NotificationRoutes.DETAIL_ID
    }
}
