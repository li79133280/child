package com.example.childgrowth.ui.components

import android.graphics.Paint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.childgrowth.R
import com.example.childgrowth.data.local.ChildProfile
import com.example.childgrowth.data.local.GrowthRecord
import com.example.childgrowth.ui.monthsBetween
import com.example.childgrowth.ui.parseAccentColor
import kotlin.math.max

enum class GrowthMetric {
    Height,
    Weight,
}

@Composable
fun GrowthChart(
    children: List<ChildProfile>,
    records: List<GrowthRecord>,
    metric: GrowthMetric,
    activeChildIds: Set<Long>,
    modifier: Modifier = Modifier,
) {
    val grouped = children
        .filter { it.id in activeChildIds }
        .associateWith { child ->
            records.filter { it.childId == child.id }.sortedBy { it.measuredAt }
        }
        .filterValues { it.isNotEmpty() }

    if (grouped.isEmpty()) {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .height(240.dp)
                .background(
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f),
                    shape = MaterialTheme.shapes.large,
                )
                .padding(24.dp),
        ) {
            Text(
                text = stringResource(R.string.growth_empty_hint),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
        return
    }

    val points = grouped.flatMap { (child, childRecords) ->
        childRecords.map { record ->
            PlotPoint(
                ageMonths = monthsBetween(child.birthday, record.measuredAt),
                value = if (metric == GrowthMetric.Height) record.heightCm.toFloat() else record.weightKg.toFloat(),
            )
        }
    }

    val minAge = points.minOf { it.ageMonths }.toFloat()
    val maxAge = max(points.maxOf { it.ageMonths }, points.minOf { it.ageMonths } + 1).toFloat()
    val minValue = points.minOf { it.value }
    val maxValue = max(points.maxOf { it.value + 1f }, minValue + 1f)

    val outlineColor = MaterialTheme.colorScheme.outline
    val monthLabel = stringResource(R.string.month_suffix)
    val metricLabel = if (metric == GrowthMetric.Height) stringResource(R.string.growth_height) else stringResource(R.string.growth_weight)

    Column(verticalArrangement = Arrangement.spacedBy(12.dp), modifier = modifier) {
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(260.dp)
                .background(
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f),
                    shape = MaterialTheme.shapes.large,
                )
                .padding(12.dp),
        ) {
            val left = 72f
            val right = size.width - 32f
            val top = 24f
            val bottom = size.height - 48f
            val xSpan = max(1f, maxAge - minAge)
            val ySpan = max(1f, maxValue - minValue)

            drawLine(
                color = outlineColor,
                start = Offset(left, bottom),
                end = Offset(right, bottom),
                strokeWidth = 2f,
            )
            drawLine(
                color = outlineColor,
                start = Offset(left, top),
                end = Offset(left, bottom),
                strokeWidth = 2f,
            )

            val labelPaint = Paint().apply {
                color = android.graphics.Color.DKGRAY
                textSize = 28f
                isAntiAlias = true
            }

            repeat(5) { index ->
                val fraction = index / 4f
                val y = bottom - (bottom - top) * fraction
                val value = minValue + ySpan * fraction
                drawLine(
                    color = outlineColor.copy(alpha = 0.3f),
                    start = Offset(left, y),
                    end = Offset(right, y),
                    strokeWidth = 1f,
                )
                drawContext.canvas.nativeCanvas.drawText(
                    String.format("%.1f", value),
                    8f,
                    y + 10f,
                    labelPaint,
                )
            }

            repeat(5) { index ->
                val fraction = index / 4f
                val x = left + (right - left) * fraction
                val age = minAge + xSpan * fraction
                drawContext.canvas.nativeCanvas.drawText(
                    "${age.toInt()}$monthLabel",
                    x - 20f,
                    bottom + 34f,
                    labelPaint,
                )
            }

            grouped.forEach { (child, childRecords) ->
                val color = parseAccentColor(child.accentColor)
                var lastPoint: Offset? = null

                childRecords.forEach { record ->
                    val age = monthsBetween(child.birthday, record.measuredAt).toFloat()
                    val value =
                        if (metric == GrowthMetric.Height) record.heightCm.toFloat() else record.weightKg.toFloat()
                    val x = left + ((age - minAge) / xSpan) * (right - left)
                    val y = bottom - ((value - minValue) / ySpan) * (bottom - top)
                    val point = Offset(x, y)

                    if (lastPoint != null) {
                        drawLine(
                            color = color,
                            start = lastPoint!!,
                            end = point,
                            strokeWidth = 6f,
                            cap = StrokeCap.Round,
                        )
                    }
                    drawCircle(color = color, radius = 7f, center = point)
                    lastPoint = point
                }
            }

            drawContext.canvas.nativeCanvas.drawText(
                metricLabel,
                left,
                18f,
                labelPaint.apply { textSize = 30f },
            )
        }

        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            grouped.keys.forEach { child ->
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Box(
                        modifier = Modifier
                            .size(12.dp)
                            .background(parseAccentColor(child.accentColor), CircleShape),
                    )
                    Text(
                        text = child.name,
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                    )
                }
            }
        }
    }
}

private data class PlotPoint(
    val ageMonths: Int,
    val value: Float,
)
