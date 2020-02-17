package com.example.bishal.taskter


import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.provider.Settings
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.CheckBox
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.iid.FirebaseInstanceId
import de.mateware.snacky.Snacky
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*


@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class MainActivity : AppCompatActivity() {
     private val tag = "myLogTag"
     private var wifi:Boolean=false
     private var bluetooth:Boolean=false
     private var lockscreen:Boolean=false
     private var music:Boolean=false
     private lateinit var firebaseAnalytics: FirebaseAnalytics
     private var doublePressFlag=false

     @SuppressLint("SimpleDateFormat")
     @Suppress("DEPRECATION")
     override fun onCreate(savedInstanceState: Bundle?) {
         super.onCreate(savedInstanceState)
         setContentView(R.layout.activity_main)
         firebaseAnalytics = FirebaseAnalytics.getInstance(this)
         FirebaseInstanceId.getInstance().instanceId
             .addOnCompleteListener(OnCompleteListener { task ->
                 if (!task.isSuccessful) {
                     Log.w(tag, "getInstanceId failed", task.exception)
                     return@OnCompleteListener
                 }
                 task.result?.token
             })
         val c = Calendar.getInstance()!!
         val actionBar = supportActionBar
         actionBar?.title="Taskster"
         actionBar?.setDisplayShowTitleEnabled(true)
         actionBar?.elevation = 0F
         seek_hour.curProcess=4
         seek_minute.curProcess=15
         changeText(4,15)


         if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
             val channel = NotificationChannel(NotificationHelper.NOTIFICATION_TAG, "foreground", NotificationManager.IMPORTANCE_LOW)
             val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
             notificationManager.createNotificationChannel(channel)
         }
         seek_hour.setOnSeekBarChangeListener { _, curValue ->
             changeText(curValue, seek_minute.curProcess)
             c.set(Calendar.HOUR, curValue)
         }
         seek_minute.setOnSeekBarChangeListener { _, curValue ->
             changeText(seek_hour.curProcess, curValue)
             c.set(Calendar.MINUTE, curValue)
             c.set(Calendar.SECOND,0)
         }

         Enable.setOnClickListener {
             startAlarm(c)
         }

         Disable.setOnClickListener {
             val stopIntent = Intent(this, ForegroundService::class.java)
             stopIntent.action= "Stop Foreground Service"
             stopService(stopIntent)
             Toast.makeText(this@MainActivity, "Timer Cancelled", Toast.LENGTH_SHORT).show()
         }

     }

    fun onCheckboxClicked(view: View) {
        if (view is CheckBox) {
            val checked: Boolean = view.isChecked
            when (view.id) {
                R.id.wifi -> {
                    if (checked) {
                        xiaomiPermi()
                        wifi=true
                        Toast.makeText(this, "wifi checked", Toast.LENGTH_SHORT).show()
                    }
                    else{
                        wifi=false
                        Toast.makeText(this, "wifi Unchecked", Toast.LENGTH_SHORT).show()
                    }
                }
                R.id.bluetooth -> {
                    if (checked) {
                        bluetooth=true
                        Toast.makeText(this, "Bluetooth checked", Toast.LENGTH_SHORT).show()
                    } else {
                        bluetooth=false
                        Toast.makeText(this, "Bluetooth Unchecked", Toast.LENGTH_SHORT).show()
                    }
                }
                R.id.lockscreen -> {
                    val d = lockscren(this@MainActivity)
                    if (checked) {
                        val check =d.active
                        if (!check) {
                            val builder = AlertDialog.Builder(this)
                            builder.setTitle(R.string.dialog_title)
                            builder.setMessage(R.string.dialog_text)
                                .setPositiveButton(
                                    R.string.Allow
                                ) { _, _ ->
                                    d.permission()
                                }
                                .setNegativeButton(
                                    R.string.cancel
                                ) { dialog, _ ->
                                    dialog.cancel()
                                }
                            builder.create()
                            builder.show()
                            builder.setCancelable(true)
                            view.isChecked=false
                        }
                        else{
                            lockscreen = true
                            Toast.makeText(this, "lockscreen checked", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        lockscreen=false
                        Toast.makeText(this, "lockscreen Unchecked", Toast.LENGTH_SHORT).show()
                    }
                }
                R.id.Music -> {
                    if (checked) {
                        music=true
                        Toast.makeText(this, "Music checked", Toast.LENGTH_SHORT).show()
                    } else {
                        music=false
                        Toast.makeText(this, "Music Unchecked", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun xiaomiPermi(){
        val manufacturer = Build.MANUFACTURER
        val model = Build.MODEL
        if (("Mi A1" != model)&&("Mi A2" != model)&&("Mi A3" != model)) {
            if ("xiaomi".equals(manufacturer, ignoreCase = true)) {
                val builder = AlertDialog.Builder(this)
                builder.setTitle(R.string.dialog_title)
                builder.setMessage(R.string.dialog_tex)
                    .setPositiveButton(
                        R.string.settings
                    ) { _, _ ->
                        val packageName = "com.example.bishal.test"
                        try {
                            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                            intent.data = Uri.parse("package:$packageName")
                            startActivity(intent)
                        } catch (e: ActivityNotFoundException) {
                            val intent =
                                Intent(Settings.ACTION_MANAGE_APPLICATIONS_SETTINGS)
                            startActivity(intent)
                        }
                    }
                    .setNegativeButton(
                        R.string.cancel
                    ) { dialog, _ ->
                        dialog.cancel()
                    }
                builder.create()
                builder.show()
                builder.setCancelable(true)
                wifi = true
                Toast.makeText(this, "wifi checked", Toast.LENGTH_SHORT).show()
            }
        }
        else{
            wifi = true
            Toast.makeText(this, "wifi checked", Toast.LENGTH_SHORT).show()
        }
    }


    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item != null) {
            when (item.itemId) {
                R.id.action_about -> {
                    val i = Intent(this@MainActivity, About::class.java)
                    startActivity(i)
                    return true
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

     @SuppressLint("SetTextI18n")
     private fun changeText(hour: Int, minute: Int) {
         val hourStr = hour.toString()
         val minuteStr = minute.toString()
         textview.text = "$hourStr:$minuteStr"
     }

     override fun onCreateOptionsMenu(menu: Menu?): Boolean {
         val inflater = menuInflater
         inflater.inflate(R.menu.option, menu)
         return super.onCreateOptionsMenu(menu)
     }

    override fun onBackPressed() {
        if (doublePressFlag){
            super.onBackPressed()
            return
        }
        this.doublePressFlag=true
        Toast.makeText(this, "Tap Back Again To Exit", Toast.LENGTH_SHORT).show()
        Handler().postDelayed({ doublePressFlag=false },2000)
    }

    @Suppress("DEPRECATION")
     private fun startAlarm(c: Calendar){
         val forIntent = Intent(this, ForegroundService::class.java)
         if (c.before(Calendar.getInstance())){
             Snacky.builder()
                 .setActivity(this@MainActivity)
                 .setBackgroundColor(Color.parseColor("#4a6572"))
                 .setTextColor(Color.parseColor("#f9AA33"))
                 .setTextSize(17F)
                 .setActionText("OK")
                 .setActionTextColor(Color.parseColor("#D81B60"))
                 .setActionTextTypeface(Typeface.DEFAULT_BOLD)
                 .setTextTypefaceStyle(Typeface.ITALIC)
                 .setActionClickListener {
                     Toast.makeText(this, "Timer Not Set", Toast.LENGTH_SHORT).show()
                 }
                 .setText("Time is Already Passed...!")
                 .setDuration(Snacky.LENGTH_SHORT)
                 .build()
                 .show()
         }else {
                 val cal = c.timeInMillis
                 if(wifi){
                     forIntent.putExtra("wifi", wifi)
                 }
                 if (bluetooth){
                     forIntent.putExtra("bluetooth", bluetooth)
                 }
                 if (music){
                     forIntent.putExtra("music", music)
                 }
                 if (lockscreen){
                     forIntent.putExtra("lockscreen", lockscreen)
                 }
                 forIntent.putExtra("cal", cal)
                 forIntent.action = "Start Foreground Service"
                 if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                     startForegroundService(forIntent)
                     Toast.makeText(this, "Timer Set", Toast.LENGTH_SHORT).show()
                 } else {
                     startService(forIntent)
                     Toast.makeText(this, "Timer Set", Toast.LENGTH_SHORT).show()
                 }
         }
     }
}




