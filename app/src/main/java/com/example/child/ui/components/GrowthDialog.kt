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
fun GrowthDialog(
    onDismiss: () -> Unit,
    onConfirm: (Double, Double, String) -> Unit,
) {
    var height by remember { mutableStateOf("") }
    var weight by remember { mutableStateOf("") }
    var note by remember { mutableStateOf("") }

    RecordDialogFrame(title = stringResource(R.string.dialog_title_growth), onDismiss = onDismiss, onConfirm = {
        val heightValue = height.toDoubleOrNull()
        val weightValue = weight.toDoubleOrNull()
        if (heightValue != null && weightValue != null) {
            onConfirm(heightValue, weightValue, note.trim())
        }
    }) {
        FormField(value = height, onValueChange = { height = it }, label = stringResource(R.string.growth_height), keyboardType = KeyboardType.Decimal)
        FormField(value = weight, onValueChange = { weight = it }, label = stringResource(R.string.growth_weight), keyboardType = KeyboardType.Decimal)
        FormField(value = note, onValueChange = { note = it }, label = stringResource(R.string.note_hint))
    }
}
