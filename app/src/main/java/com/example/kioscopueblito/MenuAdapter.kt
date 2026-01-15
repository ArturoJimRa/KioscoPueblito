package com.example.kioscopueblito

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MenuAdapter(
    private val menu: List<Producto>,
    private val onClick: (Producto) -> Unit
) : RecyclerView.Adapter<MenuAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val img: ImageView = itemView.findViewById(R.id.imgMenu)
        val nombre: TextView = itemView.findViewById(R.id.txtNombreMenu)
        val precio: TextView = itemView.findViewById(R.id.txtPrecioMenu)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_menu, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val producto = menu[position]

        holder.img.setImageResource(producto.imagen)
        holder.nombre.text = producto.nombre
        holder.precio.text = "$${producto.precio}"

        holder.itemView.setOnClickListener {
            onClick(producto)
        }
    }

    override fun getItemCount(): Int = menu.size
}
