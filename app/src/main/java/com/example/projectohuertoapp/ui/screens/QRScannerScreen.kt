package com.example.projectohuertoapp.ui.screens

import android.Manifest
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.lifecycle.awaitInstance
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavController
import com.example.projectohuertoapp.ui.permission.WithPermission

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QRScannerScreen(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Escanear Código QR") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { padding ->
        WithPermission(
            modifier = Modifier.padding(padding),
            permission = Manifest.permission.CAMERA
        ) {
            CameraPreviewScreen()
        }
    }
}

@Composable
fun CameraPreviewScreen() {
    val imageCaptureUseCase = remember { ImageCapture.Builder().build() }

    Box(modifier = Modifier.fillMaxSize()) {
        CameraPreview(
            lensFacing = CameraSelector.LENS_FACING_BACK,
            imageCaptureUseCase = imageCaptureUseCase
        )

        // Overlay de guía para el QR
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(48.dp),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .size(280.dp)
                    .background(Color.Transparent)
            ) {
                // Esquinas del marco
                Box(
                    modifier = Modifier
                        .size(40.dp, 4.dp)
                        .background(Color.White)
                        .align(Alignment.TopStart)
                )
                Box(
                    modifier = Modifier
                        .size(4.dp, 40.dp)
                        .background(Color.White)
                        .align(Alignment.TopStart)
                )
                Box(
                    modifier = Modifier
                        .size(40.dp, 4.dp)
                        .background(Color.White)
                        .align(Alignment.TopEnd)
                )
                Box(
                    modifier = Modifier
                        .size(4.dp, 40.dp)
                        .background(Color.White)
                        .align(Alignment.TopEnd)
                )
                Box(
                    modifier = Modifier
                        .size(40.dp, 4.dp)
                        .background(Color.White)
                        .align(Alignment.BottomStart)
                )
                Box(
                    modifier = Modifier
                        .size(4.dp, 40.dp)
                        .background(Color.White)
                        .align(Alignment.BottomStart)
                )
                Box(
                    modifier = Modifier
                        .size(40.dp, 4.dp)
                        .background(Color.White)
                        .align(Alignment.BottomEnd)
                )
                Box(
                    modifier = Modifier
                        .size(4.dp, 40.dp)
                        .background(Color.White)
                        .align(Alignment.BottomEnd)
                )
            }
        }

        // Texto de instrucción
        Text(
            text = "Apunta la cámara al código QR",
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 48.dp)
                .background(
                    Color.Black.copy(alpha = 0.7f),
                    shape = MaterialTheme.shapes.medium
                )
                .padding(horizontal = 24.dp, vertical = 12.dp),
            color = Color.White,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Composable
fun CameraPreview(
    modifier: Modifier = Modifier,
    lensFacing: Int,
    imageCaptureUseCase: ImageCapture
) {
    val previewUseCase = remember {
        androidx.camera.core.Preview.Builder().build()
    }
    var cameraProvider by remember { mutableStateOf<ProcessCameraProvider?>(null) }

    val localContext = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    fun rebindCameraProvider() {
        cameraProvider?.let { provider ->
            val cameraSelector = CameraSelector.Builder()
                .requireLensFacing(lensFacing)
                .build()
            provider.unbindAll()
            provider.bindToLifecycle(
                lifecycleOwner,
                cameraSelector,
                previewUseCase,
                imageCaptureUseCase
            )
        }
    }

    LaunchedEffect(Unit) {
        cameraProvider = ProcessCameraProvider.awaitInstance(localContext)
        rebindCameraProvider()
    }

    LaunchedEffect(lensFacing) {
        rebindCameraProvider()
    }

    AndroidView(
        modifier = modifier.fillMaxSize(),
        factory = { context ->
            PreviewView(context).also {
                previewUseCase.surfaceProvider = it.surfaceProvider
                rebindCameraProvider()
            }
        }
    )
}