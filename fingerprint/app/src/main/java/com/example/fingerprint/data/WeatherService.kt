package com.example.fingerprint.data

import com.example.fingerprint.WeatherData
import retrofit2.http.GET
import retrofit2.http.Query

// Interfața care definește metodele pentru a accesa serviciul de vreme
interface WeatherService {

    // Metodă pentru a obține datele despre vreme pentru un anumit oraș
    @GET("weather")
    suspend fun getWeather(
        @Query("q") city: String, // Numele orașului pentru care se dorește informația despre vreme
        @Query("appid") apiKey: String // Cheia API pentru a accesa serviciul de vreme
    ): WeatherData

    // Metodă pentru a obține datele despre vreme pe baza coordonatelor geografice
    @GET("weather")
    suspend fun getWeatherByCoordinates(
        @Query("lat") latitude: Double, // Latitudinea locației pentru care se dorește informația despre vreme
        @Query("lon") longitude: Double, // Longitudinea locației pentru care se dorește informația despre vreme
        @Query("appid") apiKey: String // Cheia API pentru a accesa serviciul de vreme
    ): WeatherData
}
