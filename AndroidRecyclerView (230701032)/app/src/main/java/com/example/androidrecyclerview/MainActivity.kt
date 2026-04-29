package com.example.androidrecyclerview

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    // ── 1. Declare UI references ──────────────────────────────────────
    private lateinit var recyclerView:  RecyclerView
    private lateinit var etFruitName:   EditText
    private lateinit var btnAdd:         Button
    private lateinit var tvItemCount:    TextView

    private lateinit var adapter: FruitAdapter

    // ── 2. Seed data — the initial list ──────────────────────────────
    private val fruitList: MutableList<Fruit> = mutableListOf(
        Fruit("Apple",      "🍎"),
        Fruit("Banana",     "🍎"),
        Fruit("Cherry",     "🍎"),
        Fruit("Mango",      "🍎"),
        Fruit("Pineapple",  "🍎")
    )

    // ── 3. onCreate ───────────────────────────────────────────────────
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 3a. Bind views
        recyclerView = findViewById(R.id.recyclerView)
        etFruitName  = findViewById(R.id.etFruitName)
        btnAdd       = findViewById(R.id.btnAdd)
        tvItemCount  = findViewById(R.id.tvItemCount)

// 3b. Create the Adapter with lambda callbacks
        adapter = FruitAdapter(
            fruits       = fruitList,
            onItemClick  = { fruit -> onFruitClicked(fruit) },
            onDeleteClick = { position -> onDeleteClicked(position) }
        )

        // 3c. Configure RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter       = adapter

        // 3d. Initial count label
        updateItemCount()

        // 3e. Add button listener
        btnAdd.setOnClickListener { addFruit() }
    }

    // ── 4. Handle whole-row tap ───────────────────────────────────────
    private fun onFruitClicked(fruit: Fruit) {
        Toast.makeText(
            this,
            "${fruit.emoji} You tapped: ${fruit.name}",
            Toast.LENGTH_SHORT
        ).show()
    }

    // ── 5. Handle delete button tap ───────────────────────────────────
    private fun onDeleteClicked(position: Int) {
        val removed = fruitList[position]
        adapter.removeFruit(position)
        updateItemCount()
        Toast.makeText(
            this,
            "Removed: ${removed.name}",
            Toast.LENGTH_SHORT
        ).show()
    }

    // ── 6. Add a new fruit from the EditText ──────────────────────────
    private fun addFruit() {
        val name = etFruitName.text.toString().trim()

        if (name.isEmpty()) {
            etFruitName.error = "Please enter a fruit name"
            return
        }

        // Add the fruit to the list and notify the adapter
        adapter.addFruit(Fruit(name, "🍎"))
        updateItemCount()

        // Clear input and scroll to newly added item
        etFruitName.setText("")
        recyclerView.scrollToPosition(fruitList.size - 1)
        Toast.makeText(this, "Added: $name", Toast.LENGTH_SHORT).show()
    }
    // ── 7. Update the item count label ────────────────────────────────
    private fun updateItemCount() {
        val count = fruitList.size
        tvItemCount.text = "$count ${if (count == 1) "item" else "items"}"
    }
}