package com.example.childgrowth.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import com.example.childgrowth.R

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

    RecordDialogFrame(title = stringResource(R.string.dialog_title_medication), onDismiss = onDismiss, onConfirm = {
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
        FormField(value = medicineName, onValueChange = { medicineName = it }, label = stringResource(R.string.medication_name))
        FormField(value = symptom, onValueChange = { symptom = it }, label = stringResource(R.string.medication_symptom))
        FormField(value = dose, onValueChange = { dose = it }, label = stringResource(R.string.medication_dose))
        FormField(value = intervalHours, onValueChange = { intervalHours = it }, label = stringResource(R.string.medication_interval), keyboardType = KeyboardType.Number)
        FormField(value = note, onValueChange = { note = it }, label = stringResource(R.string.note_hint))
    }
}
