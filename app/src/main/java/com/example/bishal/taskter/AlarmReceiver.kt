package com.example.bishal.taskter

import android.bluetooth.BluetoothAdapter
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.AudioFocusRequest
import android.media.AudioManager
import android.net.wifi.WifiManager
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity


@Suppress("DEPRECATION")
class AlarmReceiver : BroadcastReceiver() , AudioManager.OnAudioFocusChangeListener {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onReceive(context: Context, intent: Intent) {
        val extra = intent.extras
        val wifi = extra?.getBoolean("wifi")
        val music = extra?.getBoolean("music")
        val bluetooth = extra?.getBoolean("bluetooth")
        val lockscreen = extra?.getBoolean("lockscreen")
        if (wifi!!) {
            Toast.makeText(context,"Wifi Closed",Toast.LENGTH_LONG).show()
            disableWifi(context)
        }
        if (bluetooth!!) {
            Toast.makeText(context,"Bluetooth Closed",Toast.LENGTH_LONG).show()
            disableBluetooth(context)
        }
        if (music!!) {
            Toast.makeText(context,"Music Closed",Toast.LENGTH_LONG).show()
            disableMusic(context)
        }
        if (lockscreen!!){
            Toast.makeText(context,"locked",Toast.LENGTH_LONG).show()
            val d =lockscren(context)
            d.lock()
            val stopIntent = Intent(context, ForegroundService::class.java)
            stopIntent.action= "Stop Foreground Service"
            context.stopService(stopIntent)
        }
    }

    override fun onAudioFocusChange(focusChange: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    private fun disableBluetooth(context: Context) {
        val bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        bluetoothAdapter?.disable()
        val stopIntent = Intent(context, ForegroundService::class.java)
        stopIntent.action= "Stop Foreground Service"
        context.stopService(stopIntent)
    }

    private fun disableMusic(context: Context) {

         if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
             val mAudioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager?
             mAudioManager!!.requestAudioFocus(
                AudioFocusRequest.Builder(AudioManager.AUDIOFOCUS_GAIN)
                    .setAcceptsDelayedFocusGain(true)
                    .setOnAudioFocusChangeListener {
                    }.build()

             )
             val stopIntent = Intent(context, ForegroundService::class.java)
             stopIntent.action= "Stop Foreground Service"
             context.stopService(stopIntent)
         }
         else {(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
             val am = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
             lateinit var afChangeListener: AudioManager.OnAudioFocusChangeListener
             if (am.isMusicActive) {
                 am.requestAudioFocus(
                     {
                     }, AudioManager.STREAM_MUSIC,
                     AudioManager.AUDIOFOCUS_GAIN)
                 val stopIntent = Intent(context, ForegroundService::class.java)
                 stopIntent.action= "Stop Foreground Service"
                 context.stopService(stopIntent)
             }
         }
    }

    private fun disableWifi(context: Context) {

        val wifiManager = context.applicationContext.getSystemService(AppCompatActivity.WIFI_SERVICE)
        if (wifiManager is WifiManager) {
            wifiManager.isWifiEnabled = false
            val stopIntent = Intent(context, ForegroundService::class.java)
            stopIntent.action= "Stop Foreground Service"
            context.stopService(stopIntent)
        }

    }
}

private fun Any.requestAudioFocus(context: Context, streaM_MUSIC: Int, audiofocuS_GAIN: Int) {

}

private fun AudioManager.abandonAudioFocusRequest(focusChangeListener: AudioManager.OnAudioFocusChangeListener, streaM_MUSIC: Int, audiofocuS_GAIN: Int): Int {
        return audiofocuS_GAIN

}

