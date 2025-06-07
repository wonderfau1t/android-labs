package ru.denisepritskiy.lab13

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private lateinit var cityInfo: TextView
    private lateinit var btnSelectCity: Button
    private lateinit var btnShowMap: Button
    private var selectedCity: City? = null

    private val cityResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val cityIndex = result.data?.getIntExtra("cityIndex", -1) ?: -1
                if (cityIndex != -1) {
                    selectedCity = Common.cities[cityIndex]
                    updateCityInfo()
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        cityInfo = findViewById(R.id.cityInfo)
        btnSelectCity = findViewById(R.id.btnSelectCity)
        btnShowMap = findViewById(R.id.btnShowMap)

        Common.initCities(this)

        btnSelectCity.setOnClickListener {
            val intent = Intent(this, CityListActivity::class.java)
            cityResultLauncher.launch(intent)
        }

        btnShowMap.setOnClickListener {
            selectedCity?.let {
                val uri = Uri.parse("geo:${it.lat},${it.lon}?q=${Uri.encode(it.title)}")
                val mapIntent = Intent(Intent.ACTION_VIEW, uri)
                startActivity(mapIntent)
            }
        }
    }

    private fun updateCityInfo() {
        selectedCity?.let {
            cityInfo.text = "Город: ${it.title}\nРегион: ${it.region}\nПочтовый индекс: ${it.postalCode}\nЧасовой пояс: ${it.timezone}\nНаселение: ${it.population}\nОснован в: ${it.founded} году"
        }
    }
}
