package com.example.projectohuertoapp.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.projectohuertoapp.R
import com.example.projectohuertoapp.data.local.entity.Usuario
import com.example.projectohuertoapp.viewmodel.AuthViewModel

@Composable
fun RegistroScreen(
    navController: NavController,
    authViewModel: AuthViewModel
) {
    val usuarioLogueado by authViewModel.usuarioLogueado.collectAsState()
    val errorServidor by authViewModel.error.collectAsState()

    // Variable local para errores de validación (ej: contraseñas no coinciden)
    var errorLocal by remember { mutableStateOf<String?>(null) }

    var nombre by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    LaunchedEffect(usuarioLogueado) {
        if (usuarioLogueado != null) {
            navController.navigate("home") {
                popUpTo("registro") { inclusive = true }
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.frutasbackground),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Box(modifier = Modifier.fillMaxSize().padding(16.dp), contentAlignment = Alignment.Center) {
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.95f))
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Crear Cuenta", style = MaterialTheme.typography.headlineMedium)

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = nombre,
                        onValueChange = { nombre = it },
                        label = { Text("Nombre") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("Correo") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text("Contraseña") },
                        visualTransformation = PasswordVisualTransformation(),
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = confirmPassword,
                        onValueChange = { confirmPassword = it },
                        label = { Text("Confirmar Contraseña") },
                        visualTransformation = PasswordVisualTransformation(),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Mostrar error (Local o de Servidor) con ÍCONO
                    val mensajeError = errorLocal ?: errorServidor

                    if (mensajeError != null) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Warning,
                                contentDescription = "Error",
                                tint = MaterialTheme.colorScheme.error
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = mensajeError,
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                    }

                    Button(
                        onClick = {
                            if (password == confirmPassword) {
                                errorLocal = null // Limpiar error local
                                val nuevoUsuario = Usuario(nombre = nombre, correo = email, contrasena = password)
                                authViewModel.registrar(nuevoUsuario)
                            } else {
                                errorLocal = "Las contraseñas no coinciden"
                            }
                        },
                        modifier = Modifier.fillMaxWidth().height(50.dp)
                    ) {
                        Text("Registrarse")
                    }

                    TextButton(onClick = { navController.popBackStack() }) {
                        Text("Volver")
                    }
                }
            }
        }
    }
}