package com.example.kioscopueblito

import android.os.Bundle
import android.text.InputType
import android.view.WindowManager
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.text.NumberFormat
import java.util.Locale

class OrdenActivity : AppCompatActivity() {

    private val menu = mutableListOf<Producto>()
    private val orden = mutableListOf<Producto>()

    private lateinit var ordenAdapter: OrdenAdapter
    private lateinit var txtTotal: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_orden)

        // 🔒 Mantener pantalla encendida (KIOSCO)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        val recyclerMenu = findViewById<RecyclerView>(R.id.recyclerMenu)
        val recyclerOrden = findViewById<RecyclerView>(R.id.recyclerOrden)
        txtTotal = findViewById(R.id.txtTotal)

        cargarMenu()

        // 🔹 ADAPTER DEL MENÚ
        val menuAdapter = MenuAdapter(menu) { productoSeleccionado ->

            val existente = orden.find {
                it.nombre == productoSeleccionado.nombre
            }

            if (existente != null) {
                existente.cantidad++
            } else {
                orden.add(productoSeleccionado.copy())
            }

            ordenAdapter.notifyDataSetChanged()
            calcularTotal()
        }

        // 🔹 ADAPTER DE LA ORDEN
        ordenAdapter = OrdenAdapter(orden) {
            calcularTotal()
        }

        // 🔹 GRID DINÁMICO (CELULAR / TABLET)
        val columnas = if (esTablet()) 4 else 2
        recyclerMenu.layoutManager = GridLayoutManager(this, columnas)
        recyclerMenu.adapter = menuAdapter
        recyclerMenu.addItemDecoration(GridSpacingItemDecoration(columnas, 16))

        // 🔹 LISTA DE ORDEN
        recyclerOrden.layoutManager = LinearLayoutManager(this)
        recyclerOrden.adapter = ordenAdapter

        // 🔐 BOTÓN SECRETO (PIN PARA SALIR)
        txtTotal.setOnLongClickListener {
            mostrarPinSalida()
            true
        }

        calcularTotal()
    }

    // 🔒 ACTIVAR MODO KIOSCO
    override fun onResume() {
        super.onResume()
        startLockTask()
    }

    // 📦 MENÚ
    private fun cargarMenu() {
        menu.add(Producto("Picaditas", 170.0, R.drawable.picaditas))
        menu.add(Producto("Plato Oaxaca", 255.0, R.drawable.plato_oaxaca))
        menu.add(Producto("Promo muffins", 149.0, R.drawable.muffin))
    }

    // 💰 TOTAL CON FORMATO MX
    private fun calcularTotal() {
        val formato = NumberFormat.getCurrencyInstance(Locale("es", "MX"))
        val total = orden.sumOf { it.precio * it.cantidad }
        txtTotal.text = "Total: ${formato.format(total)}"
    }

    // 🔐 PIN SECRETO PARA SALIR DEL KIOSCO
    private fun mostrarPinSalida() {
        val input = EditText(this)
        input.inputType =
            InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_VARIATION_PASSWORD

        AlertDialog.Builder(this)
            .setTitle("PIN de salida")
            .setView(input)
            .setPositiveButton("Salir") { _, _ ->
                if (input.text.toString() == "2580") {
                    stopLockTask()
                    finish()
                } else {
                    Toast.makeText(this, "PIN incorrecto", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    // 📱 DETECTAR TABLET
    private fun esTablet(): Boolean {
        return resources.configuration.smallestScreenWidthDp >= 600
    }
}
