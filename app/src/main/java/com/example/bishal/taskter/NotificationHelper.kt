package com.example.bishal.taskter

import android.annotation.TargetApi
import android.app.*
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.core.app.NotificationCompat

object NotificationHelper {

    public const val NOTIFICATION_TAG = "foreground"
    public const val notificationid = 1

    fun notify(
        context: Context,
        exampleString: String, number: Int
    ) {
        val res = context.resources
        val title = res.getString(
            R.string.helper_notification_title_template, exampleString
        )
        val text = res.getString(
            R.string.helper_notification_placeholder_text_template, exampleString
        )

        val builder = NotificationCompat.Builder(context, NOTIFICATION_TAG)
            .setDefaults(Notification.DEFAULT_ALL)
            .setContentTitle(title)
            .setContentText(text)
            .setSmallIcon(R.mipmap.icon)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setTicker(exampleString)
            .setNumber(number)
            .setContentIntent(
                PendingIntent.getActivity(
                    context,
                    0,
                    Intent(Intent.ACTION_VIEW, Uri.parse("http://www.google.com")),
                    PendingIntent.FLAG_UPDATE_CURRENT
                )
            )
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText(text)
                    .setBigContentTitle(title)
                    .setSummaryText("Dummy summary text")
            )
            .addAction(
                R.drawable.ic_action_stat_share,
                res.getString(R.string.action_share),
                PendingIntent.getActivity(
                    context,
                    0,
                    Intent.createChooser(
                        Intent(Intent.ACTION_SEND)
                            .setType("text/plain")
                            .putExtra(Intent.EXTRA_TEXT, "Dummy text"), "Dummy title"
                    ),
                    PendingIntent.FLAG_UPDATE_CURRENT
                )
            )
            .addAction(
                R.drawable.ic_action_stat_reply,
                res.getString(R.string.action_reply),
                null
            )
            .setAutoCancel(true)

        notify(context, builder.build())
    }


    public fun notify(context: Context, notification: Notification) {
        val nm = context
            .getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            nm.notify(NOTIFICATION_TAG, 0, notification)
        } else {
            nm.notify(NOTIFICATION_TAG.hashCode(), notification)
        }

    }
    @TargetApi(Build.VERSION_CODES.KITKAT)
    fun cancel(context: Context) {
        val nm = context
            .getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            nm.cancel(NOTIFICATION_TAG, 0)
        } else {
            nm.cancel(NOTIFICATION_TAG.hashCode())
        }
    }
}
