package com.example.projectohuertoapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.* // Importante para remember y mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.projectohuertoapp.ui.components.ProductoCard
import com.example.projectohuertoapp.viewmodel.AuthViewModel
import com.example.projectohuertoapp.viewmodel.CarritoViewModel
import com.example.projectohuertoapp.viewmodel.CatalogoViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CatalogoScreen(
    navController: NavController,
    catalogoViewModel: CatalogoViewModel,
    carritoViewModel: CarritoViewModel,
    authViewModel: AuthViewModel
) {
    // 1. Obtener la lista de productos REAL desde el ViewModel
    // Usamos 'productos' (que sí existe en tu ViewModel) en vez de 'productosFiltrados'
    val productos by catalogoViewModel.productos.collectAsState()
    val isLoading by catalogoViewModel.isLoading.collectAsState()

    // 2. Estado LOCAL para la búsqueda (así no tienes que cambiar el ViewModel)
    var searchText by remember { mutableStateOf("") }

    // 3. Filtramos la lista aquí mismo
    val productosFiltrados = productos.filter {
        it.nombre.contains(searchText, ignoreCase = true)
    }

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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // --- CAMPO DE BÚSQUEDA ---
            OutlinedTextField(
                value = searchText,
                // Actualizamos el estado local
                onValueChange = { searchText = it },
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

            if (isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else {
                // Lista de productos filtrados
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(productosFiltrados) { producto ->
                        // CORRECCIÓN: Usamos los parámetros correctos de ProductoCard
                        ProductoCard(
                            producto = producto,
                            onAgregarClick = {
                                if (isLoggedIn) {
                                    // CORRECCIÓN: Usamos 'agregarAlCarrito'
                                    carritoViewModel.agregarAlCarrito(producto)
                                } else {
                                    // Opcional: Navegar al login si intenta agregar sin cuenta
                                    navController.navigate("login")
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}