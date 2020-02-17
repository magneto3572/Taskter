package com.example.bishal.taskter

import android.app.Activity
import android.app.admin.DevicePolicyManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.widget.Toast

class lockscren(private val activity: Context) {
    companion object {
        const val RESULT_ENABLE = 11
    }

    private var devicePolicyManager: DevicePolicyManager = activity.getSystemService(Context.DEVICE_POLICY_SERVICE) as DevicePolicyManager
    private var compName: ComponentName = ComponentName(activity, DeviceAdmin::class.java)
    public var active = devicePolicyManager.isAdminActive(compName)

    fun lock() {
        devicePolicyManager.lockNow()
    }

    fun disable(){
        devicePolicyManager.removeActiveAdmin(compName)
    }

    fun permission() {
        val active = devicePolicyManager.isAdminActive(compName)
        if (!active) {
            val intent = Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN)
            intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, compName)
            intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "Additional text explaining why we need this permission")
            (activity as Activity).startActivityForResult(intent, RESULT_ENABLE)
        } else {
            Toast.makeText(activity, "Already Granted", Toast.LENGTH_SHORT).show()
        }
    }




}