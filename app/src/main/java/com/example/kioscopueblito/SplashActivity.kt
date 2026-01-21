package com.example.kioscopueblito

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        ocultarSistema()

        val anim = AnimationUtils.loadAnimation(this, R.anim.logo_anim)

        val logoIzq = findViewById<ImageView>(R.id.logoIzq)
        val logoDer = findViewById<ImageView>(R.id.logoDer)

        logoIzq.startAnimation(anim)
        logoDer.startAnimation(anim)

        Handler(Looper.getMainLooper()).postDelayed({
            // ✅ NOMBRE CORRECTO DE LA ACTIVIDAD
            startActivity(Intent(this, KioscoWebActivity::class.java))
            finish()
        }, 2000)
    }

    private fun ocultarSistema() {
        window.decorView.systemUiVisibility =
            (View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_FULLSCREEN)
    }
}
