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
fun VaccineDialog(
    onDismiss: () -> Unit,
    onConfirm: (String, String, Int?, String, String) -> Unit,
) {
    var vaccineName by remember { mutableStateOf("") }
    var doseLabel by remember { mutableStateOf("") }
    var nextDays by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    var note by remember { mutableStateOf("") }
    val defaultDose = stringResource(R.string.vaccine_default_dose)

    RecordDialogFrame(title = stringResource(R.string.dialog_title_vaccine), onDismiss = onDismiss, onConfirm = {
        if (vaccineName.isNotBlank()) {
            onConfirm(
                vaccineName.trim(),
                doseLabel.trim().ifBlank { defaultDose },
                nextDays.toIntOrNull(),
                location.trim(),
                note.trim(),
            )
        }
    }) {
        FormField(value = vaccineName, onValueChange = { vaccineName = it }, label = stringResource(R.string.vaccine_name))
        FormField(value = doseLabel, onValueChange = { doseLabel = it }, label = stringResource(R.string.vaccine_dose))
        FormField(value = nextDays, onValueChange = { nextDays = it }, label = stringResource(R.string.vaccine_next_days), keyboardType = KeyboardType.Number)
        FormField(value = location, onValueChange = { location = it }, label = stringResource(R.string.vaccine_location))
        FormField(value = note, onValueChange = { note = it }, label = stringResource(R.string.note_hint))
    }
}
