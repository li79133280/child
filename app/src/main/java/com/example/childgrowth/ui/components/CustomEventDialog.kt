package com.example.childgrowth.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

@Composable
fun CustomEventDialog(
    onDismiss: () -> Unit,
    onConfirm: (String, String, String) -> Unit,
) {
    var category by remember { mutableStateOf("") }
    var title by remember { mutableStateOf("") }
    var note by remember { mutableStateOf("") }

    RecordDialogFrame(title = "记录自定义事件", onDismiss = onDismiss, onConfirm = {
        if (category.isNotBlank() && title.isNotBlank()) {
            onConfirm(category.trim(), title.trim(), note.trim())
        }
    }) {
        FormField(value = category, onValueChange = { category = it }, label = "分类")
        FormField(value = title, onValueChange = { title = it }, label = "标题")
        FormField(value = note, onValueChange = { note = it }, label = "备注")
    }
}
