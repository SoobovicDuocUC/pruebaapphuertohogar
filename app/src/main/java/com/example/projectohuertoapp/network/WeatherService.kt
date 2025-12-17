package com.example.projectohuertoapp.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

// 1. Modelos de Datos (Lo que responde la API)
data class WeatherResponse(
    val current_weather: CurrentWeather
)

data class CurrentWeather(
    val temperature: Double,
    val weathercode: Int
)

// 2. Interfaz de Retrofit
interface WeatherApiService {
    // Usamos Open-Meteo (Gratis y sin Key)
    // Ejemplo: https://api.open-meteo.com/v1/forecast?latitude=-33.4489&longitude=-70.6693&current_weather=true
    @GET("v1/forecast")
    suspend fun getCurrentWeather(
        @Query("latitude") lat: Double = -33.4489, // Coordenadas de Santiago (puedes cambiarlas)
        @Query("longitude") lon: Double = -70.6693,
        @Query("current_weather") current: Boolean = true
    ): WeatherResponse
}

// 3. Cliente Singleton
object WeatherRetrofitClient {
    private const val BASE_URL = "https://api.open-meteo.com/"

    val instance: WeatherApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(WeatherApiService::class.java)
    }
}