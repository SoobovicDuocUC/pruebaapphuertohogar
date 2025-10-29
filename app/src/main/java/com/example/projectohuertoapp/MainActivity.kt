package com.example.projectohuertoapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.room.Room
import com.example.projectohuertoapp.data.local.AppDatabase
import com.example.projectohuertoapp.data.repository.UsuarioRepository
import com.example.projectohuertoapp.navigation.AppNavigation
import com.example.projectohuertoapp.ui.theme.HuertoHogarTheme
import com.example.projectohuertoapp.viewmodel.AuthViewModel
// IMPORT FALTANTE AÑADIDO AQUÍ:
import com.example.projectohuertoapp.viewmodel.AuthViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inicializar la base de datos Room
        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "huertohogar_database"
        ).build() // No olvides el .build() aquí

        // Crear el Repositorio y la Factory
        val usuarioRepository = UsuarioRepository(db.usuarioDao())
        val authViewModelFactory = AuthViewModelFactory(usuarioRepository)

        setContent {
            // Usar la factory para crear el ViewModel
            val authViewModel: AuthViewModel = viewModel(factory = authViewModelFactory)

            HuertoHogarTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavigation(authViewModel)
                }
            }
        }
    }
}
