package com.example.child

import android.app.Application
import com.example.child.data.local.AppDatabase
import com.example.child.data.repository.GrowthRepository

class GrowthJournalApplication : Application() {
    val database: AppDatabase by lazy { AppDatabase.build(this) }
    val repository: GrowthRepository by lazy { GrowthRepository(database) }
}
