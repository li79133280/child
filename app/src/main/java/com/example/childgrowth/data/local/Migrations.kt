package com.example.childgrowth.data.local

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

object Migrations {
    val MIGRATION_1_2 = object : Migration(1, 2) {
        override fun migrate(db: SupportSQLiteDatabase) {
            // 版本 1 → 2 的变更记录
            // 如果有实际变更，在这里执行 SQL
        }
    }

    val MIGRATION_2_3 = object : Migration(2, 3) {
        override fun migrate(db: SupportSQLiteDatabase) {
            // 版本 2 → 3 的变更记录
            // 如果有实际变更，在这里执行 SQL
        }
    }

    fun all(): Array<Migration> = arrayOf(
        MIGRATION_1_2,
        MIGRATION_2_3,
    )
}
