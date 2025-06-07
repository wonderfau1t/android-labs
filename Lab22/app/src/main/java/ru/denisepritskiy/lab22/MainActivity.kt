package ru.denisepritskiy.lab22

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import kotlin.math.acos
import kotlin.math.cos
import kotlin.math.sin

class MainActivity : AppCompatActivity() {
    private lateinit var locationManager: LocationManager
    private lateinit var statusText: TextView
    private lateinit var distanceText: TextView
    private lateinit var generateButton: Button
    private lateinit var settingsButton: Button

    private var targetLat = 0.0
    private var targetLon = 0.0

    private val locationListener = LocationListener { location ->
        val distance = calculateDistance(
            location.latitude, location.longitude,
            targetLat, targetLon
        )
        distanceText.text = "Расстояние: ${"%.2f".format(distance)} м"
        if (distance <= 100) {
            statusText.text = "Ура, точка найдена!"
            statusText.setTextColor(getColor(R.color.green))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        locationManager = getSystemService(LOCATION_SERVICE) as LocationManager

        statusText = findViewById(R.id.statusText)
        distanceText = findViewById(R.id.distanceText)
        generateButton = findViewById(R.id.generateButton)
        settingsButton = findViewById(R.id.settingsButton)

        generateButton.setOnClickListener {
            generateTargetPoint()
        }

        settingsButton.setOnClickListener {
            val intent = Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
            intent.data = android.net.Uri.parse("package:$packageName")
            startActivity(intent)
        }

        checkPermissions()
    }

    private fun generateTargetPoint() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                100
            )
            return
        }

        val lastLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            ?: locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)

        if (lastLocation != null) {
            val delta = 0.02 // ~2.5 км
            targetLat = lastLocation.latitude + (-delta..delta).random()
            targetLon = lastLocation.longitude + (-delta..delta).random()
            statusText.text = "Точка загадана, ищите!"
            statusText.setTextColor(getColor(R.color.blue))
        } else {
            Toast.makeText(this, "Местоположение недоступно", Toast.LENGTH_SHORT).show()
        }
    }


    private fun ClosedFloatingPointRange<Double>.random() =
        start + (endInclusive - start) * Math.random()

    private fun calculateDistance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        val d = 6371000.0
        val phiA = Math.toRadians(lat1)
        val phiB = Math.toRadians(lat2)
        val lambdaA = Math.toRadians(lon1)
        val lambdaB = Math.toRadians(lon2)

        return d * acos(
            sin(phiA) * sin(phiB) +
                    cos(phiA) * cos(phiB) * cos(lambdaA - lambdaB)
        )
    }

    private fun checkPermissions(): Boolean {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                100
            )
            return false
        }

        locationManager.requestLocationUpdates(
            LocationManager.GPS_PROVIDER, 1000, 10f, locationListener
        )
        locationManager.requestLocationUpdates(
            LocationManager.NETWORK_PROVIDER, 1000, 10f, locationListener
        )
        return true
    }

    override fun onPause() {
        super.onPause()
        locationManager.removeUpdates(locationListener)
    }

    override fun onResume() {
        super.onResume()
        checkPermissions()
    }
}
