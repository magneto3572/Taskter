package com.example.bishal.taskter
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import android.util.Log

class FirebaseMessagingService : FirebaseMessagingService(){
    val TAG = "PushNotifactionService"
    lateinit var name: String

    override fun onMessageReceived(p0: RemoteMessage) {
        Log.d(TAG, "From: " + p0.from)

        if (p0.data.isNotEmpty()) {
            Log.d(TAG, "Message data payload: " + p0.data)

        }

        if (p0.notification != null) {
            Log.d(TAG, "Message Notification Body: " + p0.notification!!.body)
        }
    }

    override fun onNewToken(p0: String) {
        Log.d(TAG, "Testing Messaging $p0")
        super.onNewToken(p0)
    }


}

