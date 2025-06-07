package ru.denisepritskiy.lab15

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    private val items = mutableListOf<ShoppingItem>()
    private lateinit var adapter: ShoppingAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var fabAdd: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Инициализация view через findViewById
        recyclerView = findViewById(R.id.list)
        fabAdd = findViewById(R.id.fab_add)

        adapter = ShoppingAdapter(items) { position ->
            showEditDialog(position)
        }
        recyclerView.adapter = adapter

        fabAdd.setOnClickListener {
            showAddDialog()
        }

        setupSwipeToDelete()
    }

    private fun showAddDialog() {
        val dialog = ShoppingDialog.newInstance()
        dialog.setOnSaveListener { newItem ->
            items.add(newItem)
            adapter.notifyItemInserted(items.size - 1)
        }
        dialog.show(supportFragmentManager, "add_dialog")
    }

    private fun showEditDialog(position: Int) {
        val dialog = ShoppingDialog.newInstance(items[position])
        dialog.setOnSaveListener { updatedItem ->
            items[position] = updatedItem
            adapter.notifyItemChanged(position)
        }
        dialog.show(supportFragmentManager, "edit_dialog")
    }

    private fun setupSwipeToDelete() {
        val swipeCallback = object : ItemTouchHelper.SimpleCallback(
            0,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean = false

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                items.removeAt(position)
                adapter.notifyItemRemoved(position)
            }
        }
        val swipeHelper = ItemTouchHelper(swipeCallback)
        swipeHelper.attachToRecyclerView(recyclerView)
    }
}