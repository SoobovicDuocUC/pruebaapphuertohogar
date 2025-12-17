package com.example.projectohuertoapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.projectohuertoapp.network.WeatherRetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {

    // Estado para el Clima
    private val _temperatura = MutableStateFlow<String>("...")
    val temperatura: StateFlow<String> = _temperatura.asStateFlow()

    private val _climaDescripcion = MutableStateFlow<String>("Cargando clima...")
    val climaDescripcion: StateFlow<String> = _climaDescripcion.asStateFlow()

    init {
        obtenerClima()
    }

    private fun obtenerClima() {
        viewModelScope.launch {
            try {
                // Llamada a la API Externa (Open-Meteo)
                val respuesta = WeatherRetrofitClient.instance.getCurrentWeather()
                val temp = respuesta.current_weather.temperature
                val codigo = respuesta.current_weather.weathercode

                _temperatura.value = "$temp°C"
                _climaDescripcion.value = interpretarCodigoClima(codigo)

            } catch (e: Exception) {
                _temperatura.value = "--"
                _climaDescripcion.value = "Sin conexión"
            }
        }
    }

    // Función auxiliar para traducir el código numérico a texto
    private fun interpretarCodigoClima(code: Int): String {
        return when (code) {
            0 -> "Cielo despejado \u2600\uFE0F" // Sol
            1, 2, 3 -> "Nublado \u2601\uFE0F"   // Nube
            45, 48 -> "Neblina \uD83C\uDF2B\uFE0F"
            51, 53, 55 -> "Llovizna \uD83C\uDF27\uFE0F"
            61, 63, 65 -> "Lluvia \uD83C\uDF27\uFE0F"
            else -> "Clima variable"
        }
    }

    // Funciones de navegación
    fun onLoginIconClicked(navController: NavController) {
        navController.navigate("login")
    }

    fun onCatalogButtonClicked(navController: NavController) {
        navController.navigate("catalogo")
    }
}