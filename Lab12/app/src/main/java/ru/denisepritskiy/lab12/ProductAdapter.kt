package ru.denisepritskiy.lab12

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

data class Product(
    val name: String,
    val price: Double,
    val imageResId: Int,
    var isInCart: Boolean = false
)

class ProductAdapter(private val products: List<Product>, private val onCartClick: (Product) -> Unit) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_product, parent, false)
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = products[position]
        holder.bind(product)
    }

    override fun getItemCount() = products.size

    inner class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageView: ImageView = itemView.findViewById(R.id.product_image)
        private val nameView: TextView = itemView.findViewById(R.id.product_name)
        private val priceView: TextView = itemView.findViewById(R.id.product_price)
        private val cartButton: ImageButton = itemView.findViewById(R.id.cart_button)

        fun bind(product: Product) {
            imageView.setImageResource(product.imageResId)
            nameView.text = product.name
            priceView.text = "Цена: ${product.price} ₽"

            updateCartButton(product)

            cartButton.setOnClickListener {
                product.isInCart = !product.isInCart
                onCartClick(product)
                updateCartButton(product)
            }
        }

        private fun updateCartButton(product: Product) {
            cartButton.setImageResource(if (product.isInCart) R.drawable.shopping_cart else R.drawable.shopping_cart_add)
        }
    }
}