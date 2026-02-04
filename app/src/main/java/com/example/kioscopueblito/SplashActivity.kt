package com.example.kioscopueblito

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

/**
 * SplashActivity
 * --------------
 * Actividad de presentación del sistema kiosco.
 *
 * Su función principal es:
 * - Mostrar una pantalla inicial con animación del logotipo
 * - Ocultar la barra de navegación y estado
 * - Redirigir automáticamente a la actividad principal del kiosco
 *
 * Esta actividad se ejecuta al iniciar la aplicación y
 * sirve como transición visual antes de cargar el WebView.
 */
class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        // 🔒 Oculta la barra de estado y navegación para mantener el modo kiosco
        ocultarSistema()

        // 🎬 Cargar animación del logotipo
        val anim = AnimationUtils.loadAnimation(this, R.anim.logo_anim)

        // 🖼️ Referencias a los logotipos
        val logoIzq = findViewById<ImageView>(R.id.logoIzq)
        val logoDer = findViewById<ImageView>(R.id.logoDer)

        // ▶️ Iniciar animación en ambos logotipos
        logoIzq.startAnimation(anim)
        logoDer.startAnimation(anim)

        // ⏱️ Espera de 2 segundos antes de lanzar el kiosco principal
        Handler(Looper.getMainLooper()).postDelayed({

            // 🚀 Inicia la actividad principal del kiosco
            startActivity(Intent(this, KioscoWebActivity::class.java))

            // ❌ Finaliza esta actividad para evitar regresar a ella
            finish()

        }, 2000)
    }

    /**
     * ocultarSistema
     * --------------
     * Activa el modo inmersivo para ocultar:
     * - Barra de navegación
     * - Barra de estado
     *
     * Esto evita que el usuario acceda a controles del sistema
     * mientras se ejecuta el kiosco.
     */
    private fun ocultarSistema() {
        window.decorView.systemUiVisibility =
            (View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_FULLSCREEN)
    }
}
