package ru.denisepritskiy.lab18

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory
import java.net.URL
import java.nio.charset.Charset
import ru.denisepritskiy.lab18.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val scope = CoroutineScope(Dispatchers.IO)
    private val currencies = mutableListOf<Currency>()
    private lateinit var adapter: CurrencyAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = CurrencyAdapter(currencies)
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter

        binding.fetchButton.setOnClickListener {
            binding.progressBar.visibility = View.VISIBLE
            binding.fetchButton.isEnabled = false
            fetchCurrencyRates()
        }
    }

    private fun fetchCurrencyRates() {
        scope.launch {
            try {
                val xml = URL("https://www.cbr.ru/scripts/XML_daily.asp")
                    .readText(Charset.forName("Windows-1251"))
                val parsedCurrencies = parseXml(xml)

                withContext(Dispatchers.Main) {
                    currencies.clear()
                    currencies.addAll(parsedCurrencies)
                    adapter.notifyDataSetChanged()
                    binding.progressBar.visibility = View.GONE
                    binding.fetchButton.isEnabled = true
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(applicationContext, "Ошибка загрузки: ${e.message}", Toast.LENGTH_LONG).show()
                    binding.progressBar.visibility = View.GONE
                    binding.fetchButton.isEnabled = true
                }
            }
        }
    }

    private fun parseXml(xml: String): List<Currency> {
        val parser = XmlPullParserFactory.newInstance().newPullParser()
        parser.setInput(xml.reader())
        val currencies = mutableListOf<Currency>()
        var currentName: String? = null
        var currentValue: String? = null
        var currentTag: String? = null

        while (parser.eventType != XmlPullParser.END_DOCUMENT) {
            when (parser.eventType) {
                XmlPullParser.START_TAG -> {
                    currentTag = parser.name
                    if (currentTag == "Valute") {
                        currentName = null
                        currentValue = null
                    }
                }
                XmlPullParser.TEXT -> {
                    // Игнорируем текст, который состоит только из пробелов или переносов строк
                    val text = parser.text.trim()
                    if (text.isNotEmpty()) {
                        when (currentTag) {
                            "Name" -> currentName = text
                            "Value" -> currentValue = text
                        }
                    }
                }
                XmlPullParser.END_TAG -> {
                    if (parser.name == "Valute" && currentName != null && currentValue != null) {
                        currencies.add(Currency(currentName!!, currentValue!!))
                        Log.d("CurrencyApp", "Валюта добавлена: $currentName -> $currentValue")
                    }
                    currentTag = null // Сбрасываем текущий тег после его закрытия
                }
            }
            parser.next()
        }
        Log.d("CurrencyApp", "Парсинг завершён, валют найдено: ${currencies.size}")
        return currencies
    }
}