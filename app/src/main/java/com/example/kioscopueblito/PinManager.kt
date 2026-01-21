package com.example.kioscopueblito.util

import android.content.Context

object PinManager {

    private const val PREFS = "secure_prefs"
    private const val PIN_HASH = "pin_hash"

    fun savePin(context: Context, pin: String) {
        val hash = HashUtil.sha256(pin)
        context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
            .edit()
            .putString(PIN_HASH, hash)
            .apply()
    }

    fun validatePin(context: Context, pin: String): Boolean {
        val savedHash = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
            .getString(PIN_HASH, null)

        return savedHash == HashUtil.sha256(pin)
    }

    fun hasPin(context: Context): Boolean {
        return context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
            .contains(PIN_HASH)
    }
}
