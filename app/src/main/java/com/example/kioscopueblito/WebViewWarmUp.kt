package com.example.kioscopueblito

import android.content.Context
import android.webkit.WebView

object WebViewWarmUp {

    fun warmUp(context: Context) {
        try {
            val webView = WebView(context)
            webView.settings.javaScriptEnabled = true
            webView.loadUrl("about:blank")
        } catch (_: Exception) {}
    }
}
