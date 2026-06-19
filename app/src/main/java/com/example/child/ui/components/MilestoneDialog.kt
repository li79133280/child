package com.example.child.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import com.example.child.R

@Composable
fun MilestoneDialog(
    onDismiss: () -> Unit,
    onConfirm: (String, String, String) -> Unit,
) {
    var milestoneType by remember { mutableStateOf("") }
    var title by remember { mutableStateOf("") }
    var note by remember { mutableStateOf("") }

    RecordDialogFrame(title = stringResource(R.string.dialog_title_milestone), onDismiss = onDismiss, onConfirm = {
        if (milestoneType.isNotBlank() && title.isNotBlank()) {
            onConfirm(milestoneType.trim(), title.trim(), note.trim())
        }
    }) {
        FormField(value = milestoneType, onValueChange = { milestoneType = it }, label = stringResource(R.string.milestone_type))
        FormField(value = title, onValueChange = { title = it }, label = stringResource(R.string.custom_title))
        FormField(value = note, onValueChange = { note = it }, label = stringResource(R.string.note_hint))
    }
}
