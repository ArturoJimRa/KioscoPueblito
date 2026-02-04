package com.example.kioscopueblito

import android.app.admin.DeviceAdminReceiver

/**
 * MyDeviceAdminReceiver
 * ---------------------
 * Receptor de administración del dispositivo.
 *
 * Esta clase es necesaria para que la aplicación pueda
 * operar como **Device Owner**, lo cual permite:
 *
 * - Habilitar el modo kiosco (Lock Task Mode)
 * - Restringir el uso del sistema
 * - Controlar el comportamiento del dispositivo
 *
 * No contiene lógica adicional ya que su sola
 * declaración es suficiente para que Android
 * reconozca la aplicación como administradora.
 */
class MyDeviceAdminReceiver : DeviceAdminReceiver()
