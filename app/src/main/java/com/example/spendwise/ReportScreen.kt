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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
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

//Composable to return the complete screen layout
@Composable
fun ReportScreen(
    viewModel: AppViewModel,
    modifier: Modifier = Modifier
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
        MonthProjectionReport(viewModel)
    }
}

//Composable to define weekly report layout
@Composable
fun WeeklyReport(
    screenWidth: Dp,
    viewModel: AppViewModel,
    modifier: Modifier = Modifier)
{

    val uiState by viewModel.uiState.collectAsState()

    //Gets the total amount spent in the current week
    var totalSpendings = viewModel.GetTotalSpendingsWeek()

    //Gets the list of spending in the current week
    var data = viewModel.FilterList("Weekly")

    //Sorts the list
    val dataSorted = viewModel.SortByDescendingSpendings(data)

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            stringResource(id = R.string.report_screen_title),
            style = TextStyle(
                fontFamily = FontFamily(Font(R.font.montserrat_regular)),
                fontSize = 25.sp,
            ),
            modifier = Modifier.padding(16.dp))
        VerticalBarsChart(dataSorted)
        Spacer(modifier = Modifier.height(10.dp))
        Text(stringResource(id = R.string.spent_week), style = TextStyle(
            fontFamily = FontFamily(Font(R.font.montserrat_regular)),
            fontSize = 20.sp,
        ))
        Text("$${String.format("%.1f", totalSpendings)}/$${uiState.weeklyBudget}",
            style = TextStyle(
                fontFamily = FontFamily(Font(R.font.montserrat_regular)),
                fontSize = 20.sp,
            )
        )
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = String.format(stringResource(id = R.string.spent_month), dataSorted.keys.firstOrNull()),
            textAlign = TextAlign.Center,
            style = TextStyle(
                fontFamily = FontFamily(Font(R.font.montserrat_regular)),
                fontSize = 16.sp,
            ),
            modifier = Modifier.width(screenWidth/2))
    }
}

//Composable to define month's projection layout
@Composable
fun MonthProjectionReport(
    viewModel: AppViewModel,
    modifier: Modifier = Modifier){

    val uiState by viewModel.uiState.collectAsState()

    //Gets the total amount spent in the current month
    var totalSpendings = viewModel.GetTotalSpendingsMounth()

    //Gets the percentage spent compared to the income
    var totalPercentage = (totalSpendings * 100)/uiState.income

    HorizontalBarsChart(viewModel.GetMonthlyReport())
    Spacer(modifier = Modifier.height(25.dp))
    Text(stringResource(id = R.string.spent_month2), style = TextStyle(
          fontFamily = FontFamily(Font(R.font.montserrat_regular)),
          fontSize = 20.sp)
        )
    Text("$${String.format("%.1f", totalSpendings)}/$${uiState.monthlyBudget}",
        style = TextStyle(
            fontFamily = FontFamily(Font(R.font.montserrat_regular)),
            fontSize = 20.sp,
          )
    )
    Text(stringResource(id = R.string.spent_month_budget),
        style = TextStyle(
            fontFamily = FontFamily(Font(R.font.montserrat_regular)),
            fontSize = 16.sp,
        ))
    Spacer(modifier = Modifier.height(8.dp))
    Text(stringResource(id = R.string.saved_percentage),
        style = TextStyle(
        fontFamily = FontFamily(Font(R.font.montserrat_regular)),
        fontSize = 16.sp,
    ))
    Text(text = "${String.format("%.1f", (100 - totalPercentage))}%",
        style = TextStyle(
            fontFamily = FontFamily(Font(R.font.montserrat_regular)),
            fontSize = 20.sp,
        ),
        color = MaterialTheme.colorScheme.primary,
        fontWeight = FontWeight.SemiBold
    )
    Text(stringResource(id = R.string.saved_from_income), style = TextStyle(
        fontFamily = FontFamily(Font(R.font.montserrat_regular)),
        fontSize = 16.sp,
    ))
    Spacer(modifier = Modifier.height(50.dp))
}

//@Preview
//@Composable
//fun ShowGraph() {
//    ReportScreen(AppViewModel())
//}