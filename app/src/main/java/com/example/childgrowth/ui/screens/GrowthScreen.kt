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
import androidx.compose.ui.unit.dp
import com.example.childgrowth.data.local.ChildProfile
import com.example.childgrowth.data.local.GrowthRecord
import com.example.childgrowth.data.repository.DashboardState
import com.example.childgrowth.ui.components.ChildSwitcher
import com.example.childgrowth.ui.components.GrowthChart
import com.example.childgrowth.ui.components.GrowthMetric
import com.example.childgrowth.ui.components.HeaderBlock
import com.example.childgrowth.ui.components.SummaryRow
import com.example.childgrowth.ui.formatDateTime
import com.example.childgrowth.ui.theme.Sage
import com.example.childgrowth.ui.theme.SageSurface

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

    LaunchedEffect(children.size) {
        if (children.size <= 1) compareMode = false
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
            HeaderBlock(
                title = "成长曲线",
                subtitle = "按月龄观察身高体重变化，也能对比两个孩子",
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
        item {
            LatestGrowthSummary(
                children = children,
                records = allGrowthRecords,
                activeChildIds = activeChildIds,
            )
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
                    text = "生长趋势",
                    style = MaterialTheme.typography.titleMedium,
                )
                if (children.size > 1) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            text = "对比模式",
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
                    label = { Text("身高") },
                    shape = RoundedCornerShape(8.dp),
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                        selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    ),
                )
                FilterChip(
                    selected = metric == GrowthMetric.Weight,
                    onClick = { onMetricChange(GrowthMetric.Weight) },
                    label = { Text("体重") },
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
                text = "最近测量",
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
                    text = "还没有可展示的测量记录",
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
                text = dashboard.child?.let { "${it.name} 的今日重点" } ?: "今日重点",
                style = MaterialTheme.typography.titleMedium,
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
