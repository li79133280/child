package com.example.childgrowth.ui.components

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
import androidx.compose.ui.unit.dp

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

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "编辑孩子信息") },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 420.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                FormField(value = name, onValueChange = { name = it }, label = "姓名")
                FormField(value = gender, onValueChange = { gender = it }, label = "性别（男孩/女孩）")
                FormField(value = birthdayText, onValueChange = { birthdayText = it }, label = "生日（如 2025-04-24）")
                FormField(value = avatarLabel, onValueChange = { avatarLabel = it }, label = "头像文字（如 弟、姐）")
                FormField(value = accentColor, onValueChange = { accentColor = it }, label = "主题色（如 #2E7D6B）")
            }
        },
        confirmButton = {
            TextButton(onClick = {
                if (name.isNotBlank()) {
                    val birthdayMillis = parseBirthdayInput(birthdayText)
                    if (birthdayMillis != null) {
                        onConfirm(
                            name.trim(),
                            gender.trim().ifBlank { "男孩" },
                            birthdayMillis,
                            accentColor.trim().ifBlank { "#2E7D6B" },
                            avatarLabel.trim().ifBlank { name.trim().first().toString() },
                        )
                    }
                }
            }) { Text("保存") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("取消") }
        },
    )
}
