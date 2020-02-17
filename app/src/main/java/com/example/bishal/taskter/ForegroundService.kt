package com.example.bishal.taskter


import android.app.AlarmManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import java.util.*


class ForegroundService : Service() {
    companion object{
        private val tag = "myLogTag"

    }


    override fun onBind(intent: Intent): IBinder {
        TODO("Return the communication channel to the service.")
    }

    override fun onCreate() {
        super.onCreate()
    }

    private fun startAlarm( intent: Intent){
        val extra = intent.extras
        val wifi = extra?.getBoolean("wifi")
        val music = extra?.getBoolean("music")
        val bluetooth = extra?.getBoolean("bluetooth")
        val lockscreen = extra?.getBoolean("lockscreen")
        val cal = extra!!.getLong("cal")
        val c = Calendar.getInstance()
        val date = c.time
        c.timeInMillis=cal
        val broadcastIntent = Intent(this, AlarmReceiver::class.java)
        broadcastIntent.putExtra("wifi", wifi)
        broadcastIntent.putExtra("bluetooth", bluetooth)
        broadcastIntent.putExtra("music", music)
        broadcastIntent.putExtra("lockscreen",lockscreen)
        val pstart = PendingIntent.getBroadcast(this, 0, broadcastIntent, 0)
        val alarmMgr = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmMgr.setExact(AlarmManager.RTC_WAKEUP, cal, pstart)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val extra = intent?.extras
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (intent?.action == "Start Foreground Service") {
                startAlarm(intent)
                val stopIntent = Intent(this, ForegroundService::class.java)
                stopIntent.action = "Stop Foreground Service"
                val pStop = PendingIntent.getService(this, 0, stopIntent, 0)
                val notification = NotificationCompat.Builder(this, NotificationHelper.NOTIFICATION_TAG)
                    .setContentTitle("Timer Running")
                    .setWhen(System.currentTimeMillis())
                    .setUsesChronometer(true)
                    .setBadgeIconType(NotificationCompat.BADGE_ICON_SMALL)
                    .setSmallIcon(R.drawable.ic_stat_name)
                    .setChannelId("foreground")
                    .setContentIntent(pStop)
                    .setOngoing(true)
                    .addAction(R.drawable.ic_action_stat_reply, "Stop Timer", pStop)
                    .build()
                startForeground(NotificationHelper.notificationid, notification)
            } else if (intent?.action == "Stop Foreground Service") {
                val broadcastIntent = Intent(this, AlarmReceiver::class.java)
                val pstart = PendingIntent.getBroadcast(this, 0, broadcastIntent, 0)
                val alarmMgr = getSystemService(Context.ALARM_SERVICE) as AlarmManager
                alarmMgr.cancel(pstart)
                stopForeground(true)
                stopSelf()
            }
        }
        else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            if (intent?.action == "Start Foreground Service") {
                val stopIntent = Intent(this, ForegroundService::class.java)
                stopIntent.action = "Stop Foreground Service"
                val pStop = PendingIntent.getService(this, 0, stopIntent, 0)
                val notification = NotificationCompat.Builder(this, NotificationHelper.NOTIFICATION_TAG)
                    .setContentTitle("Timer Running")
                    .setBadgeIconType(NotificationCompat.BADGE_ICON_SMALL)
                    .setSmallIcon(R.drawable.ic_stat_name)
                    .setWhen(System.currentTimeMillis())
                    .setUsesChronometer(true)
                    .setChannelId("foreground")
                    .setContentIntent(pStop)
                    .setOngoing(true)
                    .setAutoCancel(true)
                    .addAction(R.drawable.ic_action_stat_reply, "Stop Timer", pStop)
                    .build()
                NotificationHelper.notify(this, notification)

            } else if (intent?.action == "Stop Foreground Service") {
                    val broadcastIntent = Intent(this, AlarmReceiver::class.java)
                    val pstart = PendingIntent.getBroadcast(this, 0, broadcastIntent, 0)
                    val alarmMgr = getSystemService(Context.ALARM_SERVICE) as AlarmManager
                    alarmMgr.cancel(pstart)
                    NotificationHelper.cancel(this)
                    stopService(intent)
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }


    override fun onDestroy() {
        val broadcastIntent = Intent(this, AlarmReceiver::class.java)
        val pstart = PendingIntent.getBroadcast(this, 0, broadcastIntent, 0)
        val alarmMgr = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmMgr.cancel(pstart)
        NotificationHelper.cancel(this)
        stopSelf()
        super.onDestroy()

    }
}

