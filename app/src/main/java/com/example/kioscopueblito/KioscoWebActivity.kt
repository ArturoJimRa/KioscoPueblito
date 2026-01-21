package com.example.kioscopueblito

import android.app.admin.DevicePolicyManager
import android.content.ComponentName
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.webkit.*
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import java.net.URLEncoder

class KioscoWebActivity : AppCompatActivity() {

    private lateinit var webView: WebView
    private lateinit var prefs: SharedPreferences

    private val DEFAULT_URL = "https://miviejopueblito.mx/"
    private val SECRET_PIN = "1234" // 🔐 CAMBIA ESTE PIN

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kiosco_web)

        // 🔒 🔥 AUTORIZACIÓN LOCK TASK (FALTABA)
        configurarLockTask()

        ocultarSistema()

        prefs = getSharedPreferences("kiosco_prefs", MODE_PRIVATE)
        webView = findViewById(R.id.webView)

        configurarWebView()
        configurarBotonSecreto()
    }

    override fun onResume() {
        super.onResume()

        // 🔒 BLOQUEO REAL (YA AUTORIZADO)
        try {
            startLockTask()
        } catch (_: Exception) {}

        val url = prefs.getString("kiosco_url", DEFAULT_URL)!!
        webView.loadUrl(url)
        KioskWatchdog.monitor(webView, url)


        ocultarSistema()
    }

    // 🔐 🔥 MÉTODO CLAVE QUE NO TENÍAS
    private fun configurarLockTask() {
        val dpm = getSystemService(DEVICE_POLICY_SERVICE) as DevicePolicyManager
        val admin = ComponentName(this, MyDeviceAdminReceiver::class.java)

        if (dpm.isDeviceOwnerApp(packageName)) {
            dpm.setLockTaskPackages(admin, arrayOf(packageName))
        }
    }

    private fun configurarWebView() {
        val s = webView.settings
        s.javaScriptEnabled = true
        s.domStorageEnabled = true
        s.useWideViewPort = true
        s.loadWithOverviewMode = true
        s.allowFileAccess = true
        s.allowContentAccess = true

        webView.webChromeClient = WebChromeClient()

        webView.webViewClient = object : WebViewClient() {

            override fun shouldOverrideUrlLoading(
                view: WebView,
                request: WebResourceRequest
            ): Boolean {

                val url = request.url.toString()

                // 📄 PDF INTERNO
                if (url.endsWith(".pdf", true)) {
                    val pdfViewer =
                        "https://docs.google.com/gview?embedded=true&url=" +
                                URLEncoder.encode(url, "UTF-8")
                    view.loadUrl(pdfViewer)
                    return true
                }

                return false
            }
        }
    }

    // 🔐 BOTÓN INVISIBLE
    private fun configurarBotonSecreto() {
        val btn = findViewById<View>(R.id.btnSecreto)
        btn.setOnLongClickListener {
            pedirPin()
            true
        }
    }

    private fun pedirPin() {
        val input = EditText(this)
        input.inputType =
            android.text.InputType.TYPE_CLASS_NUMBER or
                    android.text.InputType.TYPE_NUMBER_VARIATION_PASSWORD

        AlertDialog.Builder(this)
            .setTitle("Configuración")
            .setMessage("Ingresa el PIN")
            .setView(input)
            .setPositiveButton("Entrar") { _, _ ->
                if (input.text.toString() == SECRET_PIN) {
                    stopLockTask()
                    startActivity(Intent(this, AdminConfigActivity::class.java))
                }
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun ocultarSistema() {
        window.decorView.systemUiVisibility =
            (View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_FULLSCREEN)
    }

    // 🔒 REFUERZO: si el sistema recupera foco
    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) {
            ocultarSistema()
        }
    }

    override fun onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack()
        }
        // ❌ nunca sale
    }
}
