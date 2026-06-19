package com.example.child.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import com.example.child.R

@Composable
fun MoodDialog(
    onDismiss: () -> Unit,
    onConfirm: (String, String) -> Unit,
) {
    var moodType by remember { mutableStateOf("") }
    var note by remember { mutableStateOf("") }

    RecordDialogFrame(title = stringResource(R.string.dialog_title_mood), onDismiss = onDismiss, onConfirm = {
        if (moodType.isNotBlank()) {
            onConfirm(moodType.trim(), note.trim())
        }
    }) {
        FormField(value = moodType, onValueChange = { moodType = it }, label = stringResource(R.string.mood_type))
        FormField(value = note, onValueChange = { note = it }, label = stringResource(R.string.note_hint))
    }
}
