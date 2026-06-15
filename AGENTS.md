# AGENTS.md — ChildGrowthJournal

## Build & Run

```bash
# Windows (PowerShell) — Gradle daemon is OFF
.\gradlew.bat assembleDebug

# Or open in Android Studio → Sync Gradle → Run 'app' config
```

- Gradle wrapper uses a **local zip** at `.gradle-bootstrap/gradle-8.7-bin.zip` — no network download needed
- `org.gradle.daemon=false` in gradle.properties — builds are cold-start every time
- `local.properties` must point to Android SDK (currently `D:\Android sdk`)
- compileSdk = 36, targetSdk = 36, minSdk = 26

## Tech Stack

- **Language**: Kotlin 1.9.24, JVM target 17
- **UI**: Jetpack Compose (BOM 2024.06.00), Material3, Material Icons Extended
- **DB**: Room 2.6.1 with KSP 1.9.24-1.0.20 (schema version 2, destructive fallback migration)
- **Build**: AGP 8.5.2, single module (`app`)
- **Architecture**: Application → Repository → ViewModel → Composable screens
- **No dependency injection** — single-activity, manual wiring via `GrowthJournalApplication`

## Architecture

- Single module (`app`), single Activity (`MainActivity`)
- `GrowthJournalApplication` exposes `database` and `repository` as lazy singletons
- `GrowthJournalViewModel` accesses them via `(application as GrowthJournalApplication)`
- All DB tables are child-foreign-keyed with `CASCADE` delete
- Seed data (2 children + sample records) inserted on first launch via `ensureSeedData()`
- No navigation library — tabs use a `when(currentTab)` switch in Scaffold

## Key Files

| Path | Role |
|------|------|
| `app/src/main/java/com/example/childgrowth/GrowthJournalApplication.kt` | App entry, lazy DB + repo init |
| `app/src/main/java/com/example/childgrowth/MainActivity.kt` | Single Activity, edge-to-edge |
| `app/src/main/java/com/example/childgrowth/ui/GrowthJournalApp.kt` | All screens in one file (~1058 lines) |
| `app/src/main/java/com/example/childgrowth/ui/GrowthJournalViewModel.kt` | ViewModel, all mutations via `launchForSelectedChild` |
| `app/src/main/java/com/example/childgrowth/data/local/Entities.kt` | All 10 Room entities |
| `app/src/main/java/com/example/childgrowth/data/local/Daos.kt` | All 10 DAO interfaces |
| `app/src/main/java/com/example/childgrowth/data/local/AppDatabase.kt` | Room DB v2, "child_growth_journal.db" |
| `app/src/main/java/com/example/childgrowth/data/repository/GrowthRepository.kt` | Data layer, dashboard combine, seed data |
| `app/src/main/java/com/example/childgrowth/ui/components/Dialogs.kt` | 8 record-dialog composables |
| `app/src/main/java/com/example/childgrowth/ui/components/GrowthChart.kt` | Canvas chart, dual-child compare |
| `app/src/main/java/com/example/childgrowth/ui/theme/Theme.kt` | Light+Dark theme, custom palette |

## Gotchas

- `GrowthJournalApp.kt` is a **1058-line monolith** — all tabs/screens/composables live here
- Database `fallbackToDestructiveMigration()` means schema changes will **wipe all data** on upgrade
- No tests exist yet — only test dependencies are declared in build.gradle.kts
- Chinese UI strings are hardcoded in Compose — no string resources
- Sleep tracking uses a two-step flow (start → end) with `wokeAt IS NULL` queries
- `exportSchema = false` in Room — no schema JSON is generated
- Adding a new entity type requires: Entity class, DAO, AppDatabase.entities list, GrowthRepository observeDashboard combine, ViewModel method, Dialog composable, GrowthJournalApp wiring
- `observeDashboard()` in GrowthRepository combines **11 Flow sources** — this is the central data aggregation point
- All 10 entities share identical FK pattern: `childId -> child_profiles.id` with `CASCADE` + `Index("childId")`
- Theme uses custom color names (Sage, Clay, Butter, Mist, Ink) in `ui/theme/Color.kt`
