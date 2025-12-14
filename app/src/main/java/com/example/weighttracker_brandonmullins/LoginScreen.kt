package com.example.weighttracker_brandonmullins.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp

@Composable
fun LoginScreen(
    error: String?,
    onClearError: () -> Unit,
    onLogin: (String, String) -> Unit,
    onCreateAccount: (String, String) -> Unit
) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Welcome")
        Spacer(Modifier.height(12.dp))

        OutlinedTextField(
            value = username, onValueChange = { username = it },
            label = { Text("Username") }
        )
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(
            value = password, onValueChange = { password = it },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation()
        )

        if (!error.isNullOrBlank()) {
            Spacer(Modifier.height(8.dp))
            Text(error, color = MaterialTheme.colorScheme.error)
            TextButton(onClick = onClearError) { Text("Dismiss") }
        }

        Spacer(Modifier.height(16.dp))
        Button(onClick = { onLogin(username.trim(), password) }, enabled = username.isNotBlank() && password.isNotBlank()) {
            Text("Login")
        }
        Spacer(Modifier.height(8.dp))
        TextButton(onClick = { onCreateAccount(username.trim(), password) },
            enabled = username.isNotBlank() && password.isNotBlank()
        ) { Text("Create Account") }
    }
}
