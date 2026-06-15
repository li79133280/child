package com.example.childgrowth.data.repository

import androidx.room.withTransaction
import com.example.childgrowth.data.local.AppDatabase
import com.example.childgrowth.data.local.ChildProfile
import com.example.childgrowth.data.local.CustomEventRecord
import com.example.childgrowth.data.local.GrowthRecord
import com.example.childgrowth.data.local.HealthRecord
import com.example.childgrowth.data.local.KindergartenRecord
import com.example.childgrowth.data.local.ManualReminderRecord
import com.example.childgrowth.data.local.MedicationRecord
import com.example.childgrowth.data.local.OutingRecord
import com.example.childgrowth.data.local.SleepRecord
import com.example.childgrowth.data.local.VaccineRecord
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

internal suspend fun ensureSeedData(database: AppDatabase) {
    val childDao = database.childProfileDao()
    val vaccineDao = database.vaccineRecordDao()
    val medicationDao = database.medicationRecordDao()
    val sleepDao = database.sleepRecordDao()
    val outingDao = database.outingRecordDao()
    val growthDao = database.growthRecordDao()
    val healthDao = database.healthRecordDao()
    val kindergartenDao = database.kindergartenRecordDao()
    val reminderDao = database.manualReminderRecordDao()
    val customDao = database.customEventRecordDao()

    if (childDao.count() > 0) return

    database.withTransaction {
        val boyBirthday = LocalDate.of(2025, 4, 24)
            .atStartOfDay(ZoneId.systemDefault())
            .toInstant()
            .toEpochMilli()
        val girlBirthday = LocalDate.of(2023, 8, 9)
            .atStartOfDay(ZoneId.systemDefault())
            .toInstant()
            .toEpochMilli()

        val boyId = childDao.insert(
            ChildProfile(
                name = "小宝",
                gender = "男孩",
                birthday = boyBirthday,
                accentColor = "#2E7D6B",
                avatarLabel = "弟",
            ),
        )
        val girlId = childDao.insert(
            ChildProfile(
                name = "姐姐",
                gender = "女孩",
                birthday = girlBirthday,
                accentColor = "#D96C54",
                avatarLabel = "姐",
            ),
        )

        val now = Instant.now()
        growthDao.insert(
            GrowthRecord(
                childId = boyId,
                measuredAt = now.minusSeconds(60L * 60 * 24 * 90).toEpochMilli(),
                heightCm = 76.0,
                weightKg = 9.5,
                note = "9个月体检",
            ),
        )
        growthDao.insert(
            GrowthRecord(
                childId = boyId,
                measuredAt = now.minusSeconds(60L * 60 * 24 * 30).toEpochMilli(),
                heightCm = 79.0,
                weightKg = 10.2,
                note = "家里测量",
            ),
        )
        growthDao.insert(
            GrowthRecord(
                childId = girlId,
                measuredAt = now.minusSeconds(60L * 60 * 24 * 150).toEpochMilli(),
                heightCm = 92.0,
                weightKg = 13.5,
                note = "体检",
            ),
        )
        growthDao.insert(
            GrowthRecord(
                childId = girlId,
                measuredAt = now.minusSeconds(60L * 60 * 24 * 20).toEpochMilli(),
                heightCm = 94.5,
                weightKg = 14.0,
                note = "家里测量",
            ),
        )
        vaccineDao.insert(
            VaccineRecord(
                childId = girlId,
                vaccineName = "流感疫苗",
                doseLabel = "第 1 针",
                administeredAt = now.minusSeconds(60L * 60 * 24 * 25).toEpochMilli(),
                nextDueAt = now.plusSeconds(60L * 60 * 24 * 14).toEpochMilli(),
                location = "社区医院",
                note = "医生建议两周后复种",
            ),
        )
        medicationDao.insert(
            MedicationRecord(
                childId = boyId,
                medicineName = "布洛芬",
                symptom = "发热",
                dose = "3ml",
                takenAt = now.minusSeconds(60L * 60 * 3).toEpochMilli(),
                intervalHours = 6,
                nextDueAt = now.plusSeconds(60L * 60 * 3).toEpochMilli(),
                note = "体温高于 38.5 度时重点观察",
            ),
        )
        sleepDao.insert(
            SleepRecord(
                childId = boyId,
                sleptAt = now.minusSeconds(60L * 60 * 10).toEpochMilli(),
                wokeAt = now.minusSeconds(60L * 60 * 2).toEpochMilli(),
                sleepType = "夜睡",
                note = "",
            ),
        )
        outingDao.insert(
            OutingRecord(
                childId = boyId,
                title = "小区散步",
                place = "小区花园",
                happenedAt = now.minusSeconds(60L * 60 * 24 * 1).toEpochMilli(),
                note = "推车出去走了一圈，看了小狗",
            ),
        )
        healthDao.insert(
            HealthRecord(
                childId = boyId,
                eventType = "出牙",
                happenedAt = now.minusSeconds(60L * 60 * 24 * 2).toEpochMilli(),
                note = "下面又长了一颗牙，有点流口水",
            ),
        )
        kindergartenDao.insert(
            KindergartenRecord(
                childId = girlId,
                eventType = "早教活动",
                title = "音乐课",
                happenedAt = now.minusSeconds(60L * 60 * 24 * 4).toEpochMilli(),
                note = "跟着老师唱儿歌，很开心",
            ),
        )
        reminderDao.insert(
            ManualReminderRecord(
                childId = girlId,
                reminderType = "体检",
                title = "下周儿保复查",
                dueAt = now.plusSeconds(60L * 60 * 24 * 7).toEpochMilli(),
                note = "记得带上上次体检单",
            ),
        )
        customDao.insert(
            CustomEventRecord(
                childId = boyId,
                category = "成长瞬间",
                title = "自己收拾玩具",
                happenedAt = now.minusSeconds(60L * 60 * 20).toEpochMilli(),
                note = "把积木装回盒子里",
            ),
        )
    }
}
