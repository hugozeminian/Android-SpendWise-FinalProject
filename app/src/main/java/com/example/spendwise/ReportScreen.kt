package com.example.spendwise

import android.view.LayoutInflater
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun ReportScreen(
){
    ExpensesReportLayout()
}

@Composable
fun ExpensesReportLayout(){

    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val scrollState = rememberScrollState()
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
    ) {
        WeeklyReport(screenWidth)
        Spacer(modifier = Modifier.height(25.dp))
        MonthProjectionReport(screenWidth)
    }
}

@Composable
fun WeeklyReport(screenWidth: Dp)
{
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            "Expense Report",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(16.dp))
        VerticalBarsChart(
            data = mapOf(
                Pair("Groceries", 150),
                Pair("Takeout", 230),
                Pair("Utilities", 390),
                Pair("Entertainment", 400),
            ))
        Spacer(modifier = Modifier.height(10.dp))
        Text("You've spent:")
        Text("$550/$750",
            fontSize = 24.sp,
            fontWeight = FontWeight.SemiBold
        )
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            "You are spending a lot more than usual on the following category: Groceries",
            modifier = Modifier.width(screenWidth/2))
    }
}

@Composable
fun MonthProjectionReport(
    screenWidth: Dp){
    HorizontalBarsChart(data = mapOf(
        Pair("Income", 2800),
        Pair("Budget", 2000),
        Pair("Spendings", 1890),
    ))
    Spacer(modifier = Modifier.height(25.dp))
    Text("This month you've spent:")
    Text("$2200/$5000",
        fontSize = 24.sp,
        fontWeight = FontWeight.SemiBold
    )
    Text("Of your monthly budget")
    Text("And saved:")
    Text("60%",
        fontSize = 24.sp,
        fontWeight = FontWeight.SemiBold
    )
    Text("Of your monthly income")
    Spacer(modifier = Modifier.height(50.dp))
}

@Preview
@Composable
fun ShowGraph() {
    ExpensesReportLayout()
}