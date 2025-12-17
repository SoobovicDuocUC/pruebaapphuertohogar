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
import com.example.projectohuertoapp.data.repository.NetworkProductoRepository
import com.example.projectohuertoapp.data.repository.UsuarioRepository
import com.example.projectohuertoapp.navigation.AppNavigation
import com.example.projectohuertoapp.ui.theme.HuertoHogarTheme
import com.example.projectohuertoapp.viewmodel.AuthViewModel
import com.example.projectohuertoapp.viewmodel.AuthViewModelFactory
import com.example.projectohuertoapp.viewmodel.CatalogoViewModel
import com.example.projectohuertoapp.viewmodel.CatalogoViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 1. Inicializar la base de datos Room (La dejamos por si acaso, aunque Auth ahora va por API)
        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "huertohogar_database"
        )
            .fallbackToDestructiveMigration()
            .build()

        // 2. Crear Repositorios
        val usuarioRepository = UsuarioRepository()

        val productoRepository = NetworkProductoRepository()

        // 3. Crear Factories
        val authViewModelFactory = AuthViewModelFactory(usuarioRepository)
        val catalogoViewModelFactory = CatalogoViewModelFactory(productoRepository)

        setContent {
            // 4. Crear ViewModels con las factories
            val authViewModel: AuthViewModel = viewModel(factory = authViewModelFactory)
            val catalogoViewModel: CatalogoViewModel = viewModel(factory = catalogoViewModelFactory)

            HuertoHogarTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // 5. Pasar ViewModels a la navegación
                    AppNavigation(
                        authViewModel = authViewModel,
                        catalogoViewModel = catalogoViewModel
                    )
                }
            }
        }
    }
}