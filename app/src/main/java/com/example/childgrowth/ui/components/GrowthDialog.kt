package com.example.childgrowth.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.KeyboardType

@Composable
fun GrowthDialog(
    onDismiss: () -> Unit,
    onConfirm: (Double, Double, String) -> Unit,
) {
    var height by remember { mutableStateOf("") }
    var weight by remember { mutableStateOf("") }
    var note by remember { mutableStateOf("") }

    RecordDialogFrame(title = "记录身高体重", onDismiss = onDismiss, onConfirm = {
        val heightValue = height.toDoubleOrNull()
        val weightValue = weight.toDoubleOrNull()
        if (heightValue != null && weightValue != null) {
            onConfirm(heightValue, weightValue, note.trim())
        }
    }) {
        FormField(value = height, onValueChange = { height = it }, label = "身高（cm）", keyboardType = KeyboardType.Decimal)
        FormField(value = weight, onValueChange = { weight = it }, label = "体重（kg）", keyboardType = KeyboardType.Decimal)
        FormField(value = note, onValueChange = { note = it }, label = "备注")
    }
}
