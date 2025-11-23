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
import com.example.projectohuertoapp.viewmodel.AuthViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 1. Inicializar la base de datos Room
        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "huertohogar_database"
        )
            .fallbackToDestructiveMigration() // Opcional: Borra la BD si cambias el modelo (útil en desarrollo)
            .build()

        // 2. Crear el Repositorio y la Factory
        val usuarioRepository = UsuarioRepository(db.usuarioDao())
        val authViewModelFactory = AuthViewModelFactory(usuarioRepository)

        setContent {
            HuertoHogarTheme {
                // 3. Crear el ViewModel usando la Factory
                val authViewModel: AuthViewModel = viewModel(factory = authViewModelFactory)

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // 4. Pasar el ViewModel ya creado a la navegación
                    AppNavigation(authViewModel = authViewModel)
                }
            }
        }
    }
}