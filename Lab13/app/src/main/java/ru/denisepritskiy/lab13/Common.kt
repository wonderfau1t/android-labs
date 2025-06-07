package ru.denisepritskiy.lab13

import android.content.Context

data class City(
    val title: String,
    val region: String,
    val postalCode: String,
    val timezone: String,
    val population: String,
    val founded: String,
    val lat: Float,
    val lon: Float
)


object Common {
    val cities = mutableListOf<City>()

    fun initCities(ctx: Context) {
        if (cities.isEmpty()) {
            val lines = ctx.resources.openRawResource(R.raw.cities)
                .bufferedReader().readLines()
            for (i in 1 until lines.size) {
                val parts = lines[i].split(";")
                val city = City(
                    parts[3], parts[2], parts[0], parts[4], parts[7], parts[8],
                    parts[5].toFloat(), parts[6].toFloat()
                )
                cities.add(city)
            }
            cities.sortBy { it.title }
        }
    }
}