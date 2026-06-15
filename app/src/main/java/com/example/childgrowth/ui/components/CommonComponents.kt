package com.example.childgrowth.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.childgrowth.data.local.ChildProfile
import com.example.childgrowth.data.repository.TimelineItem
import com.example.childgrowth.ui.formatAge
import com.example.childgrowth.ui.formatDateTime
import com.example.childgrowth.ui.parseAccentColor
import com.example.childgrowth.ui.theme.LocalRecordColors

@Composable
fun HeaderBlock(title: String, subtitle: String) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(
            text = title,
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.onBackground,
        )
        Text(
            text = subtitle,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@Composable
fun ChildSwitcher(
    children: List<ChildProfile>,
    selectedChildId: Long?,
    onSelectChild: (Long) -> Unit,
    onEditChild: () -> Unit,
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            Text(
                text = "孩子切换",
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            children.forEach { child ->
                val selected = child.id == selectedChildId
                val bgColor by animateColorAsState(
                    targetValue = if (selected) parseAccentColor(child.accentColor).copy(alpha = 0.12f)
                    else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                    animationSpec = tween(200),
                    label = "childBg",
                )

                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .clickable { onSelectChild(child.id) },
                    color = bgColor,
                    shape = RoundedCornerShape(16.dp),
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .clip(CircleShape)
                                .background(parseAccentColor(child.accentColor)),
                            contentAlignment = Alignment.Center,
                        ) {
                            Text(
                                text = child.avatarLabel,
                                color = Color.White,
                                style = MaterialTheme.typography.titleMedium,
                            )
                        }
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = child.name,
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onSurface,
                            )
                            Text(
                                text = "${child.gender} · ${formatAge(child.birthday)}",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                        }
                        if (selected) {
                            Surface(
                                onClick = onEditChild,
                                shape = RoundedCornerShape(8.dp),
                                color = MaterialTheme.colorScheme.primaryContainer,
                            ) {
                                Text(
                                    text = "编辑",
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                    style = MaterialTheme.typography.labelMedium,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SummaryRow(label: String, value: String) {
    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.primary,
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface,
        )
    }
}

@Composable
fun RecentTimeline(
    title: String,
    items: List<TimelineItem>,
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
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
            )
            if (items.isEmpty()) {
                EmptyState(message = "还没有记录，可以先从睡眠、吃药或身高体重开始。")
            } else {
                items.forEachIndexed { index, item ->
                    TimelineRow(item = item)
                    if (index != items.lastIndex) {
                        HorizontalDivider(
                            modifier = Modifier.padding(vertical = 4.dp),
                            color = MaterialTheme.colorScheme.outlineVariant,
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun TimelineRow(item: TimelineItem) {
    val recordColors = LocalRecordColors.current
    val badgeColor = when (item.badge) {
        "疫苗" -> recordColors.vaccine
        "吃药" -> recordColors.medication
        "睡眠" -> recordColors.sleep
        "出游" -> recordColors.outing
        "生长" -> recordColors.growth
        "健康" -> recordColors.health
        "园所" -> recordColors.kindergarten
        "提醒" -> recordColors.reminder
        else -> MaterialTheme.colorScheme.primary
    }
    val badgeSurfaceColor = when (item.badge) {
        "疫苗" -> recordColors.vaccineSurface
        "吃药" -> recordColors.medicationSurface
        "睡眠" -> recordColors.sleepSurface
        "出游" -> recordColors.outingSurface
        "生长" -> recordColors.growthSurface
        "健康" -> recordColors.healthSurface
        "园所" -> recordColors.kindergartenSurface
        "提醒" -> recordColors.reminderSurface
        else -> MaterialTheme.colorScheme.primaryContainer
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.Top,
    ) {
        Box(
            modifier = Modifier
                .size(8.dp)
                .clip(CircleShape)
                .background(badgeColor)
                .padding(top = 6.dp),
        )
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(2.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = item.title,
                    style = MaterialTheme.typography.titleSmall,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f),
                )
                Spacer(Modifier.width(8.dp))
                Surface(
                    shape = RoundedCornerShape(4.dp),
                    color = badgeSurfaceColor,
                ) {
                    Text(
                        text = item.badge,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                        style = MaterialTheme.typography.labelSmall,
                        color = badgeColor,
                    )
                }
            }
            Text(
                text = item.subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )
            Text(
                text = formatDateTime(item.timestamp),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.outline,
            )
        }
    }
}

@Composable
fun EmptyState(message: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 24.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = message,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}
