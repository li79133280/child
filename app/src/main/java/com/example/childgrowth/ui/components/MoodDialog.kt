package com.example.childgrowth.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

@Composable
fun MoodDialog(
    onDismiss: () -> Unit,
    onConfirm: (String, String) -> Unit,
) {
    var moodType by remember { mutableStateOf("") }
    var note by remember { mutableStateOf("") }

    RecordDialogFrame(title = "记录情绪", onDismiss = onDismiss, onConfirm = {
        if (moodType.isNotBlank()) {
            onConfirm(moodType.trim(), note.trim())
        }
    }) {
        FormField(value = moodType, onValueChange = { moodType = it }, label = "情绪（开心/哭闹/安静等）")
        FormField(value = note, onValueChange = { note = it }, label = "备注")
    }
}
