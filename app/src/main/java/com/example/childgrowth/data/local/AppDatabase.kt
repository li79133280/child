package com.example.childgrowth.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [
        ChildProfile::class,
        VaccineRecord::class,
        MedicationRecord::class,
        SleepRecord::class,
        OutingRecord::class,
        GrowthRecord::class,
        HealthRecord::class,
        KindergartenRecord::class,
        ManualReminderRecord::class,
        CustomEventRecord::class,
        FeedingRecord::class,
        DiaperRecord::class,
        MilestoneRecord::class,
        TemperatureRecord::class,
        MoodRecord::class,
    ],
    version = 3,
    exportSchema = false,
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun childProfileDao(): ChildProfileDao
    abstract fun vaccineRecordDao(): VaccineRecordDao
    abstract fun medicationRecordDao(): MedicationRecordDao
    abstract fun sleepRecordDao(): SleepRecordDao
    abstract fun outingRecordDao(): OutingRecordDao
    abstract fun growthRecordDao(): GrowthRecordDao
    abstract fun healthRecordDao(): HealthRecordDao
    abstract fun kindergartenRecordDao(): KindergartenRecordDao
    abstract fun manualReminderRecordDao(): ManualReminderRecordDao
    abstract fun customEventRecordDao(): CustomEventRecordDao
    abstract fun feedingRecordDao(): FeedingRecordDao
    abstract fun diaperRecordDao(): DiaperRecordDao
    abstract fun milestoneRecordDao(): MilestoneRecordDao
    abstract fun temperatureRecordDao(): TemperatureRecordDao
    abstract fun moodRecordDao(): MoodRecordDao

    companion object {
        fun build(context: Context): AppDatabase =
            Room.databaseBuilder(context, AppDatabase::class.java, "child_growth_journal.db")
                .fallbackToDestructiveMigration()
                .build()
    }
}
