package com.example.childgrowth.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import com.example.childgrowth.R

@Composable
fun HealthDialog(
    onDismiss: () -> Unit,
    onConfirm: (String, String) -> Unit,
) {
    var eventType by remember { mutableStateOf("") }
    var note by remember { mutableStateOf("") }

    RecordDialogFrame(title = stringResource(R.string.dialog_title_health), onDismiss = onDismiss, onConfirm = {
        if (eventType.isNotBlank()) {
            onConfirm(eventType.trim(), note.trim())
        }
    }) {
        FormField(value = eventType, onValueChange = { eventType = it }, label = stringResource(R.string.health_event_type))
        FormField(value = note, onValueChange = { note = it }, label = stringResource(R.string.note_hint))
    }
}
