package com.example.kioscopueblito.device

import android.app.admin.DevicePolicyManager
import android.content.ComponentName
import android.content.Context
import com.example.kioscopueblito.MyDeviceAdminReceiver

object DeviceOwnerManager {

    fun enableKiosk(context: Context) {
        val dpm = context.getSystemService(Context.DEVICE_POLICY_SERVICE) as DevicePolicyManager
        val admin = ComponentName(context, MyDeviceAdminReceiver::class.java)

        if (dpm.isDeviceOwnerApp(context.packageName)) {
            dpm.setLockTaskPackages(admin, arrayOf(context.packageName))
        }
    }

    fun disableStatusBar(context: Context) {
        val dpm = context.getSystemService(Context.DEVICE_POLICY_SERVICE) as DevicePolicyManager
        val admin = ComponentName(context, MyDeviceAdminReceiver::class.java)

        if (dpm.isDeviceOwnerApp(context.packageName)) {
            dpm.setStatusBarDisabled(admin, true)
        }
    }
}
