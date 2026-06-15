package com.example.childgrowth.data.repository

import androidx.room.withTransaction
import com.example.childgrowth.data.local.AppDatabase
import com.example.childgrowth.data.local.ChildProfile
import com.example.childgrowth.data.local.CustomEventRecord
import com.example.childgrowth.data.local.DiaperRecord
import com.example.childgrowth.data.local.FeedingRecord
import com.example.childgrowth.data.local.GrowthRecord
import com.example.childgrowth.data.local.HealthRecord
import com.example.childgrowth.data.local.KindergartenRecord
import com.example.childgrowth.data.local.ManualReminderRecord
import com.example.childgrowth.data.local.MedicationRecord
import com.example.childgrowth.data.local.MilestoneRecord
import com.example.childgrowth.data.local.MoodRecord
import com.example.childgrowth.data.local.OutingRecord
import com.example.childgrowth.data.local.SleepRecord
import com.example.childgrowth.data.local.TemperatureRecord
import com.example.childgrowth.data.local.VaccineRecord
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

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

class GrowthRepository(
    private val database: AppDatabase,
) {
    private val childDao = database.childProfileDao()
    private val vaccineDao = database.vaccineRecordDao()
    private val medicationDao = database.medicationRecordDao()
    private val sleepDao = database.sleepRecordDao()
    private val outingDao = database.outingRecordDao()
    private val growthDao = database.growthRecordDao()
    private val healthDao = database.healthRecordDao()
    private val kindergartenDao = database.kindergartenRecordDao()
    private val reminderDao = database.manualReminderRecordDao()
    private val customDao = database.customEventRecordDao()
    private val feedingDao = database.feedingRecordDao()
    private val diaperDao = database.diaperRecordDao()
    private val milestoneDao = database.milestoneRecordDao()
    private val temperatureDao = database.temperatureRecordDao()
    private val moodDao = database.moodRecordDao()

    fun observeChildren(): Flow<List<ChildProfile>> = childDao.observeAll()

    fun observeAllGrowthRecords(): Flow<List<GrowthRecord>> = growthDao.observeAll()

    suspend fun updateChild(id: Long, name: String, gender: String, birthday: Long, accentColor: String, avatarLabel: String) {
        childDao.update(id, name, gender, birthday, accentColor, avatarLabel)
    }

    fun observeDashboard(childId: Long): Flow<DashboardState> =
        combine(
            childDao.observeById(childId),
            vaccineDao.observeByChild(childId),
            medicationDao.observeByChild(childId),
            sleepDao.observeByChild(childId),
            sleepDao.observeOpenSleep(childId),
            outingDao.observeByChild(childId),
            growthDao.observeByChild(childId),
            healthDao.observeByChild(childId),
            kindergartenDao.observeByChild(childId),
            reminderDao.observeUpcomingByChild(childId),
            customDao.observeByChild(childId),
        ) { values ->
            @Suppress("UNCHECKED_CAST")
            val child = values[0] as ChildProfile?
            val vaccines = values[1] as List<VaccineRecord>
            val medications = values[2] as List<MedicationRecord>
            val sleeps = values[3] as List<SleepRecord>
            val openSleep = values[4] as SleepRecord?
            val outings = values[5] as List<OutingRecord>
            val growths = values[6] as List<GrowthRecord>
            val healths = values[7] as List<HealthRecord>
            val kindergartens = values[8] as List<KindergartenRecord>
            val reminders = values[9] as List<ManualReminderRecord>
            val customs = values[10] as List<CustomEventRecord>
            val now = System.currentTimeMillis()
            val upcomingVaccine = vaccines
                .filter { it.nextDueAt != null && it.nextDueAt >= now }
                .minByOrNull { it.nextDueAt ?: Long.MAX_VALUE }
            val nextMedication = medications
                .filter { it.nextDueAt != null && it.nextDueAt >= now }
                .minByOrNull { it.nextDueAt ?: Long.MAX_VALUE }

            DashboardState(
                child = child,
                upcomingVaccine = upcomingVaccine,
                nextMedication = nextMedication,
                openSleep = openSleep,
                latestSleep = sleeps.firstOrNull { it.wokeAt != null },
                latestOuting = outings.firstOrNull(),
                latestGrowth = growths.firstOrNull(),
                latestKindergarten = kindergartens.firstOrNull(),
                upcomingReminders = buildReminderList(upcomingVaccine, nextMedication, reminders).take(5),
                timeline = buildTimeline(
                    vaccines = vaccines,
                    medications = medications,
                    sleeps = sleeps,
                    outings = outings,
                    growths = growths,
                    healths = healths,
                    kindergartens = kindergartens,
                    reminders = reminders,
                    customs = customs,
                ).sortedByDescending { it.timestamp }.take(20),
            )
        }

    suspend fun ensureSeedData() {
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

    suspend fun startSleep(childId: Long, type: String = "夜睡") {
        val openSleep = sleepDao.getOpenSleep(childId)
        if (openSleep == null) {
            sleepDao.insert(
                SleepRecord(
                    childId = childId,
                    sleptAt = System.currentTimeMillis(),
                    wokeAt = null,
                    sleepType = type,
                    note = "",
                ),
            )
        }
    }

    suspend fun endSleep(childId: Long) {
        val openSleep = sleepDao.getOpenSleep(childId) ?: return
        sleepDao.markWoke(openSleep.id, System.currentTimeMillis())
    }

    suspend fun addVaccine(
        childId: Long,
        vaccineName: String,
        doseLabel: String,
        nextAfterDays: Int?,
        location: String,
        note: String,
    ) {
        val administeredAt = System.currentTimeMillis()
        vaccineDao.insert(
            VaccineRecord(
                childId = childId,
                vaccineName = vaccineName,
                doseLabel = doseLabel,
                administeredAt = administeredAt,
                nextDueAt = nextAfterDays?.let { administeredAt + it * 24L * 60L * 60L * 1000L },
                location = location,
                note = note,
            ),
        )
    }

    suspend fun addMedication(
        childId: Long,
        medicineName: String,
        symptom: String,
        dose: String,
        intervalHours: Int?,
        note: String,
    ) {
        val takenAt = System.currentTimeMillis()
        medicationDao.insert(
            MedicationRecord(
                childId = childId,
                medicineName = medicineName,
                symptom = symptom,
                dose = dose,
                takenAt = takenAt,
                intervalHours = intervalHours,
                nextDueAt = intervalHours?.let { takenAt + it * 60L * 60L * 1000L },
                note = note,
            ),
        )
    }

    suspend fun addOuting(
        childId: Long,
        title: String,
        place: String,
        note: String,
    ) {
        outingDao.insert(
            OutingRecord(
                childId = childId,
                title = title,
                place = place,
                happenedAt = System.currentTimeMillis(),
                note = note,
            ),
        )
    }

    suspend fun addGrowth(
        childId: Long,
        heightCm: Double,
        weightKg: Double,
        note: String,
    ) {
        growthDao.insert(
            GrowthRecord(
                childId = childId,
                measuredAt = System.currentTimeMillis(),
                heightCm = heightCm,
                weightKg = weightKg,
                note = note,
            ),
        )
    }

    suspend fun addHealthEvent(
        childId: Long,
        eventType: String,
        note: String,
    ) {
        healthDao.insert(
            HealthRecord(
                childId = childId,
                eventType = eventType,
                happenedAt = System.currentTimeMillis(),
                note = note,
            ),
        )
    }

    suspend fun addKindergartenRecord(
        childId: Long,
        eventType: String,
        title: String,
        note: String,
    ) {
        kindergartenDao.insert(
            KindergartenRecord(
                childId = childId,
                eventType = eventType,
                title = title,
                happenedAt = System.currentTimeMillis(),
                note = note,
            ),
        )
    }

    suspend fun addReminder(
        childId: Long,
        reminderType: String,
        title: String,
        daysFromNow: Int?,
        note: String,
    ) {
        val dueAt = System.currentTimeMillis() + (daysFromNow ?: 0) * 24L * 60L * 60L * 1000L
        reminderDao.insert(
            ManualReminderRecord(
                childId = childId,
                reminderType = reminderType,
                title = title,
                dueAt = dueAt,
                note = note,
            ),
        )
    }

    suspend fun addCustomEvent(
        childId: Long,
        category: String,
        title: String,
        note: String,
    ) {
        customDao.insert(
            CustomEventRecord(
                childId = childId,
                category = category,
                title = title,
                happenedAt = System.currentTimeMillis(),
                note = note,
            ),
        )
    }

    suspend fun addFeeding(
        childId: Long,
        feedType: String,
        amount: String,
        note: String,
    ) {
        feedingDao.insert(
            FeedingRecord(
                childId = childId,
                feedType = feedType,
                amount = amount,
                happenedAt = System.currentTimeMillis(),
                note = note,
            ),
        )
    }

    suspend fun addDiaper(
        childId: Long,
        diaperType: String,
        note: String,
    ) {
        diaperDao.insert(
            DiaperRecord(
                childId = childId,
                diaperType = diaperType,
                happenedAt = System.currentTimeMillis(),
                note = note,
            ),
        )
    }

    suspend fun addMilestone(
        childId: Long,
        milestoneType: String,
        title: String,
        note: String,
    ) {
        milestoneDao.insert(
            MilestoneRecord(
                childId = childId,
                milestoneType = milestoneType,
                title = title,
                happenedAt = System.currentTimeMillis(),
                note = note,
            ),
        )
    }

    suspend fun addTemperature(
        childId: Long,
        temperature: Double,
        note: String,
    ) {
        temperatureDao.insert(
            TemperatureRecord(
                childId = childId,
                temperature = temperature,
                happenedAt = System.currentTimeMillis(),
                note = note,
            ),
        )
    }

    suspend fun addMood(
        childId: Long,
        moodType: String,
        note: String,
    ) {
        moodDao.insert(
            MoodRecord(
                childId = childId,
                moodType = moodType,
                happenedAt = System.currentTimeMillis(),
                note = note,
            ),
        )
    }

    private fun buildReminderList(
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

    private fun buildTimeline(
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
}
