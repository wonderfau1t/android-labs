package ru.denisepritskiy.lab14

import CityListFragment
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit


class MainActivity : AppCompatActivity(), CityListFragment.OnCitySelectedListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Common.initCities(this)

        if (savedInstanceState == null) {
            supportFragmentManager.commit() {
                replace(R.id.fragmentContainer, CityListFragment())
            }
        }
    }

    override fun onCitySelected(index: Int) {
        val isDualPane = findViewById<View?>(R.id.cityDetailContainer) != null

        if (isDualPane) {
            supportFragmentManager.commit {
                replace(R.id.cityDetailContainer, CityDetailFragment.newInstance(index))
            }
        } else {
            supportFragmentManager.commit {
                replace(R.id.fragmentContainer, CityDetailFragment.newInstance(index))
                addToBackStack(null)
            }
        }
    }
}