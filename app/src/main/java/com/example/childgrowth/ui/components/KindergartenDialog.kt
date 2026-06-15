package com.example.childgrowth.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import com.example.childgrowth.R

@Composable
fun KindergartenDialog(
    onDismiss: () -> Unit,
    onConfirm: (String, String, String) -> Unit,
) {
    var eventType by remember { mutableStateOf("") }
    var title by remember { mutableStateOf("") }
    var note by remember { mutableStateOf("") }

    RecordDialogFrame(title = stringResource(R.string.dialog_title_kindergarten), onDismiss = onDismiss, onConfirm = {
        if (eventType.isNotBlank() && title.isNotBlank()) {
            onConfirm(eventType.trim(), title.trim(), note.trim())
        }
    }) {
        FormField(value = eventType, onValueChange = { eventType = it }, label = stringResource(R.string.kindergarten_type))
        FormField(value = title, onValueChange = { title = it }, label = stringResource(R.string.kindergarten_title))
        FormField(value = note, onValueChange = { note = it }, label = stringResource(R.string.note_hint))
    }
}
