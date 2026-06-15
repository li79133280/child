package com.example.childgrowth.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

@Composable
fun DiaperDialog(
    onDismiss: () -> Unit,
    onConfirm: (String, String) -> Unit,
) {
    var diaperType by remember { mutableStateOf("") }
    var note by remember { mutableStateOf("") }

    RecordDialogFrame(title = "记录换尿布", onDismiss = onDismiss, onConfirm = {
        if (diaperType.isNotBlank()) {
            onConfirm(diaperType.trim(), note.trim())
        }
    }) {
        FormField(value = diaperType, onValueChange = { diaperType = it }, label = "类型（湿/便/混合）")
        FormField(value = note, onValueChange = { note = it }, label = "备注")
    }
}
