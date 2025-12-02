package com.example.projectohuertoapp.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.projectohuertoapp.ui.screens.CarritoScreen
import com.example.projectohuertoapp.ui.screens.CatalogoScreen
import com.example.projectohuertoapp.ui.screens.HomeScreen
import com.example.projectohuertoapp.ui.screens.LoginScreen
import com.example.projectohuertoapp.ui.screens.QRScannerScreen
import com.example.projectohuertoapp.ui.screens.RegistroScreen
import com.example.projectohuertoapp.viewmodel.AuthViewModel
import com.example.projectohuertoapp.viewmodel.CarritoViewModel
import com.example.projectohuertoapp.viewmodel.CatalogoViewModel
import com.example.projectohuertoapp.viewmodel.HomeViewModel

@Composable
fun AppNavigation(
    authViewModel: AuthViewModel,
    catalogoViewModel: CatalogoViewModel // AHORA SÍ RECIBE ESTE PARÁMETRO
) {
    val navController = rememberNavController()

    // ViewModels que no necesitan Factory especial
    val carritoViewModel: CarritoViewModel = viewModel()
    val homeViewModel: HomeViewModel = viewModel()

    NavHost(navController = navController, startDestination = "home") {

        composable("home") {
            HomeScreen(navController, homeViewModel)
        }
        composable("login") {
            LoginScreen(navController, authViewModel)
        }
        composable("registro") {
            RegistroScreen(navController, authViewModel)
        }
        composable("catalogo") {
            // Usamos el catalogoViewModel que viene desde MainActivity
            CatalogoScreen(navController, catalogoViewModel, carritoViewModel, authViewModel)
        }
        composable("carrito") {
            CarritoScreen(navController, carritoViewModel)
        }
        composable("qr_scanner") {
            QRScannerScreen(navController)
        }
    }
}