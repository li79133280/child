package com.example.child.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.child.R
import com.example.child.data.local.ChildProfile
import com.example.child.data.repository.DashboardState
import com.example.child.data.repository.ReminderItem
import com.example.child.ui.RecordDialogType
import com.example.child.ui.buildStageGuide
import com.example.child.ui.components.ChildSwitcher
import com.example.child.ui.components.EmptyState
import com.example.child.ui.components.HeaderBlock
import com.example.child.ui.components.RecentTimeline
import com.example.child.ui.components.SummaryRow
import com.example.child.ui.formatDateTime
import com.example.child.ui.formatMedicationCountdown
import com.example.child.ui.formatSleepSummary
import com.example.child.ui.theme.Butter
import com.example.child.ui.theme.ButterSurface
import com.example.child.ui.theme.Sage
import com.example.child.ui.theme.SageSurface
import kotlinx.coroutines.delay

@Composable
fun DashboardScreen(
    children: List<ChildProfile>,
    dashboard: DashboardState,
    selectedChildId: Long?,
    onSelectChild: (Long) -> Unit,
    onOpenDialog: (RecordDialogType) -> Unit,
    onEditChild: () -> Unit,
) {
    var visible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        visible = true
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        item {
            AnimatedVisibility(
                visible = visible,
                enter = fadeIn(animationSpec = tween(300)) +
                        slideInVertically(
                            animationSpec = tween(300),
                            initialOffsetY = { it / 10 }
                        )
            ) {
                HeaderBlock(
                    title = stringResource(R.string.dashboard_title),
                    subtitle = stringResource(R.string.dashboard_subtitle),
                )
            }
        }
        item {
            AnimatedVisibility(
                visible = visible,
                enter = fadeIn(animationSpec = tween(300, delayMillis = 50)) +
                        slideInVertically(
                            animationSpec = tween(300, delayMillis = 50),
                            initialOffsetY = { it / 10 }
                        )
            ) {
                ChildSwitcher(
                    children = children,
                    selectedChildId = selectedChildId,
                    onSelectChild = onSelectChild,
                    onEditChild = onEditChild,
                )
            }
        }
        item {
            AnimatedVisibility(
                visible = visible,
                enter = fadeIn(animationSpec = tween(300, delayMillis = 100)) +
                        slideInVertically(
                            animationSpec = tween(300, delayMillis = 100),
                            initialOffsetY = { it / 10 }
                        )
            ) {
                StageGuideCard(child = dashboard.child)
            }
        }
        item {
            AnimatedVisibility(
                visible = visible,
                enter = fadeIn(animationSpec = tween(300, delayMillis = 150)) +
                        slideInVertically(
                            animationSpec = tween(300, delayMillis = 150),
                            initialOffsetY = { it / 10 }
                        )
            ) {
                ReminderCenterCard(
                    reminders = dashboard.upcomingReminders,
                    onOpenDialog = onOpenDialog,
                )
            }
        }
        item {
            AnimatedVisibility(
                visible = visible,
                enter = fadeIn(animationSpec = tween(300, delayMillis = 200)) +
                        slideInVertically(
                            animationSpec = tween(300, delayMillis = 200),
                            initialOffsetY = { it / 10 }
                        )
            ) {
                OverviewCard(dashboard = dashboard)
            }
        }
        item {
            AnimatedVisibility(
                visible = visible,
                enter = fadeIn(animationSpec = tween(300, delayMillis = 250)) +
                        slideInVertically(
                            animationSpec = tween(300, delayMillis = 250),
                            initialOffsetY = { it / 10 }
                        )
            ) {
                RecentTimeline(title = stringResource(R.string.recent_activity), items = dashboard.timeline)
            }
        }
        item { Spacer(modifier = Modifier.height(8.dp)) }
    }
}

@Composable
private fun StageGuideCard(child: ChildProfile?) {
    val guide = remember(child) { child?.let { buildStageGuide(it.birthday) } }

    Card(
        colors = CardDefaults.cardColors(
            containerColor = ButterSurface,
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Text(
                text = child?.let { stringResource(R.string.stage_guide_title, it.name) } ?: stringResource(R.string.stage_guide_title_default),
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            if (guide == null) {
                Text(
                    text = stringResource(R.string.stage_guide_select_hint),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            } else {
                Text(
                    text = "${guide.stageName} · ${guide.title}",
                    style = MaterialTheme.typography.titleMedium,
                    color = Butter,
                )
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
private fun ReminderCenterCard(
    reminders: List<ReminderItem>,
    onOpenDialog: (RecordDialogType) -> Unit,
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Notifications,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                    )
                    Text(
                        text = stringResource(R.string.reminder_center),
                        style = MaterialTheme.typography.titleMedium,
                    )
                }
                Surface(
                    onClick = { onOpenDialog(RecordDialogType.Reminder) },
                    shape = RoundedCornerShape(8.dp),
                    color = MaterialTheme.colorScheme.primaryContainer,
                ) {
                    Text(
                        text = stringResource(R.string.new_reminder),
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                    )
                }
            }
            if (reminders.isEmpty()) {
                EmptyState(message = stringResource(R.string.no_reminders))
            } else {
                reminders.forEach { reminder ->
                    ReminderItem(reminder = reminder)
                }
            }
        }
    }
}

@Composable
private fun ReminderItem(reminder: ReminderItem) {
    Surface(
        shape = RoundedCornerShape(8.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = reminder.title,
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onSurface,
                )
                Text(
                    text = reminder.subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            Text(
                text = formatDateTime(reminder.dueAt),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.outline,
            )
        }
    }
}

@Composable
private fun OverviewCard(dashboard: DashboardState) {
    val now by produceState(initialValue = System.currentTimeMillis()) {
        while (true) {
            value = System.currentTimeMillis()
            delay(60_000)
        }
    }

    Card(
        colors = CardDefaults.cardColors(
            containerColor = SageSurface,
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Text(
                text = dashboard.child?.let { stringResource(R.string.today_focus, it.name) } ?: stringResource(R.string.today_focus_default),
                style = MaterialTheme.typography.titleMedium,
                color = Sage,
            )
            SummaryRow(
                label = stringResource(R.string.next_vaccine),
                value = dashboard.upcomingVaccine?.let {
                    "${it.vaccineName} · ${formatDateTime(it.nextDueAt ?: it.administeredAt)}"
                } ?: stringResource(R.string.not_scheduled),
            )
            SummaryRow(
                label = stringResource(R.string.next_medication),
                value = dashboard.nextMedication?.let { formatMedicationCountdown(it, now) } ?: stringResource(R.string.not_set),
            )
            SummaryRow(
                label = stringResource(R.string.sleep_status),
                value = when {
                    dashboard.openSleep != null -> stringResource(R.string.sleeping_now, formatDateTime(dashboard.openSleep.sleptAt))
                    dashboard.latestSleep != null -> formatSleepSummary(dashboard.latestSleep)
                    else -> stringResource(R.string.no_sleep_records)
                },
            )
            SummaryRow(
                label = stringResource(R.string.recent_outing),
                value = dashboard.latestOuting?.let {
                    "${it.title} · ${it.place.ifBlank { stringResource(R.string.no_place) }}"
                } ?: stringResource(R.string.no_outing_records),
            )
            SummaryRow(
                label = stringResource(R.string.recent_kindergarten),
                value = dashboard.latestKindergarten?.let {
                    "${it.eventType} · ${it.title}"
                } ?: stringResource(R.string.no_kindergarten_records),
            )
            SummaryRow(
                label = stringResource(R.string.recent_growth),
                value = dashboard.latestGrowth?.let {
                    "${it.heightCm} cm / ${it.weightKg} kg"
                } ?: stringResource(R.string.no_growth_records),
            )
        }
    }
}
