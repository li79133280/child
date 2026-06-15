package com.example.childgrowth.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ChildProfileDao {
    @Query("SELECT * FROM child_profiles ORDER BY birthday ASC")
    fun observeAll(): Flow<List<ChildProfile>>

    @Query("SELECT * FROM child_profiles WHERE id = :childId LIMIT 1")
    fun observeById(childId: Long): Flow<ChildProfile?>

    @Query("SELECT COUNT(*) FROM child_profiles")
    suspend fun count(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(profile: ChildProfile): Long

    @Query("UPDATE child_profiles SET name = :name, gender = :gender, birthday = :birthday, accentColor = :accentColor, avatarLabel = :avatarLabel WHERE id = :id")
    suspend fun update(id: Long, name: String, gender: String, birthday: Long, accentColor: String, avatarLabel: String)
}

@Dao
interface VaccineRecordDao {
    @Query("SELECT * FROM vaccine_records WHERE childId = :childId ORDER BY administeredAt DESC")
    fun observeByChild(childId: Long): Flow<List<VaccineRecord>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(record: VaccineRecord)
}

@Dao
interface MedicationRecordDao {
    @Query("SELECT * FROM medication_records WHERE childId = :childId ORDER BY takenAt DESC")
    fun observeByChild(childId: Long): Flow<List<MedicationRecord>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(record: MedicationRecord)
}

@Dao
interface SleepRecordDao {
    @Query("SELECT * FROM sleep_records WHERE childId = :childId ORDER BY sleptAt DESC")
    fun observeByChild(childId: Long): Flow<List<SleepRecord>>

    @Query("SELECT * FROM sleep_records WHERE childId = :childId AND wokeAt IS NULL ORDER BY sleptAt DESC LIMIT 1")
    fun observeOpenSleep(childId: Long): Flow<SleepRecord?>

    @Query("SELECT * FROM sleep_records WHERE childId = :childId AND wokeAt IS NULL ORDER BY sleptAt DESC LIMIT 1")
    suspend fun getOpenSleep(childId: Long): SleepRecord?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(record: SleepRecord): Long

    @Query("UPDATE sleep_records SET wokeAt = :wokeAt WHERE id = :recordId")
    suspend fun markWoke(recordId: Long, wokeAt: Long)
}

@Dao
interface OutingRecordDao {
    @Query("SELECT * FROM outing_records WHERE childId = :childId ORDER BY happenedAt DESC")
    fun observeByChild(childId: Long): Flow<List<OutingRecord>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(record: OutingRecord)
}

@Dao
interface GrowthRecordDao {
    @Query("SELECT * FROM growth_records WHERE childId = :childId ORDER BY measuredAt DESC")
    fun observeByChild(childId: Long): Flow<List<GrowthRecord>>

    @Query("SELECT * FROM growth_records ORDER BY measuredAt ASC")
    fun observeAll(): Flow<List<GrowthRecord>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(record: GrowthRecord)
}

@Dao
interface HealthRecordDao {
    @Query("SELECT * FROM health_records WHERE childId = :childId ORDER BY happenedAt DESC")
    fun observeByChild(childId: Long): Flow<List<HealthRecord>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(record: HealthRecord)
}

@Dao
interface KindergartenRecordDao {
    @Query("SELECT * FROM kindergarten_records WHERE childId = :childId ORDER BY happenedAt DESC")
    fun observeByChild(childId: Long): Flow<List<KindergartenRecord>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(record: KindergartenRecord)
}

@Dao
interface ManualReminderRecordDao {
    @Query("SELECT * FROM manual_reminder_records WHERE childId = :childId AND isDone = 0 ORDER BY dueAt ASC")
    fun observeUpcomingByChild(childId: Long): Flow<List<ManualReminderRecord>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(record: ManualReminderRecord)
}

@Dao
interface CustomEventRecordDao {
    @Query("SELECT * FROM custom_event_records WHERE childId = :childId ORDER BY happenedAt DESC")
    fun observeByChild(childId: Long): Flow<List<CustomEventRecord>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(record: CustomEventRecord)
}

@Dao
interface FeedingRecordDao {
    @Query("SELECT * FROM feeding_records WHERE childId = :childId ORDER BY happenedAt DESC")
    fun observeByChild(childId: Long): Flow<List<FeedingRecord>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(record: FeedingRecord)
}

@Dao
interface DiaperRecordDao {
    @Query("SELECT * FROM diaper_records WHERE childId = :childId ORDER BY happenedAt DESC")
    fun observeByChild(childId: Long): Flow<List<DiaperRecord>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(record: DiaperRecord)
}

@Dao
interface MilestoneRecordDao {
    @Query("SELECT * FROM milestone_records WHERE childId = :childId ORDER BY happenedAt DESC")
    fun observeByChild(childId: Long): Flow<List<MilestoneRecord>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(record: MilestoneRecord)
}

@Dao
interface TemperatureRecordDao {
    @Query("SELECT * FROM temperature_records WHERE childId = :childId ORDER BY happenedAt DESC")
    fun observeByChild(childId: Long): Flow<List<TemperatureRecord>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(record: TemperatureRecord)
}

@Dao
interface MoodRecordDao {
    @Query("SELECT * FROM mood_records WHERE childId = :childId ORDER BY happenedAt DESC")
    fun observeByChild(childId: Long): Flow<List<MoodRecord>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(record: MoodRecord)
}
