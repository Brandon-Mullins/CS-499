package com.example.weighttracker_brandonmullins.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SmsPermissionScreen(
    granted: Boolean,
    onRequestPermission: () -> Unit,
    onSendTest: (String) -> Unit,
    onBack: () -> Unit
) {
    var phone by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("SMS Permission")
        Spacer(Modifier.height(12.dp))

        if (!granted) {
            Button(onClick = onRequestPermission) { Text("Allow SMS Notifications") }
            Spacer(Modifier.height(8.dp))
            Text("Permission not granted yet")
        } else {
            OutlinedTextField(
                value = phone, onValueChange = { phone = it },
                label = { Text("Phone number") }
            )
            Spacer(Modifier.height(8.dp))
            Button(onClick = { if (phone.isNotBlank()) onSendTest(phone) }) {
                Text("Send test SMS")
            }
            Spacer(Modifier.height(8.dp))
            Text("Granted")
        }

        Spacer(Modifier.height(16.dp))
        TextButton(onClick = onBack) { Text("Back") }
    }
}
