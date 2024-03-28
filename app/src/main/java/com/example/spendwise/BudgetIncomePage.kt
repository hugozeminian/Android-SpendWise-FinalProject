package com.example.spendwise


import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.spendwise.data.NumericAlertMessage
import com.example.spendwise.data.checkEmptyOrNullOrNegative
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
    var manipulatedWeeklyLimit by remember { mutableStateOf("") }

    // Maintain state for the list of spending categories
    val spendingCategories = uiState.spendingsCategoriesList

    // Manage scroll state
    val lazyListState = rememberLazyListState()

    var showAlertMessage by remember { mutableStateOf(false) }

    Card(){
        Column(
            modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_medium))
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
                    label = { Text(text = "Name",style = TextStyle(
                        fontFamily = FontFamily(Font(R.font.montserrat_regular)),
                        fontSize = 16.sp)) },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Next,
                        keyboardType = KeyboardType.Text
                    ),
                    modifier = Modifier.weight(1f)
                )

                Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.padding_medium)))

                TextField(                   
                    value = manipulatedWeeklyLimit,
                    onValueChange = { newValue ->
                        if (containsOnlyNumbers(newValue)) {
                            // Only allow numeric input and limit to two decimal places
                            val validatedText =
                                newValue.takeIf { text -> text.matches(Regex("^\\d*\\.?\\d{0,2}$")) }
                                    ?: manipulatedWeeklyLimit
                            manipulatedWeeklyLimit = validatedText
                            showAlertMessage = false
                        }else {
                            showAlertMessage = true
                        }
                    },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Done,
                        keyboardType = KeyboardType.Number
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            // Add the spending category to the list
                            if (categoryName.isNotBlank() && manipulatedWeeklyLimit.isNotBlank()) {
                                weeklyLimit = checkEmptyOrNullOrNegative(manipulatedWeeklyLimit)
                                val newCategory =
                                    SpendingsCategories(categoryName, weeklyLimit.toFloat())
                                    
                                viewModel.AddSpendingsCategoriesItem(newCategory)


                                // Clear fields after adding
                                categoryName = ""
                                weeklyLimit = ""
                                manipulatedWeeklyLimit = ""
                                showAlertMessage = false
                            }else{
                                showAlertMessage = true
                            }
                        }
                    ),
                    label = { Text(stringResource(id = R.string.bp_cat_weekly_limit),
                        style = TextStyle(
                        fontFamily = FontFamily(Font(R.font.montserrat_regular)),
                        fontSize = 16.sp)) },
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.padding_medium)))

            // Clear All and Add Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                // Clear All Button
                Button(
                    onClick = {
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

                Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.padding_medium)))

                // Add Button
                Button(
                    onClick = {
                        // Add the spending category to the list
                        if (categoryName.isNotBlank() && manipulatedWeeklyLimit.isNotBlank()) {
                            weeklyLimit = checkEmptyOrNullOrNegative(manipulatedWeeklyLimit)
                            val newCategory =
                                SpendingsCategories(categoryName, weeklyLimit.toFloat())
                            viewModel.AddSpendingsCategoriesItem(newCategory)


                            // Clear fields after adding
                            categoryName = ""
                            weeklyLimit = ""
                            manipulatedWeeklyLimit = ""
                            showAlertMessage = false
                        }else{
                            showAlertMessage = true
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

            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.padding_small)))

            // Display list of spendings categories.
            Column(
                modifier = Modifier
                    .fillMaxWidth() // Limit the height for scrolling
            ) {
                spendingCategories.map {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                    ) {
                        Text(
                            text = it.name,
                            style = TextStyle(
                                fontFamily = FontFamily(Font(R.font.montserrat_regular)),
                                fontSize = 13.sp,
                            ),
                            modifier = Modifier.weight(2f)
                        )
                        Text(
                            text = stringResource(id = R.string.bp_cat_weekly_limit) + it.weeklyLimit,
                            style = TextStyle(
                                fontFamily = FontFamily(Font(R.font.montserrat_regular)),
                                fontSize = 13.sp,
                            ),
                            modifier = Modifier.weight(1f)
                        )
                        Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.padding_small)))
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete",
                            modifier = Modifier.clickable {
                                viewModel.RemoveSpendingsCategoriesItem(it)
                            }
                        )
                    }
                    Divider( thickness = 1.dp,
                        color = Color.Black)
                }
            }
        }
    }
}

@Composable
fun MonthlyWeeklyBudget(viewModel: AppViewModel, monthlyIncome: Float) {
    val uiState by viewModel.uiState.collectAsState()

    var editing by remember { mutableStateOf(false) }
    var monthlyBudget by remember { mutableStateOf(uiState.monthlyBudget.toString()) }
    var weeklyBudget by remember { mutableStateOf(uiState.weeklyBudget.toString()) }
    var budgetAlert by remember { mutableStateOf(uiState.budgetAlert.toString()) }
    var showAlert by remember { mutableStateOf(uiState.showAlert) }

    var manipulatedMonthlyBudget by remember { mutableStateOf(uiState.monthlyBudget.toString()) }
    var manipulatedWeeklyBudget by remember { mutableStateOf(uiState.weeklyBudget.toString()) }
    var manipulatedBudgetAlert by remember { mutableStateOf(uiState.budgetAlert.toString()) }

    var showAlertMessage by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier.padding(bottom = dimensionResource(id = R.dimen.padding_medium))
    ) {
        Column(
            modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_medium))
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.bp_monthly_budget),
                    style = TextStyle(
                        fontFamily = FontFamily(Font(R.font.montserrat_regular)),
                        fontSize = 16.sp,
                    ),

                )

                if (editing) {
                        TextField(
                            value = manipulatedMonthlyBudget,
                            onValueChange = { newValue ->
                                if (containsOnlyNumbers(newValue)) {
                                    // Only allow numeric input and limit to two decimal places
                                    val validatedText =
                                        newValue.takeIf { text -> text.matches(Regex("^\\d*\\.?\\d{0,2}$")) }
                                            ?: manipulatedMonthlyBudget
                                    manipulatedMonthlyBudget = validatedText
                                    showAlertMessage = false
                                } else {
                                    showAlertMessage = true
                                }
                            },
                            keyboardOptions = KeyboardOptions.Default.copy(
                                imeAction = ImeAction.Next,
                                keyboardType = KeyboardType.Number
                            ),
                            label = { Text(text = "",) },
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
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.bp_weekly_budget),
                    style = TextStyle(
                        fontFamily = FontFamily(Font(R.font.montserrat_regular)),
                        fontSize = 16.sp,
                    ),

                )

                if (editing) {
                    TextField(
                        value = manipulatedWeeklyBudget,
                        onValueChange = { newValue ->
                            if (containsOnlyNumbers(newValue)) {
                                // Only allow numeric input and limit to two decimal places
                                val validatedText =
                                    newValue.takeIf { text -> text.matches(Regex("^\\d*\\.?\\d{0,2}$")) }
                                        ?: manipulatedWeeklyBudget
                                manipulatedWeeklyBudget = validatedText
                                showAlertMessage = false
                            }else {
                                showAlertMessage = true
                            }
                        },
                        keyboardOptions = KeyboardOptions.Default.copy(
                            imeAction = ImeAction.Next,
                            keyboardType = KeyboardType.Number
                        ),
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
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.bp_budget_alert),
                    style = TextStyle(
                        fontFamily = FontFamily(Font(R.font.montserrat_regular)),
                        fontSize = 16.sp,
                    ),

                )

                if (editing) {
                    TextField(
                        value = manipulatedBudgetAlert,
                        onValueChange = { newValue ->
                            if (containsOnlyNumbers(newValue)) {
                                // Only allow numeric input and limit to two decimal places
                                val validatedText =
                                    newValue.takeIf { text -> text.matches(Regex("^\\d*\\.?\\d{0,2}$")) }
                                        ?: manipulatedBudgetAlert
                                manipulatedBudgetAlert = validatedText
                                showAlertMessage = false
                            }else {
                                showAlertMessage = true
                            }
                        },
                        keyboardOptions = KeyboardOptions.Default.copy(
                            imeAction = ImeAction.Done,
                            keyboardType = KeyboardType.Number
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = {
                                if (editing) {
                                    monthlyBudget = checkEmptyOrNullOrNegative(manipulatedMonthlyBudget)
                                    weeklyBudget = checkEmptyOrNullOrNegative(manipulatedWeeklyBudget)
                                    budgetAlert = checkEmptyOrNullOrNegative(manipulatedBudgetAlert)

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
                            }
                        ),
                        label = { Text(text = "") },
                        modifier = Modifier.align(Alignment.CenterVertically)
                    )
                } else {
                    Text(
                        text = "$budgetAlert%",
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
                    modifier = Modifier.padding(vertical = dimensionResource(id = R.dimen.padding_small))
                )
            }

            Button(
                onClick = {//ensure user's budget does not exceed monthly income set
                    if (editing) {
                        monthlyBudget = checkEmptyOrNullOrNegative(manipulatedMonthlyBudget)
                        weeklyBudget = checkEmptyOrNullOrNegative(manipulatedWeeklyBudget)
                        budgetAlert = checkEmptyOrNullOrNegative(manipulatedBudgetAlert)

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
                    .padding(top = 20.dp)
                    .align(Alignment.CenterHorizontally)
            ) {
                Text(text = if (editing) stringResource(id = R.string.bp_button_save) else stringResource(id = R.string.bp_button_edit), style = TextStyle(
                    fontFamily = FontFamily(Font(R.font.montserrat_regular)),
                    fontSize = 16.sp,
                  )
                )
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
    var manipulatedAmountText by remember { mutableStateOf("") }

    // Calculate the total sum of amounts
    var totalAmount by remember { mutableDoubleStateOf(0.0) }
    totalAmount = uiState.rewardsList.sumOf { it.amount.toDoubleOrNull() ?: 0.0 }

    var showAlertMessage by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier.padding(bottom = dimensionResource(id = R.dimen.padding_medium))
    ) {
        Column(
            modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_medium))
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
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Next,
                        keyboardType = KeyboardType.Text
                    ),
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = dimensionResource(id = R.dimen.padding_small)),
                    singleLine = true
                )

                // Amount TextField
                TextField(
                    value = manipulatedAmountText,
                    onValueChange = { newValue ->
                            // Only allow numeric input and limit to two decimal places
                        val validatedText =
                            newValue.takeIf { text -> text.matches(Regex("^\\d*\\.?\\d{0,2}$")) }
                                ?: manipulatedAmountText
                        manipulatedAmountText = validatedText
                        showAlertMessage = false
                    },
                    label = { Text(stringResource(id = R.string.bp_amount), style = TextStyle(
                        fontFamily = FontFamily(Font(R.font.montserrat_regular)),
                        fontSize = 16.sp,
                    )) },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Done,
                        keyboardType = KeyboardType.Number
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            if (description.isNotBlank() && manipulatedAmountText.isNotBlank()) {
                                amount = checkEmptyOrNullOrNegative(manipulatedAmountText)
                                viewModel.AddRewardItem(RewardItem(description, amount))
                                // Clear fields after adding
                                description = ""
                                amount = ""
                                manipulatedAmountText = ""
                                showAlertMessage = false
                            }else{
                                showAlertMessage = true
                            }
                        }
                    ),
                    modifier = Modifier
                        .weight(1f),
                    singleLine = true,
                )
            }

            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.padding_small)))

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
                        .padding(end = dimensionResource(id = R.dimen.padding_small))
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
                        if (description.isNotBlank() && manipulatedAmountText.isNotBlank()) {
                            amount = checkEmptyOrNullOrNegative(manipulatedAmountText)
                            viewModel.AddRewardItem(RewardItem(description, amount))
                            // Clear fields after adding
                            description = ""
                            amount = ""
                            manipulatedAmountText = ""
                        }else{
                    showAlertMessage = true
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
                uiState.rewardsList.map {

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(40.dp)
                    ) {
                        Text(
                            text = it.description,
                            style = TextStyle(
                                fontFamily = FontFamily(Font(R.font.montserrat_regular)),
                                fontSize = 13.sp,
                            )
                        )
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.End
                        ) {
                            Text(
                                text = "$${"%.2f".format(it.amount.toDoubleOrNull() ?: 0.0)}",
                                style = TextStyle(
                                    fontFamily = FontFamily(Font(R.font.montserrat_regular)),
                                    fontSize = 13.sp,
                                )
                            )
                            Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.padding_small)))
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Delete",
                                modifier = Modifier.clickable {
                                    viewModel.RemoveRewardItem(it)
                                }
                            )
                        }
                    }
                    Divider(
                        thickness = 1.dp,
                        color = Color.Black
                    )
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
    var manipulatedIncomeText by remember { mutableStateOf(uiState.income.toString()) }

    var showAlertMessage by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(dimensionResource(id = R.dimen.padding_medium))
    ) {
        item {
            Card(
                modifier = Modifier.padding(bottom = dimensionResource(id = R.dimen.padding_medium))
            ) {
                Column(
                    modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_medium))
                ) {
                    Text(text = stringResource(id = R.string.bp_monthly_income), style = TextStyle(
                        fontFamily = FontFamily(Font(R.font.montserrat_regular)),
                        fontSize = 25.sp,
                    ),)
                    
                    if (editing) {
                        TextField(
                            value = manipulatedIncomeText,
                            onValueChange = { newValue ->
                                if (containsOnlyNumbers(newValue)) {
                                    // Only allow numeric input and limit to two decimal places
                                    val validatedText =
                                        newValue.takeIf { text -> text.matches(Regex("^\\d*\\.?\\d{0,2}$")) }
                                            ?: manipulatedIncomeText
                                    manipulatedIncomeText = validatedText
                                    showAlertMessage = false
                                }else {
                                    showAlertMessage = true
                                }
                            },
                            keyboardOptions = KeyboardOptions.Default.copy(
                                imeAction = ImeAction.Done,
                                keyboardType = KeyboardType.Number
                            ),
                            keyboardActions = KeyboardActions(
                                onDone = {
                                    editing = !editing
                                    incomeText = checkEmptyOrNullOrNegative(manipulatedIncomeText)
                                    viewModel.SetMonthlyIncome(incomeText.toFloat())
                                }
                            ),
                            label = { Text(text = stringResource(id = R.string.bp_monthly_enter_income)) },
                            modifier = Modifier.padding(vertical = 4.dp)
                        )
                    } else {
                        Text(text = "$$incomeText", style = TextStyle(
                            fontFamily = FontFamily(Font(R.font.montserrat_regular)),
                            fontSize = 16.sp,
                        ))
                    }
                    Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.padding_medium)))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Button(
                            onClick = {
                                editing = !editing
                                incomeText = checkEmptyOrNullOrNegative(manipulatedIncomeText)
                                viewModel.SetMonthlyIncome(incomeText.toFloat())
                            },
                            shape = Shapes.extraSmall
                        ) {
                            Text(text = if (editing) stringResource(id = R.string.bp_button_done) else stringResource(id = R.string.bp_button_edit), style = TextStyle(
                                fontFamily = FontFamily(Font(R.font.montserrat_regular)),
                                fontSize = 16.sp,
                                )
                            )
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
            MonthlyWeeklyBudget(viewModel, monthlyIncome = incomeText.toFloatOrNull() ?: 0.0F)
        }
        item {
            SpendingsCategories(viewModel)
        }
    }
}

@Preview
@Composable
fun PreviewBudget() {
    Column {
        BudgetInformation(AppViewModel())
        MonthlyWeeklyBudget(AppViewModel(), 3000.0F)
        RewardsInfo(AppViewModel())
        SpendingsCategories(AppViewModel())
    }
}

