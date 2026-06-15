package com.example.childgrowth.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import com.example.childgrowth.R

@Composable
fun OutingDialog(
    onDismiss: () -> Unit,
    onConfirm: (String, String, String) -> Unit,
) {
    var title by remember { mutableStateOf("") }
    var place by remember { mutableStateOf("") }
    var note by remember { mutableStateOf("") }

    RecordDialogFrame(title = stringResource(R.string.dialog_title_outing), onDismiss = onDismiss, onConfirm = {
        if (title.isNotBlank()) {
            onConfirm(title.trim(), place.trim(), note.trim())
        }
    }) {
        FormField(value = title, onValueChange = { title = it }, label = stringResource(R.string.outing_title))
        FormField(value = place, onValueChange = { place = it }, label = stringResource(R.string.outing_place))
        FormField(value = note, onValueChange = { note = it }, label = stringResource(R.string.note_hint))
    }
}
