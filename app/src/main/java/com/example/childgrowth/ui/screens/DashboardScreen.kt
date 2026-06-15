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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.childgrowth.data.local.ChildProfile
import com.example.childgrowth.data.repository.DashboardState
import com.example.childgrowth.data.repository.ReminderItem
import com.example.childgrowth.ui.RecordDialogType
import com.example.childgrowth.ui.buildStageGuide
import com.example.childgrowth.ui.components.ChildSwitcher
import com.example.childgrowth.ui.components.EmptyState
import com.example.childgrowth.ui.components.HeaderBlock
import com.example.childgrowth.ui.components.RecentTimeline
import com.example.childgrowth.ui.components.SummaryRow
import com.example.childgrowth.ui.formatDateTime
import com.example.childgrowth.ui.formatMedicationCountdown
import com.example.childgrowth.ui.formatSleepSummary
import com.example.childgrowth.ui.theme.Butter
import com.example.childgrowth.ui.theme.ButterSurface
import com.example.childgrowth.ui.theme.Sage
import com.example.childgrowth.ui.theme.SageSurface
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
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        item {
            HeaderBlock(
                title = "成长总览",
                subtitle = "聚焦幼儿园前后的健康、作息、出游、成长和园所内容",
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
            StageGuideCard(child = dashboard.child)
        }
        item {
            ReminderCenterCard(
                reminders = dashboard.upcomingReminders,
                onOpenDialog = onOpenDialog,
            )
        }
        item {
            OverviewCard(dashboard = dashboard)
        }
        item {
            RecentTimeline(title = "最近动态", items = dashboard.timeline)
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
                text = child?.let { "${it.name} 当前阶段" } ?: "当前阶段",
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            if (guide == null) {
                Text(
                    text = "请选择孩子后查看阶段建议",
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
                        text = "提醒中心",
                        style = MaterialTheme.typography.titleMedium,
                    )
                }
                Surface(
                    onClick = { onOpenDialog(RecordDialogType.Reminder) },
                    shape = RoundedCornerShape(8.dp),
                    color = MaterialTheme.colorScheme.primaryContainer,
                ) {
                    Text(
                        text = "新增提醒",
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                    )
                }
            }
            if (reminders.isEmpty()) {
                EmptyState(message = "暂时没有待提醒的事情")
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
                text = dashboard.child?.let { "${it.name} 的今日重点" } ?: "今日重点",
                style = MaterialTheme.typography.titleMedium,
                color = Sage,
            )
            SummaryRow(
                label = "下次疫苗",
                value = dashboard.upcomingVaccine?.let {
                    "${it.vaccineName} · ${formatDateTime(it.nextDueAt ?: it.administeredAt)}"
                } ?: "暂未安排",
            )
            SummaryRow(
                label = "下次吃药",
                value = dashboard.nextMedication?.let { formatMedicationCountdown(it, now) } ?: "暂未设置",
            )
            SummaryRow(
                label = "睡眠状态",
                value = when {
                    dashboard.openSleep != null -> "正在睡觉，开始于 ${formatDateTime(dashboard.openSleep.sleptAt)}"
                    dashboard.latestSleep != null -> formatSleepSummary(dashboard.latestSleep)
                    else -> "还没有睡眠记录"
                },
            )
            SummaryRow(
                label = "最近出游",
                value = dashboard.latestOuting?.let {
                    "${it.title} · ${it.place.ifBlank { "未填写地点" }}"
                } ?: "还没有出游记录",
            )
            SummaryRow(
                label = "最近园所内容",
                value = dashboard.latestKindergarten?.let {
                    "${it.eventType} · ${it.title}"
                } ?: "暂时没有园所记录",
            )
            SummaryRow(
                label = "最近身高体重",
                value = dashboard.latestGrowth?.let {
                    "${it.heightCm} cm / ${it.weightKg} kg"
                } ?: "还没有生长记录",
            )
        }
    }
}
