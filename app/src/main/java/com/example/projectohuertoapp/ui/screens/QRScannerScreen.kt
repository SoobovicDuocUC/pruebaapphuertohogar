package com.example.projectohuertoapp.ui.screens

import android.Manifest
import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.widget.Toast
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageProxy
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
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.example.projectohuertoapp.ui.permission.WithPermission
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage
import java.util.concurrent.Executors

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

        // Overlay de guía para el QR (Mantenemos tu diseño exacto)
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
                Box(modifier = Modifier.size(40.dp, 4.dp).background(Color.White).align(Alignment.TopStart))
                Box(modifier = Modifier.size(4.dp, 40.dp).background(Color.White).align(Alignment.TopStart))
                Box(modifier = Modifier.size(40.dp, 4.dp).background(Color.White).align(Alignment.TopEnd))
                Box(modifier = Modifier.size(4.dp, 40.dp).background(Color.White).align(Alignment.TopEnd))
                Box(modifier = Modifier.size(40.dp, 4.dp).background(Color.White).align(Alignment.BottomStart))
                Box(modifier = Modifier.size(4.dp, 40.dp).background(Color.White).align(Alignment.BottomStart))
                Box(modifier = Modifier.size(40.dp, 4.dp).background(Color.White).align(Alignment.BottomEnd))
                Box(modifier = Modifier.size(4.dp, 40.dp).background(Color.White).align(Alignment.BottomEnd))
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
    val localContext = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    val previewUseCase = remember { androidx.camera.core.Preview.Builder().build() }

    // 1. NUEVO: Creamos el Analizador de Imágenes para detectar el QR
    val imageAnalysis = remember {
        ImageAnalysis.Builder()
            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
            .build()
    }

    var cameraProvider by remember { mutableStateOf<ProcessCameraProvider?>(null) }

    // Para no vibrar 100 veces por segundo, guardamos el último código leído
    var ultimoCodigo by remember { mutableStateOf<String?>(null) }

    // Configuración del Analizador (Aquí ocurre la magia)
    DisposableEffect(Unit) {
        val analyzer = ImageAnalysis.Analyzer { imageProxy ->
            procesarImagenQR(imageProxy) { codigoDetectado ->
                // Si detectamos un código NUEVO (distinto al anterior)
                if (ultimoCodigo != codigoDetectado) {
                    ultimoCodigo = codigoDetectado

                    // ¡VIBRAR!
                    vibrar(localContext)

                    // Mostrar mensaje visual
                    Toast.makeText(localContext, "QR Detectado: $codigoDetectado", Toast.LENGTH_SHORT).show()
                }
            }
        }

        // Ejecutamos el análisis en un hilo secundario
        imageAnalysis.setAnalyzer(ContextCompat.getMainExecutor(localContext), analyzer)

        onDispose { imageAnalysis.clearAnalyzer() }
    }

    fun rebindCameraProvider() {
        cameraProvider?.let { provider ->
            val cameraSelector = CameraSelector.Builder()
                .requireLensFacing(lensFacing)
                .build()

            try {
                provider.unbindAll()
                provider.bindToLifecycle(
                    lifecycleOwner,
                    cameraSelector,
                    previewUseCase,
                    imageCaptureUseCase,
                    imageAnalysis // <--- Agregamos el analizador aquí
                )
            } catch (e: Exception) {
                e.printStackTrace()
            }
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

// --- Funciones Auxiliares (Copia esto al final del archivo) ---

// Lógica de ML Kit para leer el QR
@androidx.annotation.OptIn(androidx.camera.core.ExperimentalGetImage::class)
fun procesarImagenQR(imageProxy: ImageProxy, alDetectar: (String) -> Unit) {
    val mediaImage = imageProxy.image
    if (mediaImage != null) {
        val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)
        BarcodeScanning.getClient().process(image)
            .addOnSuccessListener { barcodes ->
                for (barcode in barcodes) {
                    barcode.rawValue?.let { alDetectar(it) }
                }
            }
            .addOnCompleteListener {
                imageProxy.close() // ¡IMPORTANTE! Si no cierras, la cámara se congela
            }
    } else {
        imageProxy.close()
    }
}

// Lógica de Vibración
fun vibrar(context: Context) {
    val v = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    if (Build.VERSION.SDK_INT >= 26) {
        v.vibrate(VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE))
    } else {
        v.vibrate(100)
    }
}