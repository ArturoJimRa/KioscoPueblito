package com.example.kioscopueblito

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

/**
 * LauncherActivity
 * ----------------
 * Actividad que funciona como launcher (HOME) del sistema.
 *
 * Esta actividad se ejecuta automáticamente cuando:
 * - Se enciende la tablet
 * - El usuario presiona el botón HOME
 *
 * Su única función es redirigir inmediatamente al flujo del kiosco
 * sin mostrar ninguna interfaz visible.
 */
class LauncherActivity : AppCompatActivity() {

    /**
     * Método que se ejecuta al iniciar la actividad.
     * Lanza directamente la pantalla Splash del kiosco
     * y se cierra para no permanecer en memoria.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 🚀 Lanza inmediatamente el flujo del kiosco
        val intent = Intent(this, SplashActivity::class.java)

        // Se crea una nueva tarea para asegurar el arranque correcto
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

        // Inicia la actividad Splash
        startActivity(intent)

        // ❌ Finaliza la actividad para que no quede en segundo plano
        finish()
    }
}
