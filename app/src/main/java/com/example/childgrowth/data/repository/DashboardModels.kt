package com.example.childgrowth.data.repository

import com.example.childgrowth.data.local.ChildProfile
import com.example.childgrowth.data.local.GrowthRecord
import com.example.childgrowth.data.local.KindergartenRecord
import com.example.childgrowth.data.local.MedicationRecord
import com.example.childgrowth.data.local.OutingRecord
import com.example.childgrowth.data.local.SleepRecord
import com.example.childgrowth.data.local.VaccineRecord

data class ReminderItem(
    val id: String,
    val dueAt: Long,
    val title: String,
    val subtitle: String,
)

data class DashboardState(
    val child: ChildProfile? = null,
    val upcomingVaccine: VaccineRecord? = null,
    val nextMedication: MedicationRecord? = null,
    val openSleep: SleepRecord? = null,
    val latestSleep: SleepRecord? = null,
    val latestOuting: OutingRecord? = null,
    val latestGrowth: GrowthRecord? = null,
    val latestKindergarten: KindergartenRecord? = null,
    val upcomingReminders: List<ReminderItem> = emptyList(),
    val timeline: List<TimelineItem> = emptyList(),
)

data class TimelineItem(
    val id: String,
    val timestamp: Long,
    val badge: String,
    val title: String,
    val subtitle: String,
)
