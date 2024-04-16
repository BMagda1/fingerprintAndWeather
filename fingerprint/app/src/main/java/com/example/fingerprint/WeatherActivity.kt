package com.example.fingerprint

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.bumptech.glide.Glide
import com.example.fingerprint.data.WeatherService
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.*

// Clasele de date pentru a reprezenta informațiile despre vreme
data class WeatherData(
    val name: String,
    val main: Main,
    val weather: List<Weather>
)

data class Main(
    val temp: Double
)

data class Weather(
    val description: String,
    val icon: String
)

class WeatherActivity : AppCompatActivity() {

    // Serviciul Retrofit pentru a prelua datele despre vreme
    private lateinit var weatherService: WeatherService

    // Formatul datei pentru afișarea timpului ultimei actualizări
    private lateinit var dateFormat: SimpleDateFormat

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weather)

        // Inițializează serviciul Retrofit
        weatherService = Retrofit.Builder()
            .baseUrl("https://api.openweathermap.org/data/2.5/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(WeatherService::class.java)

        // Inițializează formatul datei cu fusul orar al României
        dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        dateFormat.timeZone = TimeZone.getTimeZone("Europe/Bucharest")

        // Actualizează inițial informațiile despre vreme
        updateWeather()

        // Setează ascultătorul pentru funcționalitatea de trage pentru a reîmprospăta
        val swipeRefreshLayout = findViewById<SwipeRefreshLayout>(R.id.swipeRefreshLayout)
        swipeRefreshLayout.setOnRefreshListener {
            updateWeather()
            swipeRefreshLayout.isRefreshing = false // Ascunde indicatorul de reîmprospătare
        }

        // Setează ascultătorul pentru FloatingActionButton pentru a lansa MapActivity
        val fab = findViewById<FloatingActionButton>(R.id.fab)
        fab.setOnClickListener {
            // Lansează MapActivity pentru a selecta o locație
            val intent = Intent(this, MapActivity::class.java)
            mapActivityLauncher.launch(intent)
        }
    }

    // Ascultătorul rezultatului pentru primirea datelor de la MapActivity
    private val mapActivityLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data
            val latitude = data?.getDoubleExtra("latitude", 0.0)
            val longitude = data?.getDoubleExtra("longitude", 0.0)
            if (latitude != null && longitude != null) {
                // Preia datele despre vreme pentru coordonatele selectate
                requestWeatherForCoordinates(latitude, longitude)
            }
        }
    }

    // Funcție pentru a prelua și actualiza informațiile despre vreme
    private fun updateWeather() {
        lifecycleScope.launch {
            try {
                // Preia datele despre vreme de la rețea
                val weatherData = withContext(Dispatchers.IO) {
                    weatherService.getWeather("Craiovita", "e2ee2359f600c994cb41aa91b3c655f3")
                }
                // Actualizează UI-ul cu datele despre vreme preluate
                updateUI(weatherData)
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(this@WeatherActivity, "Eșuat la preluarea datelor despre vreme", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Funcție pentru a actualiza UI-ul cu informațiile despre vreme
    private fun updateUI(weatherData: WeatherData) {
        // Actualizează numele orașului
        findViewById<TextView>(R.id.txtCity).text = weatherData.name
        // Converteste temperatura din Kelvin în Celsius și afișează-o
        val tempCelsius = (weatherData.main.temp - 273.15).toInt()
        findViewById<TextView>(R.id.txtTemperature).text = "${tempCelsius} °C"
        // Afișează descrierea vremii
        findViewById<TextView>(R.id.txtDetails).text = weatherData.weather[0].description

        // Obține data și ora curentă în fusul orar al României
        val currentDateTime = dateFormat.format(Date())
        findViewById<TextView>(R.id.txtUpdated).text = currentDateTime

        // Încarcă iconul vremii folosind biblioteca Glide
        val iconUrl = "https://openweathermap.org/img/w/${weatherData.weather[0].icon}.png"
        Glide.with(this@WeatherActivity)
            .load(iconUrl)
            .into(findViewById(R.id.imgWeatherIcon))
    }

    // Funcție pentru a prelua datele despre vreme pentru coordonatele date
    private fun requestWeatherForCoordinates(latitude: Double, longitude: Double) {
        lifecycleScope.launch {
            try {
                // Preia datele despre vreme pentru coordonatele date de la rețea
                val weatherData = withContext(Dispatchers.IO) {
                    weatherService.getWeatherByCoordinates(
                        latitude,
                        longitude,
                        "e2ee2359f600c994cb41aa91b3c655f3"
                    )
                }
                // Actualizează UI-ul cu datele despre vreme preluate
                updateUI(weatherData)
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(
                    this@WeatherActivity,
                    "Eșuat la preluarea datelor despre vreme",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}
