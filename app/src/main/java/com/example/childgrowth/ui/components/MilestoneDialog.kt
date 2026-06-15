package com.example.childgrowth.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

@Composable
fun MilestoneDialog(
    onDismiss: () -> Unit,
    onConfirm: (String, String, String) -> Unit,
) {
    var milestoneType by remember { mutableStateOf("") }
    var title by remember { mutableStateOf("") }
    var note by remember { mutableStateOf("") }

    RecordDialogFrame(title = "记录成长里程碑", onDismiss = onDismiss, onConfirm = {
        if (milestoneType.isNotBlank() && title.isNotBlank()) {
            onConfirm(milestoneType.trim(), title.trim(), note.trim())
        }
    }) {
        FormField(value = milestoneType, onValueChange = { milestoneType = it }, label = "类型（笑/说话/走路等）")
        FormField(value = title, onValueChange = { title = it }, label = "标题")
        FormField(value = note, onValueChange = { note = it }, label = "备注")
    }
}
