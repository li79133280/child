package com.example.childgrowth.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import com.example.childgrowth.R

@Composable
fun DiaperDialog(
    onDismiss: () -> Unit,
    onConfirm: (String, String) -> Unit,
) {
    var diaperType by remember { mutableStateOf("") }
    var note by remember { mutableStateOf("") }

    RecordDialogFrame(title = stringResource(R.string.dialog_title_diaper), onDismiss = onDismiss, onConfirm = {
        if (diaperType.isNotBlank()) {
            onConfirm(diaperType.trim(), note.trim())
        }
    }) {
        FormField(value = diaperType, onValueChange = { diaperType = it }, label = stringResource(R.string.diaper_type))
        FormField(value = note, onValueChange = { note = it }, label = stringResource(R.string.note_hint))
    }
}
