package ru.denisepritskiy.lab14

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment

class CityDetailFragment : Fragment() {
    companion object {
        private const val ARG_INDEX = "cityIndex"
        fun newInstance(index: Int): CityDetailFragment {
            val fragment = CityDetailFragment()
            fragment.arguments = Bundle().apply { putInt(ARG_INDEX, index) }
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_city_detail, container, false)
        val index = arguments?.getInt(ARG_INDEX) ?: return view
        val city = Common.cities[index]

        view.findViewById<TextView>(R.id.cityInfo).text=
            "Город: ${city.title}\nРегион: ${city.region}\nПочтовый индекс: ${city.postalCode}\nЧасовой пояс: ${city.timezone}\nНаселение: ${city.population}\nОснован в: ${city.founded} году"

        view.findViewById<Button>(R.id.btnShowMap).setOnClickListener {
            val uri = Uri.parse("geo:${city.lat},${city.lon}?q=${Uri.encode(city.title)}")
            val intent = Intent(Intent.ACTION_VIEW, uri)
            startActivity(intent)
        }
        return view
    }
}
