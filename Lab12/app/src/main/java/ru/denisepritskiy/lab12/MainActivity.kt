package ru.denisepritskiy.lab12

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var productAdapter: ProductAdapter
    private val products = mutableListOf(
        Product("Арахисовая паста", 179.99, R.drawable.food01),
        Product("Пицца", 499.99, R.drawable.food02),
        Product("Рис", 119.99, R.drawable.food03),
        Product("Брокколи", 339.99, R.drawable.food04),
        Product("Сыр", 899.99, R.drawable.food05),
        Product("Каша", 49.99, R.drawable.food06),
        Product("Молоко", 89.99, R.drawable.food07),
        Product("Окорок", 399.99,R.drawable.food08),
        Product("Рыба",279.99, R.drawable.food09),
        Product("Грибы",329.99, R.drawable.food10),
        Product("Краб",1199.99,R.drawable.food11)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)

        productAdapter = ProductAdapter(products) { product ->
            val message = if (product.isInCart) "Добавлено: ${product.name}" else "Удалено: ${product.name}"
            showToast(message)
        }
        recyclerView.adapter = productAdapter

    }

    private fun showToast(message: String) {
        val toast = Toast.makeText(this, message, Toast.LENGTH_SHORT)
        toast.show()
    }
}