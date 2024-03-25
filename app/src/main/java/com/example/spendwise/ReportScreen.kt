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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.spendwise.data.AppUiState
import com.example.spendwise.ui.theme.AppViewModel

@Composable
fun ReportScreen(
    viewModel: AppViewModel
){
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        WeeklyReport(screenWidth, viewModel)
        Spacer(modifier = Modifier.height(25.dp))
        MonthProjectionReport(screenWidth, viewModel)
    }
}

@Composable
fun WeeklyReport(
    screenWidth: Dp,
    viewModel: AppViewModel)
{

    val uiState by viewModel.uiState.collectAsState()
    var totalSpendings = viewModel.GetTotalSpendings()
    var dataSorted = viewModel.GetSortedSpendings()

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            stringResource(id = R.string.report_screen_title),
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(16.dp))
        VerticalBarsChart(dataSorted)
        Spacer(modifier = Modifier.height(10.dp))
        Text(stringResource(id = R.string.spent_week))
        Text("$${String.format("%.1f", totalSpendings)}/$${uiState.weeklyBudget}",
            fontSize = 24.sp,
            fontWeight = FontWeight.SemiBold
        )
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = String.format(stringResource(id = R.string.spent_month), dataSorted.keys.firstOrNull()),
            textAlign = TextAlign.Center,
            modifier = Modifier.width(screenWidth/2))
    }
}

@Composable
fun MonthProjectionReport(
    screenWidth: Dp,
    viewModel: AppViewModel){

    val uiState by viewModel.uiState.collectAsState()

    var totalSpendings = viewModel.GetTotalSpendings()
    var totalPercentage = (totalSpendings * 100)/uiState.income

    HorizontalBarsChart(viewModel.GetMonthlyReport())
    Spacer(modifier = Modifier.height(25.dp))
    Text(stringResource(id = R.string.spent_month2))
    Text("$${totalSpendings}/$${uiState.budget}",
        fontSize = 24.sp,
        fontWeight = FontWeight.SemiBold
    )
    Text(stringResource(id = R.string.spent_month_budget))
    Spacer(modifier = Modifier.height(8.dp))
    Text(stringResource(id = R.string.saved_percentage))
    Text(text = "${String.format("%.1f", (100 - totalPercentage))}%",
        fontSize = 24.sp,
        color = MaterialTheme.colorScheme.primary,
        fontWeight = FontWeight.SemiBold
    )
    Text(stringResource(id = R.string.saved_from_income))
    Spacer(modifier = Modifier.height(50.dp))
}

@Preview
@Composable
fun ShowGraph() {
    ReportScreen(AppViewModel())
}