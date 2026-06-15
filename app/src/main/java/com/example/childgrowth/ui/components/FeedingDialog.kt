package com.example.childgrowth.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

@Composable
fun FeedingDialog(
    onDismiss: () -> Unit,
    onConfirm: (String, String, String) -> Unit,
) {
    var feedType by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }
    var note by remember { mutableStateOf("") }

    RecordDialogFrame(title = "记录喂养", onDismiss = onDismiss, onConfirm = {
        if (feedType.isNotBlank()) {
            onConfirm(feedType.trim(), amount.trim(), note.trim())
        }
    }) {
        FormField(value = feedType, onValueChange = { feedType = it }, label = "类型（母乳/奶粉/辅食）")
        FormField(value = amount, onValueChange = { amount = it }, label = "量（如 120ml、半碗）")
        FormField(value = note, onValueChange = { note = it }, label = "备注")
    }
}
