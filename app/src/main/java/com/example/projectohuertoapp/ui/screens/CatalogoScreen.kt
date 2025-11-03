package com.example.projectohuertoapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Search // Importado para el campo de búsqueda
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.projectohuertoapp.ui.components.ProductoCard
import com.example.projectohuertoapp.viewmodel.AuthViewModel
import com.example.projectohuertoapp.viewmodel.CarritoViewModel
import com.example.projectohuertoapp.viewmodel.CatalogoViewModel // Importado

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CatalogoScreen(
    navController: NavController,
    catalogoViewModel: CatalogoViewModel,
    carritoViewModel: CarritoViewModel,
    authViewModel: AuthViewModel
) {
    // 1. Obtener la lista de productos filtrados del ViewModel
    val productosFiltrados by catalogoViewModel.productosFiltrados.collectAsState()

    // 2. Obtener el texto de búsqueda actual del ViewModel
    val searchText by catalogoViewModel.searchText.collectAsState()

    // Obtener el estado del usuario logueado
    val usuario by authViewModel.usuarioLogueado.collectAsState()
    val isLoggedIn = usuario != null

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("HuertoHogar") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver al inicio"
                        )
                    }
                },
                actions = {
                    // Botones QR y Carrito (solo si el usuario está logueado)
                    if (isLoggedIn) {
                        IconButton(onClick = { navController.navigate("qr_scanner") }) {
                            Icon(
                                imageVector = Icons.Default.QrCodeScanner,
                                contentDescription = "Escanear Código QR"
                            )
                        }
                        IconButton(onClick = { navController.navigate("carrito") }) {
                            Icon(
                                imageVector = Icons.Default.ShoppingCart,
                                contentDescription = "Carrito de Compras"
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        // Usamos Column para apilar el TextField y el LazyColumn
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // --- CAMPO DE BÚSQUEDA ---
            OutlinedTextField(
                value = searchText,
                // Llamamos a la función del ViewModel en cada cambio de texto
                onValueChange = catalogoViewModel::onSearchTextChange,
                label = { Text("Buscar productos...") },
                leadingIcon = {
                    Icon(Icons.Default.Search, contentDescription = "Buscar")
                },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline
                )
            )
            // --- FIN CAMPO DE BÚSQUEDA ---

            // Lista de productos filtrados
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp), // Ajustar el padding vertical
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Usamos la lista de productos filtrados
                items(productosFiltrados) { producto ->
                    ProductoCard(
                        producto = producto,
                        onAddToCart = {
                            if (isLoggedIn) {
                                carritoViewModel.agregarAlCarrito(producto)
                            }
                        },
                        isLoggedIn = isLoggedIn
                    )
                }
            }
        }
    }
}