package com.example.kioscopueblito

import android.content.Context
import android.net.ConnectivityManager
import android.os.Handler
import android.os.Looper

object NetworkUtil {

    fun waitForInternet(context: Context, onReady: () -> Unit) {
        val handler = Handler(Looper.getMainLooper())

        val check = object : Runnable {
            override fun run() {
                if (isConnected(context)) {
                    onReady()
                } else {
                    handler.postDelayed(this, 1000)
                }
            }
        }

        handler.post(check)
    }

    private fun isConnected(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val net = cm.activeNetworkInfo
        return net != null && net.isConnected
    }
}
