package com.example.childgrowth.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.KeyboardType

@Composable
fun VaccineDialog(
    onDismiss: () -> Unit,
    onConfirm: (String, String, Int?, String, String) -> Unit,
) {
    var vaccineName by remember { mutableStateOf("") }
    var doseLabel by remember { mutableStateOf("") }
    var nextDays by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    var note by remember { mutableStateOf("") }

    RecordDialogFrame(title = "记录疫苗", onDismiss = onDismiss, onConfirm = {
        if (vaccineName.isNotBlank()) {
            onConfirm(
                vaccineName.trim(),
                doseLabel.trim().ifBlank { "本次接种" },
                nextDays.toIntOrNull(),
                location.trim(),
                note.trim(),
            )
        }
    }) {
        FormField(value = vaccineName, onValueChange = { vaccineName = it }, label = "疫苗名称")
        FormField(value = doseLabel, onValueChange = { doseLabel = it }, label = "针次")
        FormField(value = nextDays, onValueChange = { nextDays = it }, label = "距离下次接种天数", keyboardType = KeyboardType.Number)
        FormField(value = location, onValueChange = { location = it }, label = "接种地点")
        FormField(value = note, onValueChange = { note = it }, label = "备注")
    }
}
