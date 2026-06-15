package com.example.childgrowth.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.childgrowth.GrowthJournalApplication
import com.example.childgrowth.data.local.ChildProfile
import com.example.childgrowth.data.local.GrowthRecord
import com.example.childgrowth.data.repository.DashboardState
import com.example.childgrowth.data.repository.GrowthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class GrowthJournalViewModel(
    application: Application,
) : AndroidViewModel(application) {
    private val repository: GrowthRepository =
        (application as GrowthJournalApplication).repository

    private val selectedChildIdFlow = MutableStateFlow<Long?>(null)

    val children: StateFlow<List<ChildProfile>> =
        repository.observeChildren()
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    val selectedChildId: StateFlow<Long?> =
        combine(children, selectedChildIdFlow) { children, selectedId ->
            when {
                children.isEmpty() -> null
                selectedId == null -> children.first().id
                children.any { it.id == selectedId } -> selectedId
                else -> children.first().id
            }
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)

    val dashboard: StateFlow<DashboardState> =
        selectedChildId
            .filterNotNull()
            .flatMapLatest { repository.observeDashboard(it) }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), DashboardState())

    val allGrowthRecords: StateFlow<List<GrowthRecord>> =
        repository.observeAllGrowthRecords()
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    init {
        viewModelScope.launch {
            repository.ensureSeedData()
        }
    }

    fun selectChild(childId: Long) {
        selectedChildIdFlow.value = childId
    }

    fun updateChild(id: Long, name: String, gender: String, birthday: Long, accentColor: String, avatarLabel: String) {
        viewModelScope.launch {
            repository.updateChild(id, name, gender, birthday, accentColor, avatarLabel)
        }
    }

    fun startSleep() {
        launchForSelectedChild { childId ->
            repository.startSleep(childId)
        }
    }

    fun endSleep() {
        launchForSelectedChild { childId ->
            repository.endSleep(childId)
        }
    }

    fun addVaccine(
        vaccineName: String,
        doseLabel: String,
        nextAfterDays: Int?,
        location: String,
        note: String,
    ) {
        launchForSelectedChild { childId ->
            repository.addVaccine(childId, vaccineName, doseLabel, nextAfterDays, location, note)
        }
    }

    fun addMedication(
        medicineName: String,
        symptom: String,
        dose: String,
        intervalHours: Int?,
        note: String,
    ) {
        launchForSelectedChild { childId ->
            repository.addMedication(childId, medicineName, symptom, dose, intervalHours, note)
        }
    }

    fun addOuting(
        title: String,
        place: String,
        note: String,
    ) {
        launchForSelectedChild { childId ->
            repository.addOuting(childId, title, place, note)
        }
    }

    fun addGrowth(
        heightCm: Double,
        weightKg: Double,
        note: String,
    ) {
        launchForSelectedChild { childId ->
            repository.addGrowth(childId, heightCm, weightKg, note)
        }
    }

    fun addHealthEvent(
        eventType: String,
        note: String,
    ) {
        launchForSelectedChild { childId ->
            repository.addHealthEvent(childId, eventType, note)
        }
    }

    fun addKindergartenRecord(
        eventType: String,
        title: String,
        note: String,
    ) {
        launchForSelectedChild { childId ->
            repository.addKindergartenRecord(childId, eventType, title, note)
        }
    }

    fun addReminder(
        reminderType: String,
        title: String,
        daysFromNow: Int?,
        note: String,
    ) {
        launchForSelectedChild { childId ->
            repository.addReminder(childId, reminderType, title, daysFromNow, note)
        }
    }

    fun addCustomEvent(
        category: String,
        title: String,
        note: String,
    ) {
        launchForSelectedChild { childId ->
            repository.addCustomEvent(childId, category, title, note)
        }
    }

    fun addFeeding(feedType: String, amount: String, note: String) {
        launchForSelectedChild { childId ->
            repository.addFeeding(childId, feedType, amount, note)
        }
    }

    fun addDiaper(diaperType: String, note: String) {
        launchForSelectedChild { childId ->
            repository.addDiaper(childId, diaperType, note)
        }
    }

    fun addMilestone(milestoneType: String, title: String, note: String) {
        launchForSelectedChild { childId ->
            repository.addMilestone(childId, milestoneType, title, note)
        }
    }

    fun addTemperature(temperature: Double, note: String) {
        launchForSelectedChild { childId ->
            repository.addTemperature(childId, temperature, note)
        }
    }

    fun addMood(moodType: String, note: String) {
        launchForSelectedChild { childId ->
            repository.addMood(childId, moodType, note)
        }
    }

    private fun launchForSelectedChild(action: suspend (Long) -> Unit) {
        val childId = selectedChildId.value ?: return
        viewModelScope.launch {
            action(childId)
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            override fun <T : androidx.lifecycle.ViewModel> create(
                modelClass: Class<T>,
                extras: CreationExtras,
            ): T {
                val application =
                    checkNotNull(extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY])
                @Suppress("UNCHECKED_CAST")
                return GrowthJournalViewModel(application) as T
            }
        }
    }
}
