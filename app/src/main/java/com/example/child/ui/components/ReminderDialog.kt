package com.example.child.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import com.example.child.R

@Composable
fun ReminderDialog(
    onDismiss: () -> Unit,
    onConfirm: (String, String, Int?, String) -> Unit,
) {
    var reminderType by remember { mutableStateOf("") }
    var title by remember { mutableStateOf("") }
    var days by remember { mutableStateOf("") }
    var note by remember { mutableStateOf("") }

    RecordDialogFrame(title = stringResource(R.string.dialog_title_reminder), onDismiss = onDismiss, onConfirm = {
        if (reminderType.isNotBlank() && title.isNotBlank()) {
            onConfirm(reminderType.trim(), title.trim(), days.toIntOrNull(), note.trim())
        }
    }) {
        FormField(value = reminderType, onValueChange = { reminderType = it }, label = stringResource(R.string.reminder_type))
        FormField(value = title, onValueChange = { title = it }, label = stringResource(R.string.reminder_title))
        FormField(value = days, onValueChange = { days = it }, label = stringResource(R.string.reminder_days), keyboardType = KeyboardType.Number)
        FormField(value = note, onValueChange = { note = it }, label = stringResource(R.string.note_hint))
    }
}
