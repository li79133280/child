package com.example.child.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/**
 * Room 数据库定义
 *
 * 迁移策略：
 * 1. 每次 schema 变更时，在 Migrations.kt 中添加新的 Migration 对象
 * 2. 版本号 +1，并在 all() 数组中注册
 * 3. fallbackToDestructiveMigration() 仅作为最后手段，正式环境应移除
 *
 * 添加新实体或字段时：
 * 1. 修改 Entity 类
 * 2. 增加 version 号
 * 3. 在 Migrations.kt 添加对应的 Migration
 * 4. 在 all() 数组中注册
 */
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
    exportSchema = true,
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
                .addMigrations(*Migrations.all())
                .fallbackToDestructiveMigration()
                .build()
    }
}
