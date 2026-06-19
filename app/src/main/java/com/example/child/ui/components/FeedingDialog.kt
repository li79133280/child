package com.example.child.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import com.example.child.R

@Composable
fun FeedingDialog(
    onDismiss: () -> Unit,
    onConfirm: (String, String, String) -> Unit,
) {
    var feedType by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }
    var note by remember { mutableStateOf("") }

    RecordDialogFrame(title = stringResource(R.string.dialog_title_feeding), onDismiss = onDismiss, onConfirm = {
        if (feedType.isNotBlank()) {
            onConfirm(feedType.trim(), amount.trim(), note.trim())
        }
    }) {
        FormField(value = feedType, onValueChange = { feedType = it }, label = stringResource(R.string.feeding_type))
        FormField(value = amount, onValueChange = { amount = it }, label = stringResource(R.string.feeding_amount))
        FormField(value = note, onValueChange = { note = it }, label = stringResource(R.string.note_hint))
    }
}
