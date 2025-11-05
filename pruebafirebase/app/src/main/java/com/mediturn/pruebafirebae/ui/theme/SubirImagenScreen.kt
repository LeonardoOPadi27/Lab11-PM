package com.mediturn.pruebafirebae.ui.theme

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

@Composable
fun SubirImagenScreen() {
    val storage = Firebase.storage
    var imagenUrl by remember { mutableStateOf<String?>(null) }

    // Lanzador para seleccionar imagen
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            val ref = storage.reference.child("images/${System.currentTimeMillis()}.jpg")
            ref.putFile(it).continueWithTask { task ->
                if (!task.isSuccessful) throw task.exception ?: Exception("Error al subir")
                ref.downloadUrl
            }.addOnSuccessListener { downloadUri ->
                imagenUrl = downloadUri.toString()
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text("Subir y mostrar imagen", style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(16.dp))

        Button(onClick = { launcher.launch("image/*") }, modifier = Modifier.fillMaxWidth()) {
            Text("Seleccionar y subir imagen")
        }

        Spacer(Modifier.height(16.dp))

        imagenUrl?.let {
            AsyncImage(
                model = it,
                contentDescription = "Imagen subida",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
            )
        }
    }
}
