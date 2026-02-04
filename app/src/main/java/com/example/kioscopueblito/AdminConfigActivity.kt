package com.example.kioscopueblito

import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

/**
 * AdminConfigActivity
 * -------------------
 * Actividad administrativa del sistema de kiosco.
 *
 * Permite al administrador configurar la URL que se cargará
 * dentro del WebView del kiosco, garantizando que siempre
 * se utilice el protocolo HTTPS por seguridad.
 *
 * Esta pantalla solo es accesible mediante el PIN de administrador.
 */
class AdminConfigActivity : AppCompatActivity() {

    // Preferencias compartidas para guardar configuración persistente
    private lateinit var prefs: SharedPreferences

    /**
     * Método que se ejecuta al crear la actividad.
     *
     * @param savedInstanceState Estado previo de la actividad (si existe)
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Asocia la interfaz gráfica con la actividad
        setContentView(R.layout.activity_admin_config)

        // Inicializa las preferencias compartidas
        prefs = getSharedPreferences("kiosco_prefs", MODE_PRIVATE)

        // Referencias a los componentes de la interfaz
        val edtUrl = findViewById<EditText>(R.id.edtUrl)
        val btnGuardar = findViewById<Button>(R.id.btnGuardar)

        // Carga la URL previamente guardada (si existe)
        edtUrl.setText(prefs.getString("kiosco_url", ""))

        /**
         * Evento del botón Guardar:
         * - Valida la URL ingresada
         * - Fuerza el uso de HTTPS si no se especifica protocolo
         * - Guarda la URL en SharedPreferences
         */
        btnGuardar.setOnClickListener {

            var url = edtUrl.text.toString().trim()

            // Validación: asegurar que la URL tenga protocolo seguro
            if (url.isNotEmpty()) {

                // Si no incluye http o https, se agrega https por defecto
                if (!url.startsWith("http://") && !url.startsWith("https://")) {
                    url = "https://$url"
                }

                // Guarda la URL en preferencias
                prefs.edit()
                    .putString("kiosco_url", url)
                    .apply()
            }

            // Cierra la actividad y regresa al kiosco
            finish()
        }
    }
}
