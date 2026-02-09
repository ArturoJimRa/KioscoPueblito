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

/**
 * KioscoWebActivity
 * -----------------
 * Actividad principal del sistema kiosco.
 *
 * Funciones principales:
 * - Cargar una página web en pantalla completa mediante WebView
 * - Forzar vista de escritorio en el navegador embebido
 * - Activar el modo kiosco (Lock Task)
 * - Ocultar la barra de navegación y botones del sistema
 * - Permitir acceso administrativo mediante un PIN secreto
 */
class KioscoWebActivity : AppCompatActivity() {

    /** WebView que muestra el contenido del kiosco */
    private lateinit var webView: WebView

    /** Preferencias compartidas para guardar configuración del kiosco */
    private lateinit var prefs: SharedPreferences

    /** URL por defecto del kiosco */
    private val DEFAULT_URL = "https://miviejopueblito.mx/"

    /** PIN secreto para acceder a la configuración administrativa */
    private val SECRET_PIN = "280487" // 🔐 Pin Sistemas

    /**
     * Método que se ejecuta al crear la actividad.
     * Inicializa el modo kiosco, la vista web y el botón secreto.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kiosco_web)

        // 🔒 Autoriza la aplicación para usar Lock Task (modo kiosco)
        configurarLockTask()

        // Oculta la barra del sistema y navegación
        ocultarSistema()

        // Inicialización de preferencias
        prefs = getSharedPreferences("kiosco_prefs", MODE_PRIVATE)

        // Inicialización del WebView
        webView = findViewById(R.id.webView)

        configurarWebView()
        configurarBotonSecreto()
    }

    /**
     * Se ejecuta cuando la actividad vuelve a primer plano.
     * Activa el bloqueo del sistema y recarga la URL configurada.
     */
    override fun onResume() {
        super.onResume()

        // 🔒 Inicia el modo kiosco (Lock Task)
        try {
            startLockTask()
        } catch (_: Exception) {}

        // Carga la URL almacenada o la URL por defecto
        val url = prefs.getString("kiosco_url", DEFAULT_URL)!!
        webView.loadUrl(url)

        // Monitoreo de estabilidad del WebView
        KioskWatchdog.monitor(webView, url)

        // Refuerza el ocultamiento del sistema
        ocultarSistema()
    }

    /**
     * Configura la aplicación como permitida para el modo Lock Task.
     * Este método solo funciona si la app está registrada como Device Owner.
     */
    private fun configurarLockTask() {
        val dpm = getSystemService(DEVICE_POLICY_SERVICE) as DevicePolicyManager
        val admin = ComponentName(this, MyDeviceAdminReceiver::class.java)

        if (dpm.isDeviceOwnerApp(packageName)) {
            dpm.setLockTaskPackages(admin, arrayOf(packageName))
        }
    }

    /**
     * Configura el WebView para forzar la vista de escritorio,
     * habilitar JavaScript y permitir carga de contenido dinámico.
     */
    private fun configurarWebView() {
        val s = webView.settings

        // 🖥️ User-Agent de navegador de escritorio
        s.userAgentString =
            "Mozilla/5.0 (Windows NT 10.0; Win64; x64) " +
                    "AppleWebKit/537.36 (KHTML, like Gecko) " +
                    "Chrome/120.0.0.0 Safari/537.36"

        // Habilitar soporte web moderno
        s.javaScriptEnabled = true
        s.domStorageEnabled = true

        // Forzar vista de escritorio
        s.useWideViewPort = true
        s.loadWithOverviewMode = true

        // Deshabilitar zoom para evitar comportamiento móvil
        s.setSupportZoom(false)
        s.builtInZoomControls = false
        s.displayZoomControls = false
        s.textZoom = 100

        // Permitir acceso a archivos y contenido
        s.allowFileAccess = true
        s.allowContentAccess = true

        webView.webChromeClient = WebChromeClient()

        // Cliente WebView personalizado
        webView.webViewClient = object : WebViewClient() {

            /**
             * Intercepta la carga de URLs.
             * Los archivos PDF se abren mediante Google Viewer.
             */
            override fun shouldOverrideUrlLoading(
                view: WebView,
                request: WebResourceRequest
            ): Boolean {

                val url = request.url.toString()

                // 📄 Manejo interno de archivos PDF
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

    /**
     * Configura un botón invisible que permite
     * el acceso a la configuración mediante un PIN.
     */
    private fun configurarBotonSecreto() {
        val btn = findViewById<View>(R.id.btnSecreto)
        btn.setOnLongClickListener {
            pedirPin()
            true
        }
    }

    /**
     * Muestra un diálogo para solicitar el PIN administrativo.
     * Si el PIN es correcto, se accede a la configuración.
     */
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

    /**
     * Oculta la barra de navegación y el sistema
     * para impedir la salida del modo kiosco.
     */
    private fun ocultarSistema() {
        window.decorView.systemUiVisibility =
            (View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_FULLSCREEN)
    }

    /**
     * Refuerza el modo kiosco cuando la aplicación recupera el foco.
     */
    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) {
            ocultarSistema()
        }
    }

    /**
     * Deshabilita el botón de retroceso del sistema.
     */
    override fun onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack()
        }
        // ❌ Nunca sale del kiosco
    }
}
