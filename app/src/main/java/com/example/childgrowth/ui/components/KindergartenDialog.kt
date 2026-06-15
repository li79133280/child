package com.example.childgrowth.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

@Composable
fun KindergartenDialog(
    onDismiss: () -> Unit,
    onConfirm: (String, String, String) -> Unit,
) {
    var eventType by remember { mutableStateOf("") }
    var title by remember { mutableStateOf("") }
    var note by remember { mutableStateOf("") }

    RecordDialogFrame(title = "记录园所内容", onDismiss = onDismiss, onConfirm = {
        if (eventType.isNotBlank() && title.isNotBlank()) {
            onConfirm(eventType.trim(), title.trim(), note.trim())
        }
    }) {
        FormField(value = eventType, onValueChange = { eventType = it }, label = "类型，例如活动、表扬、请假")
        FormField(value = title, onValueChange = { title = it }, label = "标题")
        FormField(value = note, onValueChange = { note = it }, label = "备注")
    }
}
