package com.example.spendwise

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.tooling.preview.Preview

class WelcomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

        val chartComposeView_BarChart = findViewById<ComposeView>(R.id.chartComposeView_BarChart)
        chartComposeView_BarChart.setContent {
            DisplayChart()
        }

//        val chartComposeView_ReportScreen = findViewById<ComposeView>(R.id.chartComposeView_ReportScreen)
//        chartComposeView_ReportScreen.setContent {
//            ShowGraph()
//        }

    }
}

@Preview
@Composable
fun DisplayChart()
{
    Column {
        VerticalBarsChart(
            data = mapOf(
                Pair("Groceries", 520),
                Pair("Takeout", 500),
                Pair("Utilities", 300),
                Pair("Entertainment", 200),
                Pair("Emergency", 600)
            ))
        HorizontalBarsChart(
            data = mapOf(
                Pair("Income", 4000),
                Pair("Budget", 3200),
                Pair("Spendings", 2400),
            )
        )
    }
}

@Preview
@Composable
fun ShowGraph() {
//    ExpensesReportLayout({})
    ReportScreen()
}