package com.example.projectohuertoapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
// Eliminar importaciones de Room
// import androidx.room.Room
// import com.example.projectohuertoapp.data.local.AppDatabase

// Nuevas importaciones de Retrofit y DTOs
//import com.example.projectohuertoapp.data.remote.RetrofitClient // <-- Clase para inicializar Retrofit

import com.example.projectohuertoapp.navigation.AppNavigation
import com.example.projectohuertoapp.network.RetrofitClient
import com.example.projectohuertoapp.network.repository.UsuarioRepository
import com.example.projectohuertoapp.ui.theme.HuertoHogarTheme
import com.example.projectohuertoapp.viewmodel.AuthViewModel
import com.example.projectohuertoapp.viewmodel.AuthViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 1. ELIMINAR: Inicialización de la base de datos Room
        /*
        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "huertohogar_database"
        )
            .fallbackToDestructiveMigration()
            .build()
        */

        // --- NUEVA INICIALIZACIÓN DE RED (Retrofit) ---
        // 1. Inicializar el cliente de Retrofit para obtener la interfaz de la API
        val usuarioApi = RetrofitClient.instance // Asumiendo que RetrofitClient es un Singleton que provee UsuarioApi

        // 2. Crear el Repositorio inyectando la API (en lugar del DAO de Room)
        // Antes: val usuarioRepository = UsuarioRepository(db.usuarioDao())
        val usuarioRepository = UsuarioRepository(usuarioApi)

        // 3. Crear el ViewModel Factory
        val authViewModelFactory = AuthViewModelFactory(usuarioRepository)

        setContent {
            HuertoHogarTheme {
                // 4. Crear el ViewModel usando la Factory
                val authViewModel: AuthViewModel = viewModel(factory = authViewModelFactory)

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // 5. Pasar el ViewModel a la navegación
                    AppNavigation(authViewModel = authViewModel)
                }
            }
        }
    }
}