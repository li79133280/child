package com.example.childgrowth.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.KeyboardType

@Composable
fun TemperatureDialog(
    onDismiss: () -> Unit,
    onConfirm: (Double, String) -> Unit,
) {
    var temperature by remember { mutableStateOf("") }
    var note by remember { mutableStateOf("") }

    RecordDialogFrame(title = "记录体温", onDismiss = onDismiss, onConfirm = {
        val temp = temperature.toDoubleOrNull()
        if (temp != null) {
            onConfirm(temp, note.trim())
        }
    }) {
        FormField(value = temperature, onValueChange = { temperature = it }, label = "体温（如 36.5）", keyboardType = KeyboardType.Decimal)
        FormField(value = note, onValueChange = { note = it }, label = "备注")
    }
}
