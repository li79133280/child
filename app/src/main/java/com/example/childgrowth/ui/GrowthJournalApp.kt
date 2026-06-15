package com.example.childgrowth.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AutoGraph
import androidx.compose.material.icons.outlined.ChildCare
import androidx.compose.material.icons.automirrored.outlined.EventNote
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.childgrowth.data.local.ChildProfile
import com.example.childgrowth.data.repository.DashboardState
import com.example.childgrowth.ui.components.CustomEventDialog
import com.example.childgrowth.ui.components.DiaperDialog
import com.example.childgrowth.ui.components.EditChildDialog
import com.example.childgrowth.ui.components.FeedingDialog
import com.example.childgrowth.ui.components.MilestoneDialog
import com.example.childgrowth.ui.components.MoodDialog
import com.example.childgrowth.ui.components.TemperatureDialog
import com.example.childgrowth.ui.components.GrowthDialog
import com.example.childgrowth.ui.components.HealthDialog
import com.example.childgrowth.ui.components.KindergartenDialog
import com.example.childgrowth.ui.components.MedicationDialog
import com.example.childgrowth.ui.components.OutingDialog
import com.example.childgrowth.ui.components.ReminderDialog
import com.example.childgrowth.ui.components.VaccineDialog
import com.example.childgrowth.ui.screens.DashboardScreen
import com.example.childgrowth.ui.screens.GrowthScreen
import com.example.childgrowth.ui.screens.HistoryScreen
import com.example.childgrowth.ui.screens.RecordsScreen

enum class AppTab(val label: String) {
    Home("首页"),
    Records("记录"),
    History("历史"),
    Growth("曲线"),
}

enum class RecordDialogType {
    Vaccine,
    Medication,
    Outing,
    Growth,
    Health,
    Kindergarten,
    Reminder,
    Custom,
    EditChild,
    Feeding,
    Diaper,
    Milestone,
    Temperature,
    Mood,
}

@Composable
fun GrowthJournalApp(viewModel: GrowthJournalViewModel) {
    val children by viewModel.children.collectAsStateWithLifecycle()
    val dashboard by viewModel.dashboard.collectAsStateWithLifecycle()
    val selectedChildId by viewModel.selectedChildId.collectAsStateWithLifecycle()
    val allGrowthRecords by viewModel.allGrowthRecords.collectAsStateWithLifecycle()

    var currentTab by remember { mutableStateOf(AppTab.Home) }
    var activeDialog by remember { mutableStateOf<RecordDialogType?>(null) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = currentTab == AppTab.Home,
                    onClick = { currentTab = AppTab.Home },
                    icon = { Icon(Icons.Outlined.Home, contentDescription = null) },
                    label = { Text(AppTab.Home.label) },
                )
                NavigationBarItem(
                    selected = currentTab == AppTab.Records,
                    onClick = { currentTab = AppTab.Records },
                    icon = { Icon(Icons.Outlined.ChildCare, contentDescription = null) },
                    label = { Text(AppTab.Records.label) },
                )
                NavigationBarItem(
                    selected = currentTab == AppTab.History,
                    onClick = { currentTab = AppTab.History },
                    icon = { Icon(Icons.AutoMirrored.Outlined.EventNote, contentDescription = null) },
                    label = { Text(AppTab.History.label) },
                )
                NavigationBarItem(
                    selected = currentTab == AppTab.Growth,
                    onClick = { currentTab = AppTab.Growth },
                    icon = { Icon(Icons.Outlined.AutoGraph, contentDescription = null) },
                    label = { Text(AppTab.Growth.label) },
                )
            }
        },
    ) { innerPadding ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            color = androidx.compose.material3.MaterialTheme.colorScheme.background,
        ) {
            when (currentTab) {
                AppTab.Home -> DashboardScreen(
                    children = children,
                    dashboard = dashboard,
                    selectedChildId = selectedChildId,
                    onSelectChild = viewModel::selectChild,
                    onOpenDialog = { activeDialog = it },
                    onEditChild = { activeDialog = RecordDialogType.EditChild },
                )

                AppTab.Records -> RecordsScreen(
                    children = children,
                    dashboard = dashboard,
                    selectedChildId = selectedChildId,
                    onSelectChild = viewModel::selectChild,
                    onStartSleep = viewModel::startSleep,
                    onEndSleep = viewModel::endSleep,
                    onOpenDialog = { activeDialog = it },
                    onEditChild = { activeDialog = RecordDialogType.EditChild },
                )

                AppTab.History -> HistoryScreen(
                    children = children,
                    dashboard = dashboard,
                    selectedChildId = selectedChildId,
                    onSelectChild = viewModel::selectChild,
                    onEditChild = { activeDialog = RecordDialogType.EditChild },
                )

                AppTab.Growth -> GrowthScreen(
                    children = children,
                    selectedChildId = selectedChildId,
                    dashboard = dashboard,
                    allGrowthRecords = allGrowthRecords,
                    onSelectChild = viewModel::selectChild,
                    onEditChild = { activeDialog = RecordDialogType.EditChild },
                )
            }
        }
    }

    when (activeDialog) {
        RecordDialogType.Vaccine -> VaccineDialog(
            onDismiss = { activeDialog = null },
            onConfirm = { vaccineName, doseLabel, nextDays, location, note ->
                viewModel.addVaccine(vaccineName, doseLabel, nextDays, location, note)
                activeDialog = null
            },
        )

        RecordDialogType.Medication -> MedicationDialog(
            onDismiss = { activeDialog = null },
            onConfirm = { medicineName, symptom, dose, intervalHours, note ->
                viewModel.addMedication(medicineName, symptom, dose, intervalHours, note)
                activeDialog = null
            },
        )

        RecordDialogType.Outing -> OutingDialog(
            onDismiss = { activeDialog = null },
            onConfirm = { title, place, note ->
                viewModel.addOuting(title, place, note)
                activeDialog = null
            },
        )

        RecordDialogType.Growth -> GrowthDialog(
            onDismiss = { activeDialog = null },
            onConfirm = { height, weight, note ->
                viewModel.addGrowth(height, weight, note)
                activeDialog = null
            },
        )

        RecordDialogType.Health -> HealthDialog(
            onDismiss = { activeDialog = null },
            onConfirm = { eventType, note ->
                viewModel.addHealthEvent(eventType, note)
                activeDialog = null
            },
        )

        RecordDialogType.Kindergarten -> KindergartenDialog(
            onDismiss = { activeDialog = null },
            onConfirm = { eventType, title, note ->
                viewModel.addKindergartenRecord(eventType, title, note)
                activeDialog = null
            },
        )

        RecordDialogType.Reminder -> ReminderDialog(
            onDismiss = { activeDialog = null },
            onConfirm = { reminderType, title, days, note ->
                viewModel.addReminder(reminderType, title, days, note)
                activeDialog = null
            },
        )

        RecordDialogType.Custom -> CustomEventDialog(
            onDismiss = { activeDialog = null },
            onConfirm = { category, title, note ->
                viewModel.addCustomEvent(category, title, note)
                activeDialog = null
            },
        )

        RecordDialogType.EditChild -> {
            val child = children.find { it.id == selectedChildId }
            if (child != null) {
                EditChildDialog(
                    initialName = child.name,
                    initialGender = child.gender,
                    initialBirthday = child.birthday,
                    initialAccentColor = child.accentColor,
                    initialAvatarLabel = child.avatarLabel,
                    onDismiss = { activeDialog = null },
                    onConfirm = { name, gender, birthday, accentColor, avatarLabel ->
                        viewModel.updateChild(child.id, name, gender, birthday, accentColor, avatarLabel)
                        activeDialog = null
                    },
                )
            }
        }

        RecordDialogType.Feeding -> FeedingDialog(
            onDismiss = { activeDialog = null },
            onConfirm = { feedType, amount, note ->
                viewModel.addFeeding(feedType, amount, note)
                activeDialog = null
            },
        )

        RecordDialogType.Diaper -> DiaperDialog(
            onDismiss = { activeDialog = null },
            onConfirm = { diaperType, note ->
                viewModel.addDiaper(diaperType, note)
                activeDialog = null
            },
        )

        RecordDialogType.Milestone -> MilestoneDialog(
            onDismiss = { activeDialog = null },
            onConfirm = { milestoneType, title, note ->
                viewModel.addMilestone(milestoneType, title, note)
                activeDialog = null
            },
        )

        RecordDialogType.Temperature -> TemperatureDialog(
            onDismiss = { activeDialog = null },
            onConfirm = { temperature, note ->
                viewModel.addTemperature(temperature, note)
                activeDialog = null
            },
        )

        RecordDialogType.Mood -> MoodDialog(
            onDismiss = { activeDialog = null },
            onConfirm = { moodType, note ->
                viewModel.addMood(moodType, note)
                activeDialog = null
            },
        )

        null -> Unit
    }
}
