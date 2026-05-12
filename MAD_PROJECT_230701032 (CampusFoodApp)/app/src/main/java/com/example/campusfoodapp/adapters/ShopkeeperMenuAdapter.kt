package com.example.campusfoodapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.campusfoodapp.R
import com.example.campusfoodapp.models.MenuItem

class ShopkeeperMenuAdapter(
    private val itemList: List<MenuItem>,
    private val onEdit: (MenuItem) -> Unit,
    private val onDelete: (MenuItem) -> Unit
) : RecyclerView.Adapter<ShopkeeperMenuAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvName: TextView = itemView.findViewById(R.id.tvMenuItemName)
        val tvDescription: TextView = itemView.findViewById(R.id.tvMenuItemDescription)
        val tvPrice: TextView = itemView.findViewById(R.id.tvMenuItemPrice)
        val btnEdit: ImageButton = itemView.findViewById(R.id.btnEditMenuItem)
        val btnDelete: ImageButton = itemView.findViewById(R.id.btnDeleteMenuItem)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_menu_shopkeeper, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = itemList[position]
        holder.tvName.text = item.name
        holder.tvDescription.text = item.description
        holder.tvPrice.text = "Rs. ${item.price}"

        holder.btnEdit.setOnClickListener { onEdit(item) }
        holder.btnDelete.setOnClickListener { onDelete(item) }
    }

    override fun getItemCount(): Int = itemList.size
}
