package com.example.childgrowth.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

@Composable
fun HealthDialog(
    onDismiss: () -> Unit,
    onConfirm: (String, String) -> Unit,
) {
    var eventType by remember { mutableStateOf("") }
    var note by remember { mutableStateOf("") }

    RecordDialogFrame(title = "记录生病", onDismiss = onDismiss, onConfirm = {
        if (eventType.isNotBlank()) {
            onConfirm(eventType.trim(), note.trim())
        }
    }) {
        FormField(value = eventType, onValueChange = { eventType = it }, label = "症状或事件")
        FormField(value = note, onValueChange = { note = it }, label = "备注")
    }
}
