package com.example.childgrowth.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import com.example.childgrowth.R

@Composable
fun CustomEventDialog(
    onDismiss: () -> Unit,
    onConfirm: (String, String, String) -> Unit,
) {
    var category by remember { mutableStateOf("") }
    var title by remember { mutableStateOf("") }
    var note by remember { mutableStateOf("") }

    RecordDialogFrame(title = stringResource(R.string.dialog_title_custom_event), onDismiss = onDismiss, onConfirm = {
        if (category.isNotBlank() && title.isNotBlank()) {
            onConfirm(category.trim(), title.trim(), note.trim())
        }
    }) {
        FormField(value = category, onValueChange = { category = it }, label = stringResource(R.string.custom_category))
        FormField(value = title, onValueChange = { title = it }, label = stringResource(R.string.custom_title))
        FormField(value = note, onValueChange = { note = it }, label = stringResource(R.string.note_hint))
    }
}
