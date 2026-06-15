package com.example.childgrowth.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.KeyboardType

@Composable
fun MedicationDialog(
    onDismiss: () -> Unit,
    onConfirm: (String, String, String, Int?, String) -> Unit,
) {
    var medicineName by remember { mutableStateOf("") }
    var symptom by remember { mutableStateOf("") }
    var dose by remember { mutableStateOf("") }
    var intervalHours by remember { mutableStateOf("") }
    var note by remember { mutableStateOf("") }

    RecordDialogFrame(title = "记录吃药", onDismiss = onDismiss, onConfirm = {
        if (medicineName.isNotBlank()) {
            onConfirm(
                medicineName.trim(),
                symptom.trim(),
                dose.trim(),
                intervalHours.toIntOrNull(),
                note.trim(),
            )
        }
    }) {
        FormField(value = medicineName, onValueChange = { medicineName = it }, label = "药名")
        FormField(value = symptom, onValueChange = { symptom = it }, label = "症状")
        FormField(value = dose, onValueChange = { dose = it }, label = "剂量")
        FormField(value = intervalHours, onValueChange = { intervalHours = it }, label = "距离下次服药小时数", keyboardType = KeyboardType.Number)
        FormField(value = note, onValueChange = { note = it }, label = "备注")
    }
}
