package com.example.childgrowth.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.BabyChangingStation
import androidx.compose.material.icons.outlined.Bedtime
import androidx.compose.material.icons.outlined.ChildCare
import androidx.compose.material.icons.outlined.EmojiEvents
import androidx.compose.material.icons.outlined.MoreHoriz
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Park
import androidx.compose.material.icons.outlined.Restaurant
import androidx.compose.material.icons.outlined.School
import androidx.compose.material.icons.outlined.Sick
import androidx.compose.material.icons.outlined.Straighten
import androidx.compose.material.icons.outlined.Thermostat
import androidx.compose.material.icons.outlined.WbSunny
import androidx.compose.material.icons.outlined.Mood
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.childgrowth.R
import com.example.childgrowth.data.local.ChildProfile
import com.example.childgrowth.data.repository.DashboardState
import com.example.childgrowth.ui.RecordDialogType
import com.example.childgrowth.ui.buildStageGuide
import com.example.childgrowth.ui.components.ChildSwitcher
import com.example.childgrowth.ui.components.HeaderBlock
import com.example.childgrowth.ui.theme.LocalRecordColors

@Composable
fun RecordsScreen(
    children: List<ChildProfile>,
    dashboard: DashboardState,
    selectedChildId: Long?,
    onSelectChild: (Long) -> Unit,
    onStartSleep: () -> Unit,
    onEndSleep: () -> Unit,
    onOpenDialog: (RecordDialogType) -> Unit,
    onEditChild: () -> Unit,
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        item {
            HeaderBlock(
                title = stringResource(R.string.records_title),
                subtitle = stringResource(R.string.records_subtitle),
            )
        }
        item {
            ChildSwitcher(
                children = children,
                selectedChildId = selectedChildId,
                onSelectChild = onSelectChild,
                onEditChild = onEditChild,
            )
        }
        item {
            RecordButtonGrid(
                hasOpenSleep = dashboard.openSleep != null,
                onStartSleep = onStartSleep,
                onEndSleep = onEndSleep,
                onOpenDialog = onOpenDialog,
            )
        }
        item {
            FocusCard(child = dashboard.child)
        }
        item { Spacer(modifier = Modifier.height(8.dp)) }
    }
}

@Composable
private fun FocusCard(child: ChildProfile?) {
    val guide = remember(child) { child?.let { buildStageGuide(it.birthday) } }

    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Text(
                text = stringResource(R.string.focus_card_title),
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            if (guide == null) {
                Text(
                    text = stringResource(R.string.focus_card_hint),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.outline,
                )
            } else {
                guide.tips.forEach { tip ->
                    Text(
                        text = "· $tip",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
        }
    }
}

@Composable
fun RecordButtonGrid(
    hasOpenSleep: Boolean,
    onStartSleep: () -> Unit,
    onEndSleep: () -> Unit,
    onOpenDialog: (RecordDialogType) -> Unit,
) {
    val recordColors = LocalRecordColors.current

    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                RecordActionCard(
                    title = stringResource(R.string.record_feeding),
                    icon = Icons.Outlined.Restaurant,
                    color = recordColors.medication,
                    surfaceColor = recordColors.medicationSurface,
                    modifier = Modifier.weight(1f),
                ) { onOpenDialog(RecordDialogType.Feeding) }
                RecordActionCard(
                    title = stringResource(R.string.record_sleep),
                    icon = Icons.Outlined.Bedtime,
                    color = recordColors.sleep,
                    surfaceColor = recordColors.sleepSurface,
                    enabled = !hasOpenSleep,
                    modifier = Modifier.weight(1f),
                    onClick = onStartSleep,
                )
            }
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                RecordActionCard(
                    title = stringResource(R.string.record_wake),
                    icon = Icons.Outlined.WbSunny,
                    color = recordColors.sleep,
                    surfaceColor = recordColors.sleepSurface,
                    enabled = hasOpenSleep,
                    modifier = Modifier.weight(1f),
                    onClick = onEndSleep,
                )
                RecordActionCard(
                    title = stringResource(R.string.record_diaper),
                    icon = Icons.Outlined.BabyChangingStation,
                    color = recordColors.outing,
                    surfaceColor = recordColors.outingSurface,
                    modifier = Modifier.weight(1f),
                ) { onOpenDialog(RecordDialogType.Diaper) }
            }
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                RecordActionCard(
                    title = stringResource(R.string.record_vaccine),
                    icon = Icons.Outlined.ChildCare,
                    color = recordColors.vaccine,
                    surfaceColor = recordColors.vaccineSurface,
                    modifier = Modifier.weight(1f),
                ) { onOpenDialog(RecordDialogType.Vaccine) }
                RecordActionCard(
                    title = stringResource(R.string.record_growth),
                    icon = Icons.Outlined.Straighten,
                    color = recordColors.growth,
                    surfaceColor = recordColors.growthSurface,
                    modifier = Modifier.weight(1f),
                ) { onOpenDialog(RecordDialogType.Growth) }
            }
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                RecordActionCard(
                    title = stringResource(R.string.record_outing),
                    icon = Icons.Outlined.Park,
                    color = recordColors.outing,
                    surfaceColor = recordColors.outingSurface,
                    modifier = Modifier.weight(1f),
                ) { onOpenDialog(RecordDialogType.Outing) }
                RecordActionCard(
                    title = stringResource(R.string.record_milestone),
                    icon = Icons.Outlined.EmojiEvents,
                    color = recordColors.milestone,
                    surfaceColor = recordColors.milestoneSurface,
                    modifier = Modifier.weight(1f),
                ) { onOpenDialog(RecordDialogType.Milestone) }
            }
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                RecordActionCard(
                    title = stringResource(R.string.record_temperature),
                    icon = Icons.Outlined.Thermostat,
                    color = recordColors.health,
                    surfaceColor = recordColors.healthSurface,
                    modifier = Modifier.weight(1f),
                ) { onOpenDialog(RecordDialogType.Temperature) }
                RecordActionCard(
                    title = stringResource(R.string.record_mood),
                    icon = Icons.Outlined.Mood,
                    color = recordColors.mood,
                    surfaceColor = recordColors.moodSurface,
                    modifier = Modifier.weight(1f),
                ) { onOpenDialog(RecordDialogType.Mood) }
            }
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                RecordActionCard(
                    title = stringResource(R.string.record_health),
                    icon = Icons.Outlined.Sick,
                    color = recordColors.health,
                    surfaceColor = recordColors.healthSurface,
                    modifier = Modifier.weight(1f),
                ) { onOpenDialog(RecordDialogType.Health) }
                RecordActionCard(
                    title = stringResource(R.string.record_reminder),
                    icon = Icons.Outlined.Notifications,
                    color = recordColors.reminder,
                    surfaceColor = recordColors.reminderSurface,
                    modifier = Modifier.weight(1f),
                ) { onOpenDialog(RecordDialogType.Reminder) }
            }
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                RecordActionCard(
                    title = stringResource(R.string.record_kindergarten),
                    icon = Icons.Outlined.School,
                    color = recordColors.kindergarten,
                    surfaceColor = recordColors.kindergartenSurface,
                    modifier = Modifier.weight(1f),
                ) { onOpenDialog(RecordDialogType.Kindergarten) }
                RecordActionCard(
                    title = stringResource(R.string.record_more),
                    icon = Icons.Outlined.MoreHoriz,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    surfaceColor = MaterialTheme.colorScheme.surfaceVariant,
                    modifier = Modifier.weight(1f),
                ) { onOpenDialog(RecordDialogType.Custom) }
            }
        }
    }
}

@Composable
private fun RecordActionCard(
    title: String,
    icon: ImageVector,
    color: androidx.compose.ui.graphics.Color,
    surfaceColor: androidx.compose.ui.graphics.Color,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    onClick: () -> Unit,
) {
    Surface(
        onClick = onClick,
        modifier = modifier.height(64.dp),
        enabled = enabled,
        shape = RoundedCornerShape(12.dp),
        color = if (enabled) surfaceColor else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 12.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(20.dp),
                tint = if (enabled) color else MaterialTheme.colorScheme.outline,
            )
            Spacer(modifier = Modifier.size(6.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.labelLarge,
                color = if (enabled) color else MaterialTheme.colorScheme.outline,
            )
        }
    }
}
