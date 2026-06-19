package com.example.child.data.repository

import com.example.child.data.local.AppDatabase
import com.example.child.data.local.ChildProfile
import com.example.child.data.local.CustomEventRecord
import com.example.child.data.local.DiaperRecord
import com.example.child.data.local.FeedingRecord
import com.example.child.data.local.GrowthRecord
import com.example.child.data.local.HealthRecord
import com.example.child.data.local.KindergartenRecord
import com.example.child.data.local.ManualReminderRecord
import com.example.child.data.local.MedicationRecord
import com.example.child.data.local.MilestoneRecord
import com.example.child.data.local.MoodRecord
import com.example.child.data.local.OutingRecord
import com.example.child.data.local.SleepRecord
import com.example.child.data.local.TemperatureRecord
import com.example.child.data.local.VaccineRecord
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

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

    suspend fun ensureSeedData() = ensureSeedData(database)

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

    suspend fun addVaccine(childId: Long, vaccineName: String, doseLabel: String, nextAfterDays: Int?, location: String, note: String) {
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

    suspend fun addMedication(childId: Long, medicineName: String, symptom: String, dose: String, intervalHours: Int?, note: String) {
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

    suspend fun addOuting(childId: Long, title: String, place: String, note: String) {
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

    suspend fun addGrowth(childId: Long, heightCm: Double, weightKg: Double, note: String) {
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

    suspend fun addHealthEvent(childId: Long, eventType: String, note: String) {
        healthDao.insert(
            HealthRecord(
                childId = childId,
                eventType = eventType,
                happenedAt = System.currentTimeMillis(),
                note = note,
            ),
        )
    }

    suspend fun addKindergartenRecord(childId: Long, eventType: String, title: String, note: String) {
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

    suspend fun addReminder(childId: Long, reminderType: String, title: String, daysFromNow: Int?, note: String) {
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

    suspend fun addCustomEvent(childId: Long, category: String, title: String, note: String) {
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

    suspend fun addFeeding(childId: Long, feedType: String, amount: String, note: String) {
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

    suspend fun addDiaper(childId: Long, diaperType: String, note: String) {
        diaperDao.insert(
            DiaperRecord(
                childId = childId,
                diaperType = diaperType,
                happenedAt = System.currentTimeMillis(),
                note = note,
            ),
        )
    }

    suspend fun addMilestone(childId: Long, milestoneType: String, title: String, note: String) {
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

    suspend fun addTemperature(childId: Long, temperature: Double, note: String) {
        temperatureDao.insert(
            TemperatureRecord(
                childId = childId,
                temperature = temperature,
                happenedAt = System.currentTimeMillis(),
                note = note,
            ),
        )
    }

    suspend fun addMood(childId: Long, moodType: String, note: String) {
        moodDao.insert(
            MoodRecord(
                childId = childId,
                moodType = moodType,
                happenedAt = System.currentTimeMillis(),
                note = note,
            ),
        )
    }
}
