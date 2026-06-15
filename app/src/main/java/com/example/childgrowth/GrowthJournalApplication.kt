package com.example.childgrowth

import android.app.Application
import com.example.childgrowth.data.local.AppDatabase
import com.example.childgrowth.data.repository.GrowthRepository

class GrowthJournalApplication : Application() {
    val database: AppDatabase by lazy { AppDatabase.build(this) }
    val repository: GrowthRepository by lazy { GrowthRepository(database) }
}
