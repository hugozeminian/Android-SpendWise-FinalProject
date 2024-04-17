package com.example.spendwise

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.spendwise.data.AppUiState
import com.example.spendwise.model.Spending
import com.example.spendwise.ui.theme.AppViewModel
import com.example.spendwise.ui.theme.darkThemeColorAlert
import com.example.spendwise.ui.theme.lightThemeColorAlert
import java.text.DecimalFormat
import java.util.Calendar
import kotlin.Double.Companion.NaN

@Composable
fun HomePage(viewModel: AppViewModel) {

    val uiState by viewModel.uiState.collectAsState()
    val budget = uiState.monthlyBudget
    val weeklyBudget = uiState.weeklyBudget
    val decimalFormat = DecimalFormat("#")

    val formatedBudgetMonth = decimalFormat.format(budget)
    val formatedBudgetWeek = decimalFormat.format(uiState.weeklyBudget)

    val totalOfSpendingMounth = viewModel.GetTotalSpendingsMounth()
    val totalOfSpendingWeek = viewModel.GetTotalSpendingsWeek()
    val formatedTotalOfSpending = decimalFormat.format(totalOfSpendingMounth)
    val formatedTotalOfSpendingWeek = decimalFormat.format(totalOfSpendingWeek)

    var formatedSpentPercentageOfBudget = decimalFormat.format((totalOfSpendingMounth * 100 / budget))

    var number = formatedSpentPercentageOfBudget.toDoubleOrNull()

    if (number == null || number.isNaN()) {
        formatedSpentPercentageOfBudget = "0"
    }

    var formatedSpentPercentageOfBudgetWeek =
        decimalFormat.format((totalOfSpendingWeek * 100 / weeklyBudget))

    number = formatedSpentPercentageOfBudgetWeek.toDoubleOrNull()

    if (number == null || number.isNaN()) {
        formatedSpentPercentageOfBudgetWeek = "0"
    }

    val isDarkColor = uiState.isDarkMode
    val customColor = if (isDarkColor) darkThemeColorAlert else lightThemeColorAlert
    val budgetAlert = uiState.budgetAlert

    val cardColorsMonth = getCardColors(totalOfSpendingMounth, budget, budgetAlert, customColor)
    val cardColorsWeek = getCardColors(totalOfSpendingWeek, weeklyBudget, budgetAlert, customColor)

    viewModel.getData()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(dimensionResource(id = R.dimen.padding_medium))
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = stringResource(id = R.string.wp_welcome_text) + uiState.loggedUser.username,
            style = TextStyle(
                fontFamily = FontFamily(Font(R.font.montserrat_regular)),
                fontSize = 32.sp,
                textDecoration = TextDecoration.Underline
            ),
            modifier = Modifier.padding(bottom = dimensionResource(id = R.dimen.padding_small))
        )
        Text(
            text = stringResource(id = R.string.wp_you_spent),
            style = TextStyle(
                fontFamily = FontFamily(Font(R.font.montserrat_regular)),
                fontSize = 16.sp,

            ),
            modifier = Modifier.padding(bottom = dimensionResource(id = R.dimen.padding_small))
        )
        Text(
            text = stringResource(id = R.string.app_money_icon) +
                    formatedTotalOfSpendingWeek +
                    stringResource(id = R.string.app_slide) +
                    stringResource(id = R.string.app_money_icon) +
                    formatedBudgetWeek,
            style = TextStyle(
                fontFamily = FontFamily(Font(R.font.montserrat_regular)),
                fontSize = 32.sp,
            ),
            modifier = Modifier.padding(bottom = dimensionResource(id = R.dimen.padding_small))
        )
        Text(
            text = stringResource(id = R.string.wp_weekly_budget),
            style = TextStyle(
                fontFamily = FontFamily(Font(R.font.montserrat_regular)),
                fontSize = 16.sp,
            ),
            modifier = Modifier.padding(bottom = dimensionResource(id = R.dimen.padding_small))
        )

        Divider()

        Text(
            text = stringResource(id = R.string.wp_you_spent),
            style = TextStyle(
                fontFamily = FontFamily(Font(R.font.montserrat_regular)),
                fontSize = 16.sp,
            ),
            modifier = Modifier.padding(
                top = dimensionResource(id = R.dimen.padding_small),
                bottom = dimensionResource(id = R.dimen.padding_small))
        )
        Text(
            text = stringResource(id = R.string.app_money_icon) +
                    formatedTotalOfSpending +
                    stringResource(id = R.string.app_slide) +
                    stringResource(id = R.string.app_money_icon) +
                    formatedBudgetMonth,
            style = TextStyle(
                fontFamily = FontFamily(Font(R.font.montserrat_regular)),
                fontSize = 32.sp,
            ),
            modifier = Modifier.padding(bottom = dimensionResource(id = R.dimen.padding_small))
        )
        Text(
            text = stringResource(id = R.string.wp_monthly_budget),
            style = TextStyle(
                fontFamily = FontFamily(Font(R.font.montserrat_regular)),
                fontSize = 16.sp,
            ),
            modifier = Modifier.padding(bottom = dimensionResource(id = R.dimen.padding_small))
        )

        Divider()

        Card(
            modifier = Modifier
                .padding(top = 16.dp)
                .fillMaxWidth()
                .align(Alignment.CenterHorizontally)
            ,
            colors = cardColorsWeek,
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.wp_you_spent) +
                            formatedSpentPercentageOfBudgetWeek +
                            stringResource(id = R.string.app_percentage) +
                            stringResource(id = R.string.wp_weekly_budget),
                    style = TextStyle(
                        fontFamily = FontFamily(Font(R.font.montserrat_regular)),
                        fontSize = 32.sp,
                    ),
                )
            }
        }

        Card(
            modifier = Modifier
                .padding(top = dimensionResource(id = R.dimen.padding_medium))
                .fillMaxWidth()
                .align(Alignment.CenterHorizontally),
            colors = cardColorsMonth,
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_medium))
            ) {
                Text(
                    text = stringResource(id = R.string.wp_you_spent) +
                            formatedSpentPercentageOfBudget +
                            stringResource(id = R.string.app_percentage) +
                            stringResource(id = R.string.wp_monthly_budget),
                    style = TextStyle(
                        fontFamily = FontFamily(Font(R.font.montserrat_regular)),
                        fontSize = 32.sp,
                    ),
                )
            }
        }
    }
}

//@Preview
//@Composable
//fun PreviewHomePage() {
//    val viewModel = AppViewModel()
//    HomePage(viewModel)
//}

//@Composable
//fun SpendingList(viewModel: AppViewModel, uiState: AppUiState) {
//    var ascending by remember { mutableStateOf(true) }
//    val sortedList = viewModel.getSortedSpendingList(ascending, uiState)
//
//    Card(modifier = Modifier.padding(top = 8.dp)) {
//        Column(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(16.dp)
//        ) {
//            Row(verticalAlignment = Alignment.CenterVertically) {
//                Text("Sort by Date:", modifier = Modifier.padding(end = 8.dp))
//                Switch(checked = ascending, onCheckedChange = { ascending = it })
//            }
//            LazyColumn {
//                items(sortedList) { spending ->
//                    Text("${spending.category} - ${spending.date} - ${spending.amount}")
//                }
//            }
//        }
//    }
//}

//@Composable
//fun SpendingListForCurrentMonth(viewModel: AppViewModel, uiState: AppUiState) {
//    var ascending by remember { mutableStateOf(true) }
//    val currentMonth = Spending.getCurrentMonth()
//    val spendingsForCurrentMonth = viewModel.getSortedSpendingsForMonth(currentMonth, ascending, uiState)
//
//    Card(modifier = Modifier.padding(top = 8.dp)) {
//        Column(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(16.dp)
//        ) {
//            Row(verticalAlignment = Alignment.CenterVertically) {
//                Text("Sort by Date:", modifier = Modifier.padding(end = 8.dp))
//                Switch(checked = ascending, onCheckedChange = { ascending = it })
//            }
//            Text("Spending for Current Month", modifier = Modifier.padding(bottom = 8.dp))
//            LazyColumn {
//                items(spendingsForCurrentMonth) { spending ->
//                    Text("${spending.category} - ${spending.date} - ${spending.amount}")
//                }
//            }
//        }
//    }
//
//}

//@Composable
//fun SpendingListForCurrentWeek(viewModel: AppViewModel, uiState: AppUiState) {
//    var ascending by remember { mutableStateOf(true) }
//    val calendar = Calendar.getInstance()
//    val weekDays = Spending.getWeekDays(calendar)
//    val spendingsForCurrentWeek = viewModel.getSortedSpendingsForWeek(weekDays, calendar, ascending, uiState)
//
//    Card(modifier = Modifier.padding(top = 8.dp)) {
//        Column(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(16.dp)
//        ) {
//            Row(verticalAlignment = Alignment.CenterVertically) {
//                Text("Sort by Date:", modifier = Modifier.padding(end = 8.dp))
//                Switch(checked = ascending, onCheckedChange = { ascending = it })
//            }
//            Text("Spending for Current Week", modifier = Modifier.padding(bottom = 8.dp))
//            LazyColumn {
//                items(spendingsForCurrentWeek) { spending ->
//                    Text("${spending.category} - ${spending.date} - ${spending.amount}")
//                }
//            }
//        }
//    }
//
//}

@Composable
fun getCardColors(
    totalSpending: Float,
    budget: Float,
    budgetAlert: Float,
    customColor: Color
): CardColors {
    val formattedSpentPercentage = totalSpending * 100 / budget
    return if (formattedSpentPercentage > budgetAlert) {
        CardDefaults.cardColors(containerColor = customColor)
    } else {
        CardDefaults.cardColors()
    }
}