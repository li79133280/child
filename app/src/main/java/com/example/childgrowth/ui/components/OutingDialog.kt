package com.example.childgrowth.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

@Composable
fun OutingDialog(
    onDismiss: () -> Unit,
    onConfirm: (String, String, String) -> Unit,
) {
    var title by remember { mutableStateOf("") }
    var place by remember { mutableStateOf("") }
    var note by remember { mutableStateOf("") }

    RecordDialogFrame(title = "记录出游", onDismiss = onDismiss, onConfirm = {
        if (title.isNotBlank()) {
            onConfirm(title.trim(), place.trim(), note.trim())
        }
    }) {
        FormField(value = title, onValueChange = { title = it }, label = "活动名称")
        FormField(value = place, onValueChange = { place = it }, label = "地点")
        FormField(value = note, onValueChange = { note = it }, label = "备注")
    }
}
