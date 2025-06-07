package ru.denisepritskiy.lab14

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CityAdapter(private val cities: List<City>, private val onClick: (Int) -> Unit) :
    RecyclerView.Adapter<CityAdapter.CityViewHolder>() {

    class CityViewHolder(val textView: TextView) : RecyclerView.ViewHolder(textView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CityViewHolder {
        val textView = LayoutInflater.from(parent.context)
            .inflate(android.R.layout.simple_list_item_1, parent, false) as TextView
        return CityViewHolder(textView)
    }

    override fun onBindViewHolder(holder: CityViewHolder, position: Int) {
        holder.textView.text = "${cities[position].title} (${cities[position].region})"
        holder.textView.setOnClickListener { onClick(position) }
    }

    override fun getItemCount() = cities.size
}
