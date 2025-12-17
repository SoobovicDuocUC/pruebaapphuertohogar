package com.example.projectohuertoapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.projectohuertoapp.viewmodel.CarritoViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CarritoScreen(navController: NavController, viewModel: CarritoViewModel) {
    val items by viewModel.items.collectAsState()

    // Calculamos el total
    val total = items.sumOf { it.producto.precio * it.cantidad }

    val cantTotal = items.sumOf { it.cantidad }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mi Carrito") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver al catálogo"
                        )
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            if (items.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Tu carrito está vacío", style = MaterialTheme.typography.titleMedium)
                }
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    contentPadding = PaddingValues(vertical = 8.dp)
                ) {
                    items(items) { item ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Nombre del producto
                            Text(
                                text = item.producto.nombre,
                                modifier = Modifier.weight(1f),
                                fontWeight = FontWeight.Bold
                            )

                            // Controles de cantidad
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                IconButton(
                                    onClick = { viewModel.decrementarCantidad(item.producto) },
                                    enabled = item.cantidad > 0
                                ) {
                                    Icon(Icons.Filled.Remove, contentDescription = "Disminuir")
                                }

                                Text(
                                    text = "${item.cantidad}x",
                                    modifier = Modifier.width(30.dp),
                                    style = MaterialTheme.typography.bodyLarge
                                )

                                IconButton(
                                    onClick = { viewModel.agregarAlCarrito(item.producto) }
                                ) {
                                    Icon(Icons.Filled.Add, contentDescription = "Aumentar")
                                }
                            }

                            Spacer(modifier = Modifier.width(8.dp))

                            // CORRECCIÓN AQUÍ: Convertimos a Double para evitar el crash
                            Text(
                                text = "$${"%,.0f".format((item.producto.precio * item.cantidad).toDouble())}",
                                fontWeight = FontWeight.Bold
                            )

                            Spacer(modifier = Modifier.width(4.dp))

                            IconButton(
                                onClick = { viewModel.eliminarDelCarrito(item.producto) },
                                modifier = Modifier.size(36.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Delete,
                                    contentDescription = "Eliminar producto",
                                    tint = MaterialTheme.colorScheme.error
                                )
                            }
                        }
                        // Usamos HorizontalDivider (Material 3) en vez de Divider
                        HorizontalDivider()
                    }
                }

                HorizontalDivider(thickness = 2.dp)

                // Sección de Total
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Total:", style = MaterialTheme.typography.headlineSmall)
                    // CORRECCIÓN AQUÍ TAMBIÉN: Convertimos a Double
                    Text(
                        text = "$${"%,.0f".format(total.toDouble())}",
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                    Text( "Cantidad de Productos: $cantTotal",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.secondary,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
                    )

                Button(
                    onClick = { /* Lógica de pago */ },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                        .height(50.dp)
                ) {
                    Text("Proceder al Pago")
                }
            }
        }
    }
}