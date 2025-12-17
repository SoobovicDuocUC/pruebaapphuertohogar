package com.example.projectohuertoapp.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.WbSunny // Icono de Sol
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.projectohuertoapp.R
import com.example.projectohuertoapp.viewmodel.HomeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeViewModel
) {
    // 1. Observamos los datos del clima
    val temperatura by viewModel.temperatura.collectAsState()
    val descripcion by viewModel.climaDescripcion.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("HuertoHogar") },
                actions = {
                    IconButton(onClick = { viewModel.onLoginIconClicked(navController) }) {
                        Icon(
                            imageVector = Icons.Filled.AccountCircle,
                            contentDescription = "Iniciar Sesión",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.85f),
                    titleContentColor = Color.White,
                    actionIconContentColor = Color.White
                )
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Fondo de pantalla
            Image(
                painter = painterResource(id = R.drawable.frutasbackground),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            // Capa oscura para leer mejor el texto
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.4f))
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "BIENVENIDO A HUERTO HOGAR",
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 24.dp)
                )

                // --- NUEVO: WIDGET DE CLIMA (API EXTERNA) ---
                Card(
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .padding(bottom = 32.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f)
                    ),
                    elevation = CardDefaults.cardElevation(8.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        // Icono
                        Icon(
                            imageVector = Icons.Default.WbSunny,
                            contentDescription = "Clima",
                            tint = Color(0xFFFFB300), // Color amarillo anaranjado
                            modifier = Modifier.size(40.dp)
                        )
                        Spacer(modifier = Modifier.width(16.dp))

                        // Textos
                        Column {
                            Text(
                                text = "Clima en tu zona",
                                style = MaterialTheme.typography.labelMedium,
                                color = Color.Gray
                            )
                            Text(
                                text = temperatura,
                                style = MaterialTheme.typography.headlineMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = descripcion,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }
                // -------------------------------------------

                Button(
                    onClick = { viewModel.onCatalogButtonClicked(navController) },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondary,
                        contentColor = Color.White
                    ),
                    modifier = Modifier
                        .fillMaxWidth(0.6f)
                        .height(50.dp)
                ) {
                    Text(
                        text = "VER CATÁLOGO",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}