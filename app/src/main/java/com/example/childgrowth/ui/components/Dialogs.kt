package com.example.childgrowth.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter

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

@Composable
fun OutingDialog(
    onDismiss: () -> Unit,
    onConfirm: (String, String, String) -> Unit,
) {
    var title by remember { mutableStateOf("") }
    var place by remember { mutableStateOf("") }
    var note by remember { mutableStateOf("") }

    RecordDialogFrame(title = "记录出游", onDismiss = onDismiss, onConfirm = {
        if (title.isNotBlank()) {
            onConfirm(title.trim(), place.trim(), note.trim())
        }
    }) {
        FormField(value = title, onValueChange = { title = it }, label = "活动名称")
        FormField(value = place, onValueChange = { place = it }, label = "地点")
        FormField(value = note, onValueChange = { note = it }, label = "备注")
    }
}

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

@Composable
fun HealthDialog(
    onDismiss: () -> Unit,
    onConfirm: (String, String) -> Unit,
) {
    var eventType by remember { mutableStateOf("") }
    var note by remember { mutableStateOf("") }

    RecordDialogFrame(title = "记录生病", onDismiss = onDismiss, onConfirm = {
        if (eventType.isNotBlank()) {
            onConfirm(eventType.trim(), note.trim())
        }
    }) {
        FormField(value = eventType, onValueChange = { eventType = it }, label = "症状或事件")
        FormField(value = note, onValueChange = { note = it }, label = "备注")
    }
}

@Composable
fun KindergartenDialog(
    onDismiss: () -> Unit,
    onConfirm: (String, String, String) -> Unit,
) {
    var eventType by remember { mutableStateOf("") }
    var title by remember { mutableStateOf("") }
    var note by remember { mutableStateOf("") }

    RecordDialogFrame(title = "记录园所内容", onDismiss = onDismiss, onConfirm = {
        if (eventType.isNotBlank() && title.isNotBlank()) {
            onConfirm(eventType.trim(), title.trim(), note.trim())
        }
    }) {
        FormField(value = eventType, onValueChange = { eventType = it }, label = "类型，例如活动、表扬、请假")
        FormField(value = title, onValueChange = { title = it }, label = "标题")
        FormField(value = note, onValueChange = { note = it }, label = "备注")
    }
}

@Composable
fun ReminderDialog(
    onDismiss: () -> Unit,
    onConfirm: (String, String, Int?, String) -> Unit,
) {
    var reminderType by remember { mutableStateOf("") }
    var title by remember { mutableStateOf("") }
    var days by remember { mutableStateOf("") }
    var note by remember { mutableStateOf("") }

    RecordDialogFrame(title = "新增提醒", onDismiss = onDismiss, onConfirm = {
        if (reminderType.isNotBlank() && title.isNotBlank()) {
            onConfirm(reminderType.trim(), title.trim(), days.toIntOrNull(), note.trim())
        }
    }) {
        FormField(value = reminderType, onValueChange = { reminderType = it }, label = "提醒类型，例如体检、复诊")
        FormField(value = title, onValueChange = { title = it }, label = "提醒标题")
        FormField(value = days, onValueChange = { days = it }, label = "距离提醒天数", keyboardType = KeyboardType.Number)
        FormField(value = note, onValueChange = { note = it }, label = "备注")
    }
}

@Composable
fun CustomEventDialog(
    onDismiss: () -> Unit,
    onConfirm: (String, String, String) -> Unit,
) {
    var category by remember { mutableStateOf("") }
    var title by remember { mutableStateOf("") }
    var note by remember { mutableStateOf("") }

    RecordDialogFrame(title = "记录自定义事件", onDismiss = onDismiss, onConfirm = {
        if (category.isNotBlank() && title.isNotBlank()) {
            onConfirm(category.trim(), title.trim(), note.trim())
        }
    }) {
        FormField(value = category, onValueChange = { category = it }, label = "分类")
        FormField(value = title, onValueChange = { title = it }, label = "标题")
        FormField(value = note, onValueChange = { note = it }, label = "备注")
    }
}

@Composable
private fun RecordDialogFrame(
    title: String,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    content: @Composable () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = title) },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 420.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                Text(
                    text = "默认自动记录当前时间，尽量减少手动输入。",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                content()
            }
        },
        confirmButton = {
            TextButton(onClick = onConfirm) { Text("保存") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("取消") }
        },
    )
}

@Composable
private fun FormField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    keyboardType: KeyboardType = KeyboardType.Text,
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        modifier = Modifier.fillMaxWidth(),
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        singleLine = keyboardType != KeyboardType.Text,
    )
}

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

private fun formatBirthdayForInput(millis: Long): String {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    return Instant.ofEpochMilli(millis).atZone(ZoneId.systemDefault()).format(formatter)
}

private fun parseBirthdayInput(text: String): Long? {
    return try {
        val parts = text.split("-")
        if (parts.size == 3) {
            LocalDate.of(parts[0].toInt(), parts[1].toInt(), parts[2].toInt())
                .atStartOfDay(ZoneId.systemDefault())
                .toInstant()
                .toEpochMilli()
        } else null
    } catch (_: Exception) {
        null
    }
}

@Composable
fun FeedingDialog(
    onDismiss: () -> Unit,
    onConfirm: (String, String, String) -> Unit,
) {
    var feedType by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }
    var note by remember { mutableStateOf("") }

    RecordDialogFrame(title = "记录喂养", onDismiss = onDismiss, onConfirm = {
        if (feedType.isNotBlank()) {
            onConfirm(feedType.trim(), amount.trim(), note.trim())
        }
    }) {
        FormField(value = feedType, onValueChange = { feedType = it }, label = "类型（母乳/奶粉/辅食）")
        FormField(value = amount, onValueChange = { amount = it }, label = "量（如 120ml、半碗）")
        FormField(value = note, onValueChange = { note = it }, label = "备注")
    }
}

@Composable
fun DiaperDialog(
    onDismiss: () -> Unit,
    onConfirm: (String, String) -> Unit,
) {
    var diaperType by remember { mutableStateOf("") }
    var note by remember { mutableStateOf("") }

    RecordDialogFrame(title = "记录换尿布", onDismiss = onDismiss, onConfirm = {
        if (diaperType.isNotBlank()) {
            onConfirm(diaperType.trim(), note.trim())
        }
    }) {
        FormField(value = diaperType, onValueChange = { diaperType = it }, label = "类型（湿/便/混合）")
        FormField(value = note, onValueChange = { note = it }, label = "备注")
    }
}

@Composable
fun MilestoneDialog(
    onDismiss: () -> Unit,
    onConfirm: (String, String, String) -> Unit,
) {
    var milestoneType by remember { mutableStateOf("") }
    var title by remember { mutableStateOf("") }
    var note by remember { mutableStateOf("") }

    RecordDialogFrame(title = "记录成长里程碑", onDismiss = onDismiss, onConfirm = {
        if (milestoneType.isNotBlank() && title.isNotBlank()) {
            onConfirm(milestoneType.trim(), title.trim(), note.trim())
        }
    }) {
        FormField(value = milestoneType, onValueChange = { milestoneType = it }, label = "类型（笑/说话/走路等）")
        FormField(value = title, onValueChange = { title = it }, label = "标题")
        FormField(value = note, onValueChange = { note = it }, label = "备注")
    }
}

@Composable
fun TemperatureDialog(
    onDismiss: () -> Unit,
    onConfirm: (Double, String) -> Unit,
) {
    var temperature by remember { mutableStateOf("") }
    var note by remember { mutableStateOf("") }

    RecordDialogFrame(title = "记录体温", onDismiss = onDismiss, onConfirm = {
        val temp = temperature.toDoubleOrNull()
        if (temp != null) {
            onConfirm(temp, note.trim())
        }
    }) {
        FormField(value = temperature, onValueChange = { temperature = it }, label = "体温（如 36.5）", keyboardType = KeyboardType.Decimal)
        FormField(value = note, onValueChange = { note = it }, label = "备注")
    }
}

@Composable
fun MoodDialog(
    onDismiss: () -> Unit,
    onConfirm: (String, String) -> Unit,
) {
    var moodType by remember { mutableStateOf("") }
    var note by remember { mutableStateOf("") }

    RecordDialogFrame(title = "记录情绪", onDismiss = onDismiss, onConfirm = {
        if (moodType.isNotBlank()) {
            onConfirm(moodType.trim(), note.trim())
        }
    }) {
        FormField(value = moodType, onValueChange = { moodType = it }, label = "情绪（开心/哭闹/安静等）")
        FormField(value = note, onValueChange = { note = it }, label = "备注")
    }
}
