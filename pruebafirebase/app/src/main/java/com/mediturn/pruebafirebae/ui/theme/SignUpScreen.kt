package com.mediturn.pruebafirebae.ui.theme

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

@Composable
fun SignUpScreen() {
    val auth = FirebaseAuth.getInstance()
    val db = Firebase.firestore
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var nombre by remember { mutableStateOf("") }
    var mensaje by remember { mutableStateOf("") }

    Column(
        Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text("Registro con perfil", style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(16.dp))

        OutlinedTextField(value = nombre, onValueChange = { nombre = it }, label = { Text("Nombre completo") })
        OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("Correo electrónico") })
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Contraseña") },
            visualTransformation = PasswordVisualTransformation()
        )

        Spacer(Modifier.height(16.dp))
        Button(onClick = {
            auth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener { result ->
                    val uid = result.user?.uid ?: return@addOnSuccessListener
                    val perfil = mapOf(
                        "nombre" to nombre,
                        "rol" to "Alumno",
                        "email" to email
                    )
                    db.collection("perfiles").document(uid).set(perfil)
                        .addOnSuccessListener {
                            mensaje = "Usuario y perfil registrados correctamente"
                        }
                }
                .addOnFailureListener { e ->
                    mensaje = "Error: ${e.localizedMessage}"
                }
        }, modifier = Modifier.fillMaxWidth()) {
            Text("Registrar usuario")
        }

        Spacer(Modifier.height(12.dp))
        Text(mensaje)
    }
}
