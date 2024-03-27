package com.example.spendwise


import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.spendwise.data.NumericAlertMessage
import com.example.spendwise.data.containsOnlyNumbers
import com.example.spendwise.model.RewardItem
import com.example.spendwise.model.SpendingsCategories
import com.example.spendwise.ui.theme.AppViewModel
import com.example.spendwise.ui.theme.Shapes



@Composable
fun SpendingsCategories(viewModel: AppViewModel) {
    val uiState by viewModel.uiState.collectAsState()

    // Maintain state for category name and weekly limit fields
    var categoryName by remember { mutableStateOf("") }
    var weeklyLimit by remember { mutableStateOf("") }

    // Maintain state for the list of spending categories
    var spendingCategories by remember { mutableStateOf(uiState.spendingsCategoriesList) }

    // Manage scroll state
    val lazyListState = rememberLazyListState()

    var showAlertMessage by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier.padding(8.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = stringResource(id = R.string.bp_spendings_categories),
                style = TextStyle(
                    fontFamily = FontFamily(Font(R.font.montserrat_regular)),
                    fontSize = 25.sp,
                ),
                modifier = Modifier.padding(bottom = 12.dp)
            )
            // Input fields for category name and weekly limit
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextField(
                    value = categoryName,
                    onValueChange = { categoryName = it },
                    label = { Text(text = "Name") },
                    modifier = Modifier.weight(1f)
                )

                Spacer(modifier = Modifier.width(16.dp))

                TextField(
                    value = weeklyLimit,
                    onValueChange = { newValue ->
                        if (containsOnlyNumbers(newValue)) {
                            // Only allow numeric input and limit to two decimal places
                            val newText =
                                newValue.takeIf { text -> text.matches(Regex("^\\d*\\.?\\d{0,2}$")) }
                                    ?: weeklyLimit
                            weeklyLimit = newText
                            showAlertMessage = false
                        } else {
                            showAlertMessage = true
                        }
                    },
                    label = { Text(stringResource(id = R.string.bp_cat_weekly_limit)) },
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Clear All and Add Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                // Clear All Button
                Button(
                    onClick = { spendingCategories = emptyList()
                                viewModel.RemoveAllSpendingsCategoriesItem()
                              },
                    shape = Shapes.extraSmall,
                    modifier = Modifier.weight(1f)
                ) {
                    Text(text = stringResource(id = R.string.bp_button_clear_all), style = TextStyle(
                        fontFamily = FontFamily(Font(R.font.montserrat_regular)),
                        fontSize = 16.sp,
                    ))
                }

                Spacer(modifier = Modifier.width(16.dp))

                // Add Button
                Button(
                    onClick = {
                        // Add the spending category to the list
                        if (categoryName.isNotBlank() && weeklyLimit.isNotBlank()) {
                            val newCategory = SpendingsCategories(categoryName, weeklyLimit.toFloat())

                            spendingCategories = uiState.spendingsCategoriesList + newCategory
                            viewModel.AddSpendingsCategoriesItem(newCategory)


                            // Clear fields after adding
                            categoryName = ""
                            weeklyLimit = ""
                        }
                    },
                    shape = Shapes.extraSmall,
                    modifier = Modifier.weight(1f)
                ) {
                    Text(stringResource(id = R.string.bp_button_add), style = TextStyle(
                        fontFamily = FontFamily(Font(R.font.montserrat_regular)),
                        fontSize = 16.sp,
                    ))
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                NumericAlertMessage(showAlertMessage)
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Display list of spendings categories.
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 200.dp),  // Limit the height for scrolling
                state = lazyListState
            ) {
                items(spendingCategories.size) { index ->
                    val category = spendingCategories[index]
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = category.name,
                            style = TextStyle(
                                fontFamily = FontFamily(Font(R.font.montserrat_regular)),
                                fontSize = 13.sp,
                            ),
                            modifier = Modifier.weight(1f)
                        )
                        Text(
                            text = stringResource(id = R.string.bp_cat_weekly_limit) + category.weeklyLimit,
                            style = TextStyle(
                                fontFamily = FontFamily(Font(R.font.montserrat_regular)),
                                fontSize = 13.sp,
                            ),
                            modifier = Modifier.weight(1f)
                        )
                        IconButton(
                            onClick = {
                                // Remove the spending category from the list
                                spendingCategories = spendingCategories.filterIndexed { i, _ -> i != index }
                                viewModel.RemoveSpendingsCategoriesItem(index)
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Delete"
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MonthlyWeeklyBudget(viewModel: AppViewModel, monthlyIncome: Double) {
    val uiState by viewModel.uiState.collectAsState()

    var editing by remember { mutableStateOf(false) }
    var monthlyBudget by remember { mutableStateOf(uiState.budget.toString()) }
    var weeklyBudget by remember { mutableStateOf(uiState.weeklyBudget.toString()) }
    var budgetAlert by remember { mutableStateOf(uiState.budgetAlert.toString()) }
    var showAlert by remember { mutableStateOf(uiState.showAlert) }

    var showAlertMessage by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier.padding(8.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = stringResource(id = R.string.bp_monthly_budget),
                    style = TextStyle(
                        fontFamily = FontFamily(Font(R.font.montserrat_regular)),
                        fontSize = 16.sp,
                    ),
                    modifier = Modifier.alignByBaseline()
                )

                if (editing) {
                    TextField(
                        value = monthlyBudget,
                        onValueChange = { newValue ->
                            if (containsOnlyNumbers(newValue)) {
                                monthlyBudget = newValue
                                val income = newValue.toFloatOrNull() ?: 0f
                                viewModel.SetMonthlyIncome(income)
                                showAlertMessage = false
                            } else {
                                showAlertMessage = true
                            }
                        },
                        label = { Text(text = "") },
                        modifier = Modifier.align(Alignment.CenterVertically)
                    )
                } else {
                    Text(
                        text = "$$monthlyBudget",
                        style = TextStyle(
                            fontFamily = FontFamily(Font(R.font.montserrat_regular)),
                            fontSize = 16.sp,
                        ),
                        modifier = Modifier.align(Alignment.CenterVertically)
                    )
                }
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = stringResource(id = R.string.bp_weekly_budget),
                    style = TextStyle(
                        fontFamily = FontFamily(Font(R.font.montserrat_regular)),
                        fontSize = 16.sp,
                    ),
                    modifier = Modifier.alignByBaseline()
                )

                if (editing) {
                    TextField(
                        value = weeklyBudget,
                        onValueChange = { newValue ->
                            if (containsOnlyNumbers(newValue)) {
                                weeklyBudget = newValue
                                val income = newValue.toFloatOrNull() ?: 0f
                                viewModel.SetMonthlyIncome(income)
                                showAlertMessage = false
                            } else {
                                showAlertMessage = true
                            }
                        },
                        label = { Text(text = "") },
                        modifier = Modifier.align(Alignment.CenterVertically)
                    )
                } else {
                    Text(
                        text = "$$weeklyBudget",
                        style = TextStyle(
                            fontFamily = FontFamily(Font(R.font.montserrat_regular)),
                            fontSize = 16.sp,
                        ),
                        modifier = Modifier.align(Alignment.CenterVertically)
                    )
                }
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = stringResource(id = R.string.bp_budget_alert),
                    style = TextStyle(
                        fontFamily = FontFamily(Font(R.font.montserrat_regular)),
                        fontSize = 16.sp,
                    ),
                    modifier = Modifier.alignByBaseline()
                )

                if (editing) {
                    TextField(
                        value = budgetAlert,
                        onValueChange = { newValue ->
                            if (containsOnlyNumbers(newValue)) {
                                budgetAlert = newValue
                                val income = newValue.toFloatOrNull() ?: 0f
                                viewModel.SetMonthlyIncome(income)
                                showAlertMessage = false
                            } else {
                                showAlertMessage = true
                            }
                        },
                        label = { Text(text = "") },
                        modifier = Modifier.align(Alignment.CenterVertically)
                    )
                } else {
                    Text(
                        text = "%$budgetAlert",
                        style = TextStyle(
                            fontFamily = FontFamily(Font(R.font.montserrat_regular)),
                            fontSize = 16.sp,
                        ),
                        modifier = Modifier.align(Alignment.CenterVertically)
                    )
                }
            }

            if (showAlert) { //ensure user's budget does not exceed monthly income set
                Text(
                    text = "Error, Your monthly budget and/or weekly budget exceeds your current income!",
                    color = Color.Red,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }

            Button(
                onClick = {//ensure user's budget does not exceed monthly income set
                    if (editing) {
                        val monthlyBudgetValue = monthlyBudget.toDoubleOrNull() ?: 0.0
                        val weeklyBudgetValue = weeklyBudget.toDoubleOrNull() ?: 0.0
                        val monthlyBudgetLimit = weeklyBudgetValue * 4

                        viewModel.SetMonthlyBudget(monthlyBudget.toFloat())
                        viewModel.SetWeeklyBudget(weeklyBudget.toFloat())
                        viewModel.SetBudgetAlert(budgetAlert.toFloat())

                        if (monthlyBudgetValue > monthlyIncome || monthlyBudgetValue < monthlyBudgetLimit) {
                            showAlert = true
                        } else {
                            showAlert = false
                            editing = false

                        }
                    } else {
                        editing = true
                    }
                },
                shape = Shapes.extraSmall,
                modifier = Modifier
                    .padding(top = 16.dp)
                    .align(Alignment.CenterHorizontally)
            ) {
                Text(text = if (editing) stringResource(id = R.string.bp_button_save) else stringResource(id = R.string.bp_button_edit), style = TextStyle(
                    fontFamily = FontFamily(Font(R.font.montserrat_regular)),
                    fontSize = 16.sp,
                ))
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                NumericAlertMessage(showAlertMessage)
            }
        }
    }
}

@Composable
fun RewardsInfo(viewModel: AppViewModel) {
    val uiState by viewModel.uiState.collectAsState()

    // Maintain state for description and amount fields
    var description by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }

    // Calculate the total sum of amounts
    var totalAmount by remember { mutableStateOf(0.0) }
    totalAmount = uiState.rewardsList.map { it.amount.toDoubleOrNull() ?: 0.0 }.sum()

    var showAlertMessage by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier.padding(8.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Rewards Balance: $${"%.2f".format(totalAmount)}",
                style = TextStyle(
                    fontFamily = FontFamily(Font(R.font.montserrat_regular)),
                    fontSize = 25.sp,
                ),
                modifier = Modifier.padding(bottom = 12.dp)

            )

            // Description and Amount input fields Row
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Description TextField
                TextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text(text = stringResource(id = R.string.bp_description), style = TextStyle(
                        fontFamily = FontFamily(Font(R.font.montserrat_regular)),
                        fontSize = 16.sp,
                    )) },
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 8.dp),
                    singleLine = true
                )

                // Amount TextField
                TextField(
                    value = amount,
                    onValueChange = { newValue ->
                        if (containsOnlyNumbers(newValue)) {
                            // Only allow numeric input and limit to two decimal places
                            val newText =
                                newValue.takeIf { text -> text.matches(Regex("^\\d*\\.?\\d{0,2}$")) }
                                    ?: amount
                            amount = newText
                            showAlertMessage = false
                        } else {
                            showAlertMessage = true
                        }
                    },
                    label = { Text(stringResource(id = R.string.bp_amount), style = TextStyle(
                        fontFamily = FontFamily(Font(R.font.montserrat_regular)),
                        fontSize = 16.sp,
                    )) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 8.dp),
                    singleLine = true,
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Clear All and Add Buttons Row
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Clear All Button
                Button(
                    onClick = {
                        // Clear all added items
                        viewModel.RemoveAllRewardItems()
                    },
                    shape = Shapes.extraSmall,
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 8.dp)
                ) {
                    Text(stringResource(id = R.string.bp_button_clear_all), style = TextStyle(
                        fontFamily = FontFamily(Font(R.font.montserrat_regular)),
                        fontSize = 16.sp,
                    ))
                }

                // Add Button
                Button(
                    onClick = {
                        // Add the reward item to the list
                        if (description.isNotBlank() && amount.isNotBlank()) {
                            viewModel.AddRewardItem(RewardItem(description, amount))
                            // Clear fields after adding
                            description = ""
                            amount = ""
                        }
                    },
                    modifier = Modifier.weight(1f),
                    shape = Shapes.extraSmall
                ) {
                    Text(stringResource(id = R.string.bp_button_add), style = TextStyle(
                        fontFamily = FontFamily(Font(R.font.montserrat_regular)),
                        fontSize = 16.sp,
                    ))
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                NumericAlertMessage(showAlertMessage)
            }


            Column(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                uiState.rewardsList.map { it ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = it.description,
                            style = TextStyle(
                                fontFamily = FontFamily(Font(R.font.montserrat_regular)),
                                fontSize = 13.sp,
                            ),
                            modifier = Modifier
                                .weight(1f)
                                .padding(top = 8.dp, start = 16.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "$${"%.2f".format(it.amount.toDoubleOrNull() ?: 0.0)}",
                            style = TextStyle(
                                fontFamily = FontFamily(Font(R.font.montserrat_regular)),
                                fontSize = 13.sp,
                            ),
                            modifier = Modifier
                                .weight(0.5f)
                                .padding(top = 8.dp, end = 16.dp),

                        )
                        IconButton(
                            onClick = {
                                viewModel.RemoveRewardItem(it)
                            },
                            modifier = Modifier.size(24.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Delete"
                            )
                        }
                    }
                }
            }
        }
    }



}



@Composable
fun BudgetInformation(viewModel: AppViewModel) {
    val uiState by viewModel.uiState.collectAsState()

    var editing by remember { mutableStateOf(false) }
    var incomeText by remember { mutableStateOf(uiState.income.toString()) }

    var showAlertMessage by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        item {
            Card(
                modifier = Modifier.padding(8.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(text = stringResource(id = R.string.bp_monthly_income), style = TextStyle(
                        fontFamily = FontFamily(Font(R.font.montserrat_regular)),
                        fontSize = 25.sp,
                    ),)
                    if (editing) {
                        TextField(
                            value = incomeText,
                            onValueChange = { newValue ->
                                if (containsOnlyNumbers(newValue)) {
                                    incomeText = newValue
                                    val income = newValue.toFloatOrNull() ?: 0f
                                    viewModel.SetMonthlyIncome(income)
                                    showAlertMessage = false
                                } else {
                                    showAlertMessage = true
                                }
                            },
                            label = { Text(text = stringResource(id = R.string.bp_monthly_enter_income)) },
                            modifier = Modifier.padding(vertical = 4.dp)
                        )
                    } else {
                        Text(text = "$$incomeText", style = MaterialTheme.typography.bodyLarge)
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Button(
                            onClick = { editing = !editing },
                            shape = Shapes.extraSmall) {
                            Text(text = if (editing) stringResource(id = R.string.bp_button_done) else stringResource(id = R.string.bp_button_edit), style = TextStyle(
                                fontFamily = FontFamily(Font(R.font.montserrat_regular)),
                                fontSize = 16.sp,
                            ))

                        }
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        NumericAlertMessage(showAlertMessage)
                    }
                }
            }
        }

        item {
            RewardsInfo(viewModel)
        }

        item {
            MonthlyWeeklyBudget(viewModel, monthlyIncome = incomeText.toDoubleOrNull() ?: 0.0)
        }
        item{
            SpendingsCategories(viewModel)
        }
    }
}

@Preview
@Composable
fun PreviewBudget(){
    Column {
        BudgetInformation(AppViewModel())
        MonthlyWeeklyBudget(AppViewModel(), 3000.0)
        RewardsInfo(AppViewModel())
        SpendingsCategories(AppViewModel())
    }
}

