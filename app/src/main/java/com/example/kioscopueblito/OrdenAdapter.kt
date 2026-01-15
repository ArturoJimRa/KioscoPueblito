package com.example.kioscopueblito

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView

class OrdenAdapter(
    private val lista: MutableList<Producto>,
    private val onUpdate: () -> Unit
) : RecyclerView.Adapter<OrdenAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val img = view.findViewById<ImageView>(R.id.imgProducto)
        val nombre = view.findViewById<TextView>(R.id.txtNombre)
        val cantidad = view.findViewById<TextView>(R.id.txtCantidad)
        val btnMas = view.findViewById<Button>(R.id.btnMas)
        val btnMenos = view.findViewById<Button>(R.id.btnMenos)
        val btnEliminar = view.findViewById<Button>(R.id.btnEliminar)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_orden, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val producto = lista[position]

        holder.nombre.text = producto.nombre
        holder.cantidad.text = producto.cantidad.toString()
        holder.img.setImageResource(producto.imagen)

        holder.btnMas.setOnClickListener {
            producto.cantidad++
            notifyItemChanged(position)
            onUpdate()
        }

        holder.btnMenos.setOnClickListener {
            if (producto.cantidad > 1) {
                producto.cantidad--
                notifyItemChanged(position)
            }
            onUpdate()
        }

        holder.btnEliminar.setOnClickListener {
            lista.removeAt(position)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, lista.size)
            onUpdate()
        }
    }

    override fun getItemCount(): Int = lista.size
}
