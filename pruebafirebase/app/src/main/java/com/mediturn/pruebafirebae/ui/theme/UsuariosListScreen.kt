package com.mediturn.pruebafirebae.ui.theme

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.mediturn.pruebafirebae.data.Usuario

@Composable
fun UsuariosListScreen() {
    val db = Firebase.firestore
    var usuarios by remember { mutableStateOf(listOf<Usuario>()) } // <--- aseguramos tipo correcto


    LaunchedEffect(Unit) {
        db.collection("perfiles")
            .addSnapshotListener { snapshot, e ->
                if (e == null && snapshot != null) {
                    usuarios = snapshot.documents.mapNotNull { it.toObject(Usuario::class.java) }
                }
            }
    }

    Column(
        Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Lista de usuarios", style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(8.dp))

        LazyColumn {
            items(usuarios) { usuario ->
                Card(
                    Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(Modifier.padding(12.dp)) {
                        Text("Nombre: ${usuario.nombre}")
                        Text("Rol: ${usuario.rol}")
                    }
                }
            }
        }
    }
}
