package com.example.spendwise


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
//import com.example.budgetincome.ui.theme.BudgetIncomeTheme

// Define a data class to represent a reward item
data class RewardItem(val description: String, val amount: String)
// Define a data class to represent a spending category
data class SpendingCategory(val name: String, val weeklyLimit: String)

// SpendingsCategories composable function

@Composable
fun SpendingsCategories() {
    // Maintain state for category name and weekly limit fields
    var categoryName by remember { mutableStateOf("") }
    var weeklyLimit by remember { mutableStateOf("") }

    // Maintain state for the list of spending categories
    var spendingCategories by remember { mutableStateOf(emptyList<SpendingCategory>()) }

    // Manage scroll state
    val lazyListState = rememberLazyListState()

    Surface(
        color = Color.LightGray,
        modifier = Modifier.padding(8.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Spendings Categories",
                style = MaterialTheme.typography.headlineMedium,
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
                    onValueChange = {
                        // Only allow numeric input and limit to two decimal places
                        val newText = it.takeIf { text -> text.matches(Regex("^\\d*\\.?\\d{0,2}$")) } ?: weeklyLimit
                        weeklyLimit = newText
                    },
                    label = { Text(text = "Weekly Limit") },
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
                    onClick = { spendingCategories = emptyList() },
                    modifier = Modifier.weight(1f)
                ) {
                    Text(text = "Clear All")
                }

                Spacer(modifier = Modifier.width(16.dp))

                // Add Button
                Button(
                    onClick = {
                        // Add the spending category to the list
                        if (categoryName.isNotBlank() && weeklyLimit.isNotBlank()) {
                            val formattedWeeklyLimit = "$${"%.2f".format(weeklyLimit.toDouble())}"
                            spendingCategories = listOf(SpendingCategory(categoryName, formattedWeeklyLimit)) + spendingCategories
                            // Clear fields after adding
                            categoryName = ""
                            weeklyLimit = ""
                        }
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text(text = "Add")
                }
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
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.weight(1f)
                        )
                        Text(
                            text = "Weekly Limit: ${category.weeklyLimit}",
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.weight(1f)
                        )
                        IconButton(
                            onClick = {
                                // Remove the spending category from the list
                                spendingCategories = spendingCategories.filterIndexed { i, _ -> i != index }
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
fun MonthlyWeeklyBudget(monthlyIncome: Double) {
    var editing by remember { mutableStateOf(false) }
    var monthlyBudget by remember { mutableStateOf("0.00") }
    var weeklyBudget by remember { mutableStateOf("0.00") }
    var showAlert by remember { mutableStateOf(false) }

    Surface(
        color = Color.LightGray,
        modifier = Modifier.padding(8.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Monthly Budget: ",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.weight(1f)
                )

                if (editing) {
                    TextField(
                        value = monthlyBudget,
                        onValueChange = { monthlyBudget = it },
                        label = { Text(text = "") },
                        modifier = Modifier.weight(1f)
                    )
                } else {
                    Text(
                        text = "$$monthlyBudget",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.weight(1.7f)
                    )
                }
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Weekly Budget: ",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.weight(1f)
                )

                if (editing) {
                    TextField(
                        value = weeklyBudget,
                        onValueChange = { weeklyBudget = it },
                        label = { Text(text = "") },
                        modifier = Modifier.weight(1f)
                    )
                } else {
                    Text(
                        text = "$$weeklyBudget",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.weight(1.7f)
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
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
            ) {
                Text(text = if (editing) "Save" else "Edit")
            }
        }
    }
}



@Composable
fun RewardsInfo() {
    // Maintain state for description and amount fields
    var description by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }

    // Maintain state for the list of rewards
    var rewards by remember { mutableStateOf(emptyList<RewardItem>()) }

    // Calculate the total sum of amounts
    var totalAmount by remember { mutableStateOf(0.0) }
    totalAmount = rewards.map { it.amount.toDoubleOrNull() ?: 0.0 }.sum()

    // Manage scroll state
    val lazyListState = rememberLazyListState()
    Surface(
        color = Color.LightGray,
        modifier = Modifier.padding(8.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Rewards Balance: $${"%.2f".format(totalAmount)}",
                style = MaterialTheme.typography.headlineSmall,
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
                    label = { Text(text = "Description") },
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 8.dp),
                    singleLine = true
                )

                // Amount TextField
                TextField(
                    value = amount,
                    onValueChange = {
                        // Only allow numeric input and limit to two decimal places
                        val newText = it.takeIf { text -> text.matches(Regex("^\\d*\\.?\\d{0,2}$")) } ?: amount
                        amount = newText
                    },
                    label = { Text(text = "Amount") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 8.dp),
                    singleLine = true,
                    placeholder = { Text(text = "Amount") } // Placeholder for the amount field
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
                        rewards = emptyList()
                    },
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 8.dp)
                ) {
                    Text(text = "Clear All")
                }

                // Add Button
                Button(
                    onClick = {
                        // Add the reward item to the list
                        if (description.isNotBlank() && amount.isNotBlank()) {
                            rewards = listOf(RewardItem(description, amount)) + rewards
                            // Clear fields after adding
                            description = ""
                            amount = ""
                        }
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text(text = "Add")
                }
            }

            // Display list of rewards in a LazyColumn, allow for scrolling and only show first few rewards before needing to scroll
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp),
                state = lazyListState
            ) {
                items(rewards.size) { index ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = rewards[index].description,
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier
                                .weight(1f)
                                .padding(top = 8.dp, start = 16.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "$${"%.2f".format(rewards[index].amount.toDoubleOrNull() ?: 0.0)}",
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier
                                .weight(0.5f)
                                .padding(top = 8.dp, end = 16.dp)
                        )
                        IconButton(
                            onClick = {
                                // Remove the reward item from the list
                                rewards = rewards.filterIndexed { i, _ -> i != index }
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
fun BudgetInformation() {
    var editing by remember { mutableStateOf(false) }
    var incomeText by remember { mutableStateOf("0.00") }
    var savingsPercentage by remember { mutableStateOf(0) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        item {
            Surface(
                color = Color.LightGray,
                modifier = Modifier.padding(8.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(text = "Monthly Income", style = MaterialTheme.typography.headlineSmall)
                    if (editing) {
                        TextField(
                            value = incomeText,
                            onValueChange = { incomeText = it },
                            label = { Text(text = "Enter Monthly Income") },
                            modifier = Modifier.padding(vertical = 4.dp)
                        )
                    } else {
                        Text(text = "$$incomeText", style = MaterialTheme.typography.bodyLarge)
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = "Monthly Savings Goal", style = MaterialTheme.typography.headlineSmall)
                    if (editing) {
                        TextField(
                            value = savingsPercentage.toString(),
                            onValueChange = { savingsPercentage = it.toIntOrNull() ?: 0 },
                            label = { Text(text = "Enter Monthly Savings Goal (%)") },
                            modifier = Modifier.padding(vertical = 4.dp)
                        )
                    } else {
                        Text(text = "$savingsPercentage%", style = MaterialTheme.typography.bodyLarge)
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Button(onClick = { editing = !editing }) {
                            Text(text = if (editing) "Done" else "Edit")
                        }
                    }
                }
            }
        }

        item {
            RewardsInfo()
        }

        item {
            MonthlyWeeklyBudget(monthlyIncome = incomeText.toDoubleOrNull() ?: 0.0)
        }
        item{
            SpendingsCategories()
        }
    }
}



//@Preview(showBackground = true)
//@Composable
//fun BudgetInformationPreview() {
//    BudgetIncomeTheme {
//        BudgetInformation()
//    }
//}

