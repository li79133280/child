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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.child.R
import com.example.child.data.local.ChildProfile
import com.example.child.data.local.GrowthRecord
import com.example.child.data.repository.DashboardState
import com.example.child.ui.components.ChildSwitcher
import com.example.child.ui.components.GrowthChart
import com.example.child.ui.components.GrowthMetric
import com.example.child.ui.components.HeaderBlock
import com.example.child.ui.components.SummaryRow
import com.example.child.ui.formatDateTime
import com.example.child.ui.theme.Sage
import com.example.child.ui.theme.SageSurface

@Composable
fun GrowthScreen(
    children: List<ChildProfile>,
    selectedChildId: Long?,
    dashboard: DashboardState,
    allGrowthRecords: List<GrowthRecord>,
    onSelectChild: (Long) -> Unit,
    onEditChild: () -> Unit,
) {
    var compareMode by remember { mutableStateOf(true) }
    var metric by remember { mutableStateOf(GrowthMetric.Height) }
    var visible by remember { mutableStateOf(false) }

    LaunchedEffect(children.size) {
        if (children.size <= 1) compareMode = false
    }

    LaunchedEffect(Unit) {
        visible = true
    }

    val activeChildIds = remember(children, selectedChildId, compareMode) {
        if (compareMode) children.map { it.id }.toSet() else setOfNotNull(selectedChildId)
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
                    title = stringResource(R.string.growth_title),
                    subtitle = stringResource(R.string.growth_subtitle),
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
                GrowthChartCard(
                    children = children,
                    records = allGrowthRecords,
                    metric = metric,
                    activeChildIds = activeChildIds,
                    compareMode = compareMode,
                    onCompareModeChange = { compareMode = it },
                    onMetricChange = { metric = it },
                )
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
                LatestGrowthSummary(
                    children = children,
                    records = allGrowthRecords,
                    activeChildIds = activeChildIds,
                )
            }
        }
        item {
            OverviewCard(dashboard = dashboard)
        }
        item { Spacer(modifier = Modifier.height(8.dp)) }
    }
}

@Composable
private fun GrowthChartCard(
    children: List<ChildProfile>,
    records: List<GrowthRecord>,
    metric: GrowthMetric,
    activeChildIds: Set<Long>,
    compareMode: Boolean,
    onCompareModeChange: (Boolean) -> Unit,
    onMetricChange: (GrowthMetric) -> Unit,
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = stringResource(R.string.growth_trend),
                    style = MaterialTheme.typography.titleMedium,
                )
                if (children.size > 1) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            text = stringResource(R.string.compare_mode),
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                        Switch(
                            checked = compareMode,
                            onCheckedChange = onCompareModeChange,
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = MaterialTheme.colorScheme.primary,
                                checkedTrackColor = MaterialTheme.colorScheme.primaryContainer,
                            ),
                        )
                    }
                }
            }
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                FilterChip(
                    selected = metric == GrowthMetric.Height,
                    onClick = { onMetricChange(GrowthMetric.Height) },
                    label = { Text(stringResource(R.string.height)) },
                    shape = RoundedCornerShape(8.dp),
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                        selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    ),
                )
                FilterChip(
                    selected = metric == GrowthMetric.Weight,
                    onClick = { onMetricChange(GrowthMetric.Weight) },
                    label = { Text(stringResource(R.string.weight)) },
                    shape = RoundedCornerShape(8.dp),
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                        selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    ),
                )
            }
            GrowthChart(
                children = children,
                records = records,
                metric = metric,
                activeChildIds = activeChildIds,
            )
        }
    }
}

@Composable
private fun LatestGrowthSummary(
    children: List<ChildProfile>,
    records: List<GrowthRecord>,
    activeChildIds: Set<Long>,
) {
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
                text = stringResource(R.string.latest_measurement),
                style = MaterialTheme.typography.titleMedium,
                color = Sage,
            )
            val latestRecords = records
                .filter { it.childId in activeChildIds }
                .groupBy { it.childId }
                .mapNotNull { (childId, childRecords) ->
                    val child = children.firstOrNull { it.id == childId } ?: return@mapNotNull null
                    child to childRecords.maxByOrNull { it.measuredAt }
                }
            if (latestRecords.isEmpty()) {
                Text(
                    text = stringResource(R.string.no_measurement_records),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            } else {
                latestRecords.forEach { (child, record) ->
                    if (record != null) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Column {
                                Text(
                                    text = child.name,
                                    style = MaterialTheme.typography.titleSmall,
                                )
                                Text(
                                    text = formatDateTime(record.measuredAt),
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                )
                            }
                            Text(
                                text = "${record.heightCm} cm / ${record.weightKg} kg",
                                style = MaterialTheme.typography.titleMedium,
                                color = Sage,
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun OverviewCard(dashboard: DashboardState) {
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
                text = dashboard.child?.let { stringResource(R.string.today_focus, it.name) } ?: stringResource(R.string.today_focus_default),
                style = MaterialTheme.typography.titleMedium,
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
