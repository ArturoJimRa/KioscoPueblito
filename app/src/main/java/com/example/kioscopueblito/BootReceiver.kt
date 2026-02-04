package com.example.kioscopueblito

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

/**
 * BootReceiver
 * ------------
 * Receptor de eventos del sistema que se ejecuta automáticamente
 * cuando la tablet termina de encender (BOOT_COMPLETED).
 *
 * Su función es iniciar el flujo del kiosco sin intervención del usuario,
 * garantizando que la aplicación se abra de forma automática al encender
 * el dispositivo.
 */
class BootReceiver : BroadcastReceiver() {

    /**
     * Método que se ejecuta al recibir un evento del sistema.
     *
     * @param context Contexto del sistema en el momento del arranque
     * @param intent  Intent recibido (en este caso, BOOT_COMPLETED)
     */
    override fun onReceive(context: Context, intent: Intent) {

        // Verifica que el evento recibido sea el arranque del sistema
        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {

            // Intent para lanzar el launcher del kiosco
            val i = Intent(context, LauncherActivity::class.java)

            // Flags para asegurar un arranque limpio del flujo del kiosco
            i.addFlags(
                Intent.FLAG_ACTIVITY_NEW_TASK or
                        Intent.FLAG_ACTIVITY_CLEAR_TASK or
                        Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS
            )

            // Inicia el launcher del kiosco
            context.startActivity(i)
        }
    }
}
