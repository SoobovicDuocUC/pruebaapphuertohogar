package com.example.projectohuertoapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.projectohuertoapp.viewmodel.AuthViewModel
import com.example.projectohuertoapp.viewmodel.RegistroState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistroScreen(navController: NavController, authViewModel: AuthViewModel) {
    var nombre by remember { mutableStateOf("") }
    var correo by remember { mutableStateOf("") }//hola
    var contrasena by remember { mutableStateOf("") }
    var errorManual by remember { mutableStateOf<String?>(null) }

    // Observar el estado de registro desde el ViewModel
    val registroState by authViewModel.registroState.collectAsState()

    // Reaccionar al estado de registro
    LaunchedEffect(registroState) {
        if (registroState is RegistroState.Success) {
            navController.navigate("catalogo") {
                popUpTo("login") { inclusive = true }
            }
            // Limpiar el estado después de navegar
            authViewModel.resetRegistroState()
        }
    }

    // Limpiar el estado si el usuario sale de la pantalla
    DisposableEffect(Unit) {
        onDispose {
            authViewModel.resetRegistroState()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Registro de Usuario") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver atrás"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "Crea tu cuenta",
                style = MaterialTheme.typography.headlineLarge,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                "Únete a la comunidad de HuertoHogar",
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
            )
            Spacer(modifier = Modifier.height(48.dp))

            OutlinedTextField(
                value = nombre,
                onValueChange = { nombre = it },
                label = { Text("Nombre Completo") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                enabled = registroState !is RegistroState.Loading
            )
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = correo,
                onValueChange = { correo = it },
                label = { Text("Correo Electrónico") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                enabled = registroState !is RegistroState.Loading
            )
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = contrasena,
                onValueChange = { contrasena = it },
                label = { Text("Contraseña (mín. 6 caracteres)") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                enabled = registroState !is RegistroState.Loading
            )

            // Mostrar error manual (campos vacíos)
            errorManual?.let {
                Text(
                    it,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(top = 8.dp),
                    style = MaterialTheme.typography.bodySmall
                )
            }

            // Mostrar error de registro (ej. email duplicado)
            if (registroState is RegistroState.Error) {
                Text(
                    (registroState as RegistroState.Error).message,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(top = 8.dp),
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Mostrar indicador de carga
            if (registroState is RegistroState.Loading) {
                CircularProgressIndicator(modifier = Modifier.padding(bottom = 16.dp))
            }

            Button(
                onClick = {
                    errorManual = null // Limpiar error manual
                    if (nombre.isNotBlank() && correo.isNotBlank() && contrasena.length >= 6) {
                        // La lógica de navegación ahora está en el LaunchedEffect
                        authViewModel.registrarUsuario(nombre, correo, contrasena)
                    } else {
                        errorManual = "Por favor, completa todos los campos correctamente."
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                enabled = registroState !is RegistroState.Loading
            ) {
                Text("Crear Cuenta", style = MaterialTheme.typography.labelLarge)
            }
            TextButton(onClick = { navController.popBackStack() }) {
                Text("¿Ya tienes cuenta? Inicia sesión")
            }
        }
    }
}