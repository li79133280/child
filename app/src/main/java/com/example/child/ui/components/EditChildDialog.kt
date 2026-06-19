package com.example.child.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.child.R

@Composable
fun EditChildDialog(
    initialName: String,
    initialGender: String,
    initialBirthday: Long,
    initialAccentColor: String,
    initialAvatarLabel: String,
    onDismiss: () -> Unit,
    onConfirm: (String, String, Long, String, String) -> Unit,
) {
    var name by remember { mutableStateOf(initialName) }
    var gender by remember { mutableStateOf(initialGender) }
    var birthdayText by remember { mutableStateOf(formatBirthdayForInput(initialBirthday)) }
    var accentColor by remember { mutableStateOf(initialAccentColor) }
    var avatarLabel by remember { mutableStateOf(initialAvatarLabel) }
    val defaultGender = stringResource(R.string.child_default_gender)

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = stringResource(R.string.dialog_title_edit_child)) },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 420.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                FormField(value = name, onValueChange = { name = it }, label = stringResource(R.string.child_name))
                FormField(value = gender, onValueChange = { gender = it }, label = stringResource(R.string.child_gender))
                FormField(value = birthdayText, onValueChange = { birthdayText = it }, label = stringResource(R.string.child_birthday))
                FormField(value = avatarLabel, onValueChange = { avatarLabel = it }, label = stringResource(R.string.child_avatar))
                FormField(value = accentColor, onValueChange = { accentColor = it }, label = stringResource(R.string.child_theme_color))
            }
        },
        confirmButton = {
            TextButton(onClick = {
                if (name.isNotBlank()) {
                    val birthdayMillis = parseBirthdayInput(birthdayText)
                    if (birthdayMillis != null) {
                        onConfirm(
                            name.trim(),
                            gender.trim().ifBlank { defaultGender },
                            birthdayMillis,
                            accentColor.trim().ifBlank { "#2E7D6B" },
                            avatarLabel.trim().ifBlank { name.trim().first().toString() },
                        )
                    }
                }
            }) { Text(stringResource(R.string.save)) }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text(stringResource(R.string.cancel)) }
        },
    )
}
