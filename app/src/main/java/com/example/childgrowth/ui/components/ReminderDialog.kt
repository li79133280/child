package com.example.childgrowth.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.KeyboardType

@Composable
fun ReminderDialog(
    onDismiss: () -> Unit,
    onConfirm: (String, String, Int?, String) -> Unit,
) {
    var reminderType by remember { mutableStateOf("") }
    var title by remember { mutableStateOf("") }
    var days by remember { mutableStateOf("") }
    var note by remember { mutableStateOf("") }

    RecordDialogFrame(title = "新增提醒", onDismiss = onDismiss, onConfirm = {
        if (reminderType.isNotBlank() && title.isNotBlank()) {
            onConfirm(reminderType.trim(), title.trim(), days.toIntOrNull(), note.trim())
        }
    }) {
        FormField(value = reminderType, onValueChange = { reminderType = it }, label = "提醒类型，例如体检、复诊")
        FormField(value = title, onValueChange = { title = it }, label = "提醒标题")
        FormField(value = days, onValueChange = { days = it }, label = "距离提醒天数", keyboardType = KeyboardType.Number)
        FormField(value = note, onValueChange = { note = it }, label = "备注")
    }
}
