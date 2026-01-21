package com.example.kioscopueblito

import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

class AdminConfigActivity : AppCompatActivity() {

    private lateinit var prefs: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_config)

        prefs = getSharedPreferences("kiosco_prefs", MODE_PRIVATE)

        val edtUrl = findViewById<EditText>(R.id.edtUrl)
        val btnGuardar = findViewById<Button>(R.id.btnGuardar)

        edtUrl.setText(prefs.getString("kiosco_url", ""))

        btnGuardar.setOnClickListener {
            var url = edtUrl.text.toString().trim()

            // 🔐 NUEVO: forzar https por default
            if (url.isNotEmpty()) {
                if (!url.startsWith("http://") && !url.startsWith("https://")) {
                    url = "https://$url"
                }

                prefs.edit().putString("kiosco_url", url).apply()
            }

            finish()
        }
    }
}
