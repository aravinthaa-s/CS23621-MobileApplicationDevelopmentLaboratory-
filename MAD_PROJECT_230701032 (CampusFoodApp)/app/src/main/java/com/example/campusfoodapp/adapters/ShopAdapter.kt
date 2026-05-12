package com.example.campusfoodapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.campusfoodapp.R
import com.example.campusfoodapp.models.Shop

class ShopAdapter(
    private val shopList: List<Shop>,
    private val isAdmin: Boolean = false,
    private val onEdit: (Shop) -> Unit = {},
    private val onDelete: (Shop) -> Unit = {},
    private val onClick: (Shop) -> Unit
) : RecyclerView.Adapter<ShopAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val shopName: TextView = itemView.findViewById(R.id.tvShopName)
        val editBtn: ImageButton = itemView.findViewById(R.id.btnEditShop)
        val deleteBtn: ImageButton = itemView.findViewById(R.id.btnDeleteShop)
        val goArrow: View = itemView.findViewById(R.id.ivGoToShop)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_shop, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val shop = shopList[position]
        holder.shopName.text = shop.name

        if (isAdmin) {
            holder.editBtn.visibility = View.VISIBLE
            holder.deleteBtn.visibility = View.VISIBLE
            holder.goArrow.visibility = View.GONE
        } else {
            holder.editBtn.visibility = View.GONE
            holder.deleteBtn.visibility = View.GONE
            holder.goArrow.visibility = View.VISIBLE
        }

        holder.editBtn.setOnClickListener { onEdit(shop) }
        holder.deleteBtn.setOnClickListener { onDelete(shop) }
        holder.itemView.setOnClickListener { onClick(shop) }
    }

    override fun getItemCount(): Int = shopList.size
}