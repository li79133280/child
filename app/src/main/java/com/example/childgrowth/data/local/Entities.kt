package com.example.childgrowth.data.local

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "child_profiles")
data class ChildProfile(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val gender: String,
    val birthday: Long,
    val accentColor: String,
    val avatarLabel: String,
)

@Entity(
    tableName = "vaccine_records",
    foreignKeys = [
        ForeignKey(
            entity = ChildProfile::class,
            parentColumns = ["id"],
            childColumns = ["childId"],
            onDelete = ForeignKey.CASCADE,
        ),
    ],
    indices = [Index("childId")],
)
data class VaccineRecord(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val childId: Long,
    val vaccineName: String,
    val doseLabel: String,
    val administeredAt: Long,
    val nextDueAt: Long?,
    val location: String,
    val note: String,
)

@Entity(
    tableName = "medication_records",
    foreignKeys = [
        ForeignKey(
            entity = ChildProfile::class,
            parentColumns = ["id"],
            childColumns = ["childId"],
            onDelete = ForeignKey.CASCADE,
        ),
    ],
    indices = [Index("childId")],
)
data class MedicationRecord(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val childId: Long,
    val medicineName: String,
    val symptom: String,
    val dose: String,
    val takenAt: Long,
    val intervalHours: Int?,
    val nextDueAt: Long?,
    val note: String,
)

@Entity(
    tableName = "sleep_records",
    foreignKeys = [
        ForeignKey(
            entity = ChildProfile::class,
            parentColumns = ["id"],
            childColumns = ["childId"],
            onDelete = ForeignKey.CASCADE,
        ),
    ],
    indices = [Index("childId")],
)
data class SleepRecord(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val childId: Long,
    val sleptAt: Long,
    val wokeAt: Long?,
    val sleepType: String,
    val note: String,
)

@Entity(
    tableName = "outing_records",
    foreignKeys = [
        ForeignKey(
            entity = ChildProfile::class,
            parentColumns = ["id"],
            childColumns = ["childId"],
            onDelete = ForeignKey.CASCADE,
        ),
    ],
    indices = [Index("childId")],
)
data class OutingRecord(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val childId: Long,
    val title: String,
    val place: String,
    val happenedAt: Long,
    val note: String,
)

@Entity(
    tableName = "growth_records",
    foreignKeys = [
        ForeignKey(
            entity = ChildProfile::class,
            parentColumns = ["id"],
            childColumns = ["childId"],
            onDelete = ForeignKey.CASCADE,
        ),
    ],
    indices = [Index("childId")],
)
data class GrowthRecord(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val childId: Long,
    val measuredAt: Long,
    val heightCm: Double,
    val weightKg: Double,
    val note: String,
)

@Entity(
    tableName = "health_records",
    foreignKeys = [
        ForeignKey(
            entity = ChildProfile::class,
            parentColumns = ["id"],
            childColumns = ["childId"],
            onDelete = ForeignKey.CASCADE,
        ),
    ],
    indices = [Index("childId")],
)
data class HealthRecord(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val childId: Long,
    val eventType: String,
    val happenedAt: Long,
    val note: String,
)

@Entity(
    tableName = "kindergarten_records",
    foreignKeys = [
        ForeignKey(
            entity = ChildProfile::class,
            parentColumns = ["id"],
            childColumns = ["childId"],
            onDelete = ForeignKey.CASCADE,
        ),
    ],
    indices = [Index("childId")],
)
data class KindergartenRecord(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val childId: Long,
    val eventType: String,
    val title: String,
    val happenedAt: Long,
    val note: String,
)

@Entity(
    tableName = "manual_reminder_records",
    foreignKeys = [
        ForeignKey(
            entity = ChildProfile::class,
            parentColumns = ["id"],
            childColumns = ["childId"],
            onDelete = ForeignKey.CASCADE,
        ),
    ],
    indices = [Index("childId")],
)
data class ManualReminderRecord(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val childId: Long,
    val reminderType: String,
    val title: String,
    val dueAt: Long,
    val note: String,
    val isDone: Boolean = false,
)

@Entity(
    tableName = "custom_event_records",
    foreignKeys = [
        ForeignKey(
            entity = ChildProfile::class,
            parentColumns = ["id"],
            childColumns = ["childId"],
            onDelete = ForeignKey.CASCADE,
        ),
    ],
    indices = [Index("childId")],
)
data class CustomEventRecord(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val childId: Long,
    val category: String,
    val title: String,
    val happenedAt: Long,
    val note: String,
)

@Entity(
    tableName = "feeding_records",
    foreignKeys = [
        ForeignKey(
            entity = ChildProfile::class,
            parentColumns = ["id"],
            childColumns = ["childId"],
            onDelete = ForeignKey.CASCADE,
        ),
    ],
    indices = [Index("childId")],
)
data class FeedingRecord(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val childId: Long,
    val feedType: String,
    val amount: String,
    val happenedAt: Long,
    val note: String,
)

@Entity(
    tableName = "diaper_records",
    foreignKeys = [
        ForeignKey(
            entity = ChildProfile::class,
            parentColumns = ["id"],
            childColumns = ["childId"],
            onDelete = ForeignKey.CASCADE,
        ),
    ],
    indices = [Index("childId")],
)
data class DiaperRecord(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val childId: Long,
    val diaperType: String,
    val happenedAt: Long,
    val note: String,
)

@Entity(
    tableName = "milestone_records",
    foreignKeys = [
        ForeignKey(
            entity = ChildProfile::class,
            parentColumns = ["id"],
            childColumns = ["childId"],
            onDelete = ForeignKey.CASCADE,
        ),
    ],
    indices = [Index("childId")],
)
data class MilestoneRecord(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val childId: Long,
    val milestoneType: String,
    val title: String,
    val happenedAt: Long,
    val note: String,
)

@Entity(
    tableName = "temperature_records",
    foreignKeys = [
        ForeignKey(
            entity = ChildProfile::class,
            parentColumns = ["id"],
            childColumns = ["childId"],
            onDelete = ForeignKey.CASCADE,
        ),
    ],
    indices = [Index("childId")],
)
data class TemperatureRecord(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val childId: Long,
    val temperature: Double,
    val happenedAt: Long,
    val note: String,
)

@Entity(
    tableName = "mood_records",
    foreignKeys = [
        ForeignKey(
            entity = ChildProfile::class,
            parentColumns = ["id"],
            childColumns = ["childId"],
            onDelete = ForeignKey.CASCADE,
        ),
    ],
    indices = [Index("childId")],
)
data class MoodRecord(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val childId: Long,
    val moodType: String,
    val happenedAt: Long,
    val note: String,
)
