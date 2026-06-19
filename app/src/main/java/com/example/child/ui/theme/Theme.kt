package com.example.child.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

@Immutable
data class RecordColors(
    val vaccine: Color,
    val vaccineSurface: Color,
    val medication: Color,
    val medicationSurface: Color,
    val sleep: Color,
    val sleepSurface: Color,
    val growth: Color,
    val growthSurface: Color,
    val health: Color,
    val healthSurface: Color,
    val outing: Color,
    val outingSurface: Color,
    val kindergarten: Color,
    val kindergartenSurface: Color,
    val reminder: Color,
    val reminderSurface: Color,
    val mood: Color,
    val moodSurface: Color,
    val milestone: Color,
    val milestoneSurface: Color,
)

val LocalRecordColors = staticCompositionLocalOf {
    RecordColors(
        vaccine = VaccineBlue,
        vaccineSurface = VaccineBlueSurface,
        medication = MedicationOrange,
        medicationSurface = MedicationOrangeSurface,
        sleep = SleepPurple,
        sleepSurface = SleepPurpleSurface,
        growth = GrowthGreen,
        growthSurface = GrowthGreenSurface,
        health = HealthRed,
        healthSurface = HealthRedSurface,
        outing = OutingTeal,
        outingSurface = OutingTealSurface,
        kindergarten = KindergartenYellow,
        kindergartenSurface = KindergartenYellowSurface,
        reminder = ReminderPink,
        reminderSurface = ReminderPinkSurface,
        mood = MoodLavender,
        moodSurface = MoodLavenderSurface,
        milestone = MilestoneGold,
        milestoneSurface = MilestoneGoldSurface,
    )
}

private val LightColors = lightColorScheme(
    primary = Sage,
    onPrimary = Color.White,
    primaryContainer = SageSurface,
    onPrimaryContainer = SageDark,
    secondary = Clay,
    onSecondary = Color.White,
    secondaryContainer = ClaySurface,
    onSecondaryContainer = ClayDark,
    tertiary = ButterDark,
    onTertiary = Color.White,
    tertiaryContainer = ButterSurface,
    onTertiaryContainer = ButterDark,
    background = MistLight,
    onBackground = Ink,
    surface = Color.White,
    onSurface = Ink,
    surfaceVariant = Mist,
    onSurfaceVariant = InkLight,
    outline = Gray300,
    outlineVariant = Gray200,
)

private val DarkColors = darkColorScheme(
    primary = SageLight,
    onPrimary = SageDark,
    primaryContainer = SageDark,
    onPrimaryContainer = SageSurface,
    secondary = ClayLight,
    onSecondary = ClayDark,
    secondaryContainer = ClayDark,
    onSecondaryContainer = ClaySurface,
    tertiary = ButterLight,
    onTertiary = ButterDark,
    tertiaryContainer = ButterDark,
    onTertiaryContainer = ButterSurface,
    background = Ink,
    onBackground = MistLight,
    surface = InkLight,
    onSurface = MistLight,
    surfaceVariant = InkMuted,
    onSurfaceVariant = Gray400,
    outline = Gray600,
    outlineVariant = Gray700,
)

private val LightRecordColors = RecordColors(
    vaccine = VaccineBlue,
    vaccineSurface = VaccineBlueSurface,
    medication = MedicationOrange,
    medicationSurface = MedicationOrangeSurface,
    sleep = SleepPurple,
    sleepSurface = SleepPurpleSurface,
    growth = GrowthGreen,
    growthSurface = GrowthGreenSurface,
    health = HealthRed,
    healthSurface = HealthRedSurface,
    outing = OutingTeal,
    outingSurface = OutingTealSurface,
    kindergarten = KindergartenYellow,
    kindergartenSurface = KindergartenYellowSurface,
    reminder = ReminderPink,
    reminderSurface = ReminderPinkSurface,
    mood = MoodLavender,
    moodSurface = MoodLavenderSurface,
    milestone = MilestoneGold,
    milestoneSurface = MilestoneGoldSurface,
)

private val DarkRecordColors = RecordColors(
    vaccine = VaccineBlue.copy(alpha = 0.8f),
    vaccineSurface = VaccineBlue.copy(alpha = 0.15f),
    medication = MedicationOrange.copy(alpha = 0.8f),
    medicationSurface = MedicationOrange.copy(alpha = 0.15f),
    sleep = SleepPurple.copy(alpha = 0.8f),
    sleepSurface = SleepPurple.copy(alpha = 0.15f),
    growth = GrowthGreen.copy(alpha = 0.8f),
    growthSurface = GrowthGreen.copy(alpha = 0.15f),
    health = HealthRed.copy(alpha = 0.8f),
    healthSurface = HealthRed.copy(alpha = 0.15f),
    outing = OutingTeal.copy(alpha = 0.8f),
    outingSurface = OutingTeal.copy(alpha = 0.15f),
    kindergarten = KindergartenYellow.copy(alpha = 0.8f),
    kindergartenSurface = KindergartenYellow.copy(alpha = 0.15f),
    reminder = ReminderPink.copy(alpha = 0.8f),
    reminderSurface = ReminderPink.copy(alpha = 0.15f),
    mood = MoodLavender.copy(alpha = 0.8f),
    moodSurface = MoodLavender.copy(alpha = 0.15f),
    milestone = MilestoneGold.copy(alpha = 0.8f),
    milestoneSurface = MilestoneGold.copy(alpha = 0.15f),
)

@Composable
fun ChildGrowthTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val colorScheme = if (darkTheme) DarkColors else LightColors
    val recordColors = if (darkTheme) DarkRecordColors else LightRecordColors

    androidx.compose.runtime.CompositionLocalProvider(
        LocalRecordColors provides recordColors,
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = AppTypography,
            content = content,
        )
    }
}
