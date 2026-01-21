package com.example.kioscopueblito

import android.os.Handler
import android.os.Looper
import android.webkit.WebView

object KioskWatchdog {

    private val handler = Handler(Looper.getMainLooper())
    private var runnable: Runnable? = null

    fun monitor(webView: WebView, url: String) {
        runnable?.let { handler.removeCallbacks(it) }

        runnable = Runnable {
            try {
                webView.reload()
            } catch (_: Exception) {}
        }

        handler.postDelayed(runnable!!, 60_000) // 60 segundos
    }
}
