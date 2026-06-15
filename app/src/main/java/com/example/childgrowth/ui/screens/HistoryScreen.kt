package com.example.childgrowth.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.childgrowth.R
import com.example.childgrowth.data.local.ChildProfile
import com.example.childgrowth.data.repository.DashboardState
import com.example.childgrowth.ui.components.ChildSwitcher
import com.example.childgrowth.ui.components.HeaderBlock
import com.example.childgrowth.ui.components.RecentTimeline

enum class TimelineFilter(val labelResId: Int) {
    All(R.string.filter_all),
    Reminder(R.string.filter_reminder),
    Vaccine(R.string.filter_vaccine),
    Medication(R.string.filter_medication),
    Sleep(R.string.filter_sleep),
    Outing(R.string.filter_outing),
    Growth(R.string.filter_growth),
    Health(R.string.filter_health),
    Kindergarten(R.string.filter_kindergarten),
    Custom(R.string.filter_custom),
}

@Composable
fun HistoryScreen(
    children: List<ChildProfile>,
    dashboard: DashboardState,
    selectedChildId: Long?,
    onSelectChild: (Long) -> Unit,
    onEditChild: () -> Unit,
) {
    var filter by remember { mutableStateOf(TimelineFilter.All) }
    val filteredItems = remember(dashboard.timeline, filter) {
        dashboard.timeline.filter { item ->
            when (filter) {
                TimelineFilter.All -> true
                TimelineFilter.Reminder -> item.badge == "提醒"
                TimelineFilter.Vaccine -> item.badge == "疫苗"
                TimelineFilter.Medication -> item.badge == "吃药"
                TimelineFilter.Sleep -> item.badge == "睡眠"
                TimelineFilter.Outing -> item.badge == "出游"
                TimelineFilter.Growth -> item.badge == "生长"
                TimelineFilter.Health -> item.badge == "健康"
                TimelineFilter.Kindergarten -> item.badge == "园所"
                TimelineFilter.Custom -> item.badge !in setOf("提醒", "疫苗", "吃药", "睡眠", "出游", "生长", "健康", "园所")
            }
        }
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        item {
            HeaderBlock(
                title = stringResource(R.string.history_title),
                subtitle = stringResource(R.string.history_subtitle),
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
            HistoryFilterCard(currentFilter = filter, onSelect = { filter = it })
        }
        item {
            RecentTimeline(title = stringResource(R.string.filter_result), items = filteredItems)
        }
        item { Spacer(modifier = Modifier.height(8.dp)) }
    }
}

@Composable
private fun HistoryFilterCard(
    currentFilter: TimelineFilter,
    onSelect: (TimelineFilter) -> Unit,
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
                text = stringResource(R.string.filter_type),
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                TimelineFilter.entries.chunked(3).forEach { rowFilters ->
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        rowFilters.forEach { filter ->
                            FilterChip(
                                selected = currentFilter == filter,
                                onClick = { onSelect(filter) },
                                label = { Text(stringResource(filter.labelResId)) },
                                shape = RoundedCornerShape(8.dp),
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                                    selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer,
                                ),
                            )
                        }
                    }
                }
            }
        }
    }
}
