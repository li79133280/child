package com.example.childgrowth

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import com.example.childgrowth.ui.GrowthJournalApp
import com.example.childgrowth.ui.GrowthJournalViewModel
import com.example.childgrowth.ui.theme.ChildGrowthTheme

class MainActivity : ComponentActivity() {
    private val viewModel: GrowthJournalViewModel by viewModels {
        GrowthJournalViewModel.Factory
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ChildGrowthTheme {
                GrowthJournalApp(viewModel = viewModel)
            }
        }
    }
}
