package com.example.childgrowth.data.repository

import com.example.childgrowth.data.local.CustomEventRecord
import com.example.childgrowth.data.local.GrowthRecord
import com.example.childgrowth.data.local.HealthRecord
import com.example.childgrowth.data.local.KindergartenRecord
import com.example.childgrowth.data.local.ManualReminderRecord
import com.example.childgrowth.data.local.MedicationRecord
import com.example.childgrowth.data.local.OutingRecord
import com.example.childgrowth.data.local.SleepRecord
import com.example.childgrowth.data.local.VaccineRecord

internal fun buildTimeline(
    vaccines: List<VaccineRecord>,
    medications: List<MedicationRecord>,
    sleeps: List<SleepRecord>,
    outings: List<OutingRecord>,
    growths: List<GrowthRecord>,
    healths: List<HealthRecord>,
    kindergartens: List<KindergartenRecord>,
    reminders: List<ManualReminderRecord>,
    customs: List<CustomEventRecord>,
): List<TimelineItem> {
    val vaccineItems = vaccines.map {
        TimelineItem(
            id = "v-${it.id}",
            timestamp = it.administeredAt,
            badge = "疫苗",
            title = it.vaccineName,
            subtitle = "${it.doseLabel} · ${it.location.ifBlank { "未填写地点" }}",
        )
    }
    val medicationItems = medications.map {
        TimelineItem(
            id = "m-${it.id}",
            timestamp = it.takenAt,
            badge = "吃药",
            title = "${it.medicineName} ${it.dose}",
            subtitle = it.symptom.ifBlank { "已记录用药" },
        )
    }
    val sleepItems = sleeps.map {
        TimelineItem(
            id = "s-${it.id}",
            timestamp = it.wokeAt ?: it.sleptAt,
            badge = "睡眠",
            title = if (it.wokeAt == null) "${it.sleepType}进行中" else it.sleepType,
            subtitle = if (it.wokeAt == null) "等待记录起床时间" else "已记录睡醒时间",
        )
    }
    val outingItems = outings.map {
        TimelineItem(
            id = "o-${it.id}",
            timestamp = it.happenedAt,
            badge = "出游",
            title = it.title,
            subtitle = it.place.ifBlank { "未填写地点" },
        )
    }
    val growthItems = growths.map {
        TimelineItem(
            id = "g-${it.id}",
            timestamp = it.measuredAt,
            badge = "生长",
            title = "${it.heightCm} cm / ${it.weightKg} kg",
            subtitle = it.note.ifBlank { "记录了身高体重" },
        )
    }
    val healthItems = healths.map {
        TimelineItem(
            id = "h-${it.id}",
            timestamp = it.happenedAt,
            badge = "健康",
            title = it.eventType,
            subtitle = it.note.ifBlank { "记录了健康事件" },
        )
    }
    val kindergartenItems = kindergartens.map {
        TimelineItem(
            id = "k-${it.id}",
            timestamp = it.happenedAt,
            badge = "园所",
            title = "${it.eventType} · ${it.title}",
            subtitle = it.note.ifBlank { "记录了一条园所内容" },
        )
    }
    val reminderItems = reminders.map {
        TimelineItem(
            id = "r-${it.id}",
            timestamp = it.dueAt,
            badge = "提醒",
            title = it.title,
            subtitle = "${it.reminderType} · ${it.note.ifBlank { "待处理提醒" }}",
        )
    }
    val customItems = customs.map {
        TimelineItem(
            id = "c-${it.id}",
            timestamp = it.happenedAt,
            badge = it.category,
            title = it.title,
            subtitle = it.note.ifBlank { "记录了一个自定义事件" },
        )
    }
    return vaccineItems + medicationItems + sleepItems + outingItems + growthItems + healthItems + kindergartenItems + reminderItems + customItems
}
