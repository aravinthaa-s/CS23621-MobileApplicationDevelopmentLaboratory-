package com.example.campusfoodapp.adapters

import android.view.*
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.campusfoodapp.R
import com.example.campusfoodapp.models.MenuItem
import java.util.Locale

class CartAdapter(private val cartList: List<MenuItem>) :
    RecyclerView.Adapter<CartAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.tvCartName)
        val qty: TextView = itemView.findViewById(R.id.tvCartQty)
        val price: TextView = itemView.findViewById(R.id.tvCartPrice)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_cart, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = cartList[position]
        holder.name.text = item.name
        holder.qty.text = "Qty: ${item.quantity}"

        // Parse price and calculate total
        // We strip non-numeric characters except for the decimal point
        val cleanPriceString = item.price.replace(Regex("[^\\d.]"), "")
        val unitPrice = cleanPriceString.toDoubleOrNull() ?: 0.0
        
        if (unitPrice > 0) {
            val totalItemPrice = unitPrice * item.quantity
            holder.price.text = String.format(Locale.getDefault(), "₹%.2f", totalItemPrice)
        } else {
            // If parsing fails or price is 0, show the raw price string from the model
            holder.price.text = if (item.price.contains("₹")) item.price else "₹${item.price}"
        }
    }

    override fun getItemCount(): Int = cartList.size
}
