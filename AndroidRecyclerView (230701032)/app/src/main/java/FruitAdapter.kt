package com.example.androidrecyclerview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

// ── 1. Extend RecyclerView.Adapter typed to our ViewHolder ─────────── 
class FruitAdapter(
    private val fruits: MutableList<Fruit>,
    private val onItemClick: (Fruit) -> Unit,
    private val onDeleteClick: (Int) -> Unit
) : RecyclerView.Adapter<FruitAdapter.FruitViewHolder>() {

    // ── 2. ViewHolder: holds view references for ONE item row ───────── 
    inner class FruitViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        val tvEmoji:     TextView    = itemView.findViewById(R.id.tvEmoji)
        val tvFruitName: TextView    = itemView.findViewById(R.id.tvFruitName)
        val btnDelete:   ImageButton = itemView.findViewById(R.id.btnDelete)
    }

    // ── 3. onCreateViewHolder: inflate item_fruit.xml once per ViewHolder 
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):
            FruitViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_fruit, parent, false)
        return FruitViewHolder(view)
    }

    // ── 4. onBindViewHolder: fill a recycled ViewHolder with new data ── 
    override fun onBindViewHolder(holder: FruitViewHolder, position: Int) {
        val fruit = fruits[position]

        // Populate the two TextViews 
        holder.tvEmoji.text     = fruit.emoji
        holder.tvFruitName.text = fruit.name
        // Whole-row click → invoke callback with the Fruit object
        holder.itemView.setOnClickListener {
            onItemClick(fruit)
        }

        // Delete button click → invoke callback with current position
        holder.btnDelete.setOnClickListener {
            val pos = holder.adapterPosition
            if (pos != RecyclerView.NO_ID.toInt()) {
                onDeleteClick(pos)
            }
        }
    }

    // ── 5. getItemCount: tells RecyclerView how many rows to draw ──────
    override fun getItemCount(): Int = fruits.size

    // ── 6. Helper: add a new item and notify RecyclerView ─────────────
    fun addFruit(fruit: Fruit) {
        fruits.add(fruit)
        notifyItemInserted(fruits.size - 1)  // Efficient: animates the new row
    }

    // ── 7. Helper: remove an item at a given position ─────────────────
    fun removeFruit(position: Int) {
        if (position in fruits.indices) {
            fruits.removeAt(position)
            notifyItemRemoved(position)      // Efficient: animates the removal
        }
    }
}