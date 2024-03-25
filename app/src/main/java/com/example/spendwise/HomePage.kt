package com.example.spendwise

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.spendwise.ui.theme.AppViewModel
import com.example.spendwise.ui.theme.darkThemeColorAlert
import com.example.spendwise.ui.theme.lightThemeColorAlert
import java.text.DecimalFormat

@Composable
fun HomePage(viewModel: AppViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    val budget = uiState.budget
    val weeklyBudget = uiState.weeklyBudget
    val decimalFormat = DecimalFormat("#")

    val formatedBudgetMonth = decimalFormat.format(budget)
    val formatedBudgetWeek = decimalFormat.format(uiState.weeklyBudget)

    val totalOfSpending = viewModel.GetTotalSpendings()
    val formatedTotalOfSpending = decimalFormat.format(totalOfSpending)
    val formatedTotalOfSpendingWeek = decimalFormat.format(totalOfSpending/4)

    var formatedSpentPercentageOfBudget = decimalFormat.format((totalOfSpending * 100 / budget))
    var formatedSpentPercentageOfBudgetWeek = decimalFormat.format((totalOfSpending * 100 / weeklyBudget))

    val isDarkColor = uiState.isDarkMode
    val customColor = if (isDarkColor) lightThemeColorAlert else darkThemeColorAlert
    val budgetAlert = uiState.budgetAlert
    val cardColors = if (formatedSpentPercentageOfBudgetWeek > budgetAlert.toString()) {
        CardDefaults.cardColors(containerColor = customColor)
    } else {
        CardDefaults.cardColors()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            text = stringResource(id = R.string.wp_welcome_text) + uiState.loggedUser.username,
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Text(
            text = stringResource(id = R.string.wp_you_spent),
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Text(
            text = stringResource(id = R.string.app_money_icon) +
                    formatedTotalOfSpendingWeek +
                    stringResource(id = R.string.app_slide) +
                    stringResource(id = R.string.app_money_icon) +
                    formatedBudgetWeek,
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Text(
            text = stringResource(id = R.string.wp_weekly_budget),
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Divider()

        Text(
            text = stringResource(id = R.string.wp_you_spent),
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(top = 8.dp, bottom = 8.dp)
        )
        Text(
            text = stringResource(id = R.string.app_money_icon) +
                    formatedTotalOfSpending +
                    stringResource(id = R.string.app_slide) +
                    stringResource(id = R.string.app_money_icon) +
                    formatedBudgetMonth,
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Text(
            text = stringResource(id = R.string.wp_monthly_budget),
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Divider()

        Card(
            modifier = Modifier.padding(top = 16.dp),
            colors = cardColors,
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.wp_you_spent) +
                            formatedSpentPercentageOfBudgetWeek +
                            stringResource(id = R.string.app_percentage)  +
                            stringResource(id = R.string.wp_weekly_budget),
                    style = MaterialTheme.typography.headlineLarge,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }
        }

        Card(
            modifier = Modifier.padding(top = 16.dp),
            colors = cardColors,
            ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.wp_you_spent) +
                            formatedSpentPercentageOfBudget +
                            stringResource(id = R.string.app_percentage)  +
                            stringResource(id = R.string.wp_monthly_budget),
                    style = MaterialTheme.typography.headlineLarge,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }
        }
    }
}

@Preview
@Composable
fun PreviewHomePage() {
    val viewModel = AppViewModel()
    HomePage(viewModel)
}
