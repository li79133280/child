package com.example.childgrowth.ui

import androidx.compose.ui.graphics.Color
import com.example.childgrowth.data.local.MedicationRecord
import com.example.childgrowth.data.local.SleepRecord
import java.time.Instant
import java.time.Period
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import kotlin.math.roundToInt

data class StageGuide(
    val stageName: String,
    val title: String,
    val tips: List<String>,
)

fun buildStageGuide(birthdayMillis: Long): StageGuide {
    val months = monthsBetween(birthdayMillis, System.currentTimeMillis())
    return when {
        months < 12 -> StageGuide(
            stageName = "婴儿期",
            title = "重点是喂养、作息、疫苗和身体反应",
            tips = listOf("喂养记录（母乳/奶粉）", "换尿布次数", "睡眠起止时间", "疫苗接种与下次提醒", "体温监测"),
        )

        months < 24 -> StageGuide(
            stageName = "学步期",
            title = "重点记录饮食、睡眠和成长里程碑",
            tips = listOf("辅食引入记录", "换尿布情况", "第一次站立/走路", "简单词汇记录", "情绪变化"),
        )

        months < 36 -> StageGuide(
            stageName = "幼儿期",
            title = "重点记录生活习惯、外出和成长里程碑",
            tips = listOf("语言发展里程碑", "外出地点和活动", "情绪和行为变化", "身高体重变化", "社交互动"),
        )

        months < 72 -> StageGuide(
            stageName = "学龄前",
            title = "重点积累园所活动、成长习惯和生长数据",
            tips = listOf("身高体重变化", "园所活动与老师反馈", "独立习惯和成长瞬间", "兴趣爱好发展"),
        )

        else -> StageGuide(
            stageName = "扩展阶段",
            title = "当前先保留接口，后续再补小学模块",
            tips = listOf("园所过渡期内容", "长期成长档案", "为后续模块预留结构"),
        )
    }
}

fun parseAccentColor(hex: String): Color =
    runCatching { Color(android.graphics.Color.parseColor(hex)) }.getOrDefault(Color(0xFF2E7D6B))

fun formatAge(birthdayMillis: Long): String {
    val zone = ZoneId.systemDefault()
    val birth = Instant.ofEpochMilli(birthdayMillis).atZone(zone).toLocalDate()
    val today = Instant.now().atZone(zone).toLocalDate()
    val period = Period.between(birth, today)
    return "${period.years}岁${period.months}个月"
}

fun monthsBetween(startMillis: Long, endMillis: Long): Int {
    val zone = ZoneId.systemDefault()
    val start = Instant.ofEpochMilli(startMillis).atZone(zone).toLocalDate()
    val end = Instant.ofEpochMilli(endMillis).atZone(zone).toLocalDate()
    val period = Period.between(start, end)
    return maxOf(0, period.years * 12 + period.months)
}

fun formatDateTime(millis: Long): String {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
    return Instant.ofEpochMilli(millis).atZone(ZoneId.systemDefault()).format(formatter)
}

fun formatSleepSummary(record: SleepRecord): String {
    val wakeAt = record.wokeAt ?: return "睡眠进行中"
    val durationHours = ((wakeAt - record.sleptAt) / 3_600_000.0 * 10).roundToInt() / 10.0
    return "${formatDateTime(record.sleptAt)} 到 ${formatDateTime(wakeAt)} · ${durationHours} 小时"
}

fun formatMedicationCountdown(record: MedicationRecord, now: Long): String {
    val target = record.nextDueAt ?: return "未设置下次服药"
    val diff = target - now
    return if (diff <= 0L) {
        "${record.medicineName} 已到服药时间"
    } else {
        val hours = diff / 3_600_000
        val minutes = (diff % 3_600_000) / 60_000
        "${record.medicineName} 倒计时 ${hours}小时${minutes}分钟"
    }
}
