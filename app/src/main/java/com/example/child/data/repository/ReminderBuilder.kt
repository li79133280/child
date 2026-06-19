package com.example.child.data.repository

import com.example.child.data.local.ManualReminderRecord
import com.example.child.data.local.MedicationRecord
import com.example.child.data.local.VaccineRecord

internal fun buildReminderList(
    upcomingVaccine: VaccineRecord?,
    nextMedication: MedicationRecord?,
    reminders: List<ManualReminderRecord>,
): List<ReminderItem> {
    val vaccineReminder = upcomingVaccine?.nextDueAt?.let {
        ReminderItem(
            id = "rem-v-${upcomingVaccine.id}",
            dueAt = it,
            title = upcomingVaccine.vaccineName,
            subtitle = "疫苗提醒",
        )
    }
    val medicationReminder = nextMedication?.nextDueAt?.let {
        ReminderItem(
            id = "rem-m-${nextMedication.id}",
            dueAt = it,
            title = nextMedication.medicineName,
            subtitle = "下次吃药",
        )
    }
    val manualItems = reminders.map {
        ReminderItem(
            id = "rem-${it.id}",
            dueAt = it.dueAt,
            title = it.title,
            subtitle = it.reminderType,
        )
    }
    return (listOfNotNull(vaccineReminder, medicationReminder) + manualItems).sortedBy { it.dueAt }
}
