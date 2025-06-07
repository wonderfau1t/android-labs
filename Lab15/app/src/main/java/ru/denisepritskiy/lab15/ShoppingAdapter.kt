package ru.denisepritskiy.lab15

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ShoppingAdapter(
    private val items: MutableList<ShoppingItem>,
    private val onItemClick: (Int) -> Unit,
) : RecyclerView.Adapter<ShoppingAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_shopping, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int = items.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvName: TextView = itemView.findViewById(R.id.tv_name)
        private val tvQuantity: TextView = itemView.findViewById(R.id.tv_quantity)
        init {
            itemView.setOnClickListener { onItemClick(adapterPosition) }
        }

        fun bind(item: ShoppingItem) {

            tvName.text = item.name
            tvQuantity.text = "Количество: ${item.quantity}"
        }
    }
}