package com.example.spendwise

import android.view.RoundedCorner
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.spendwise.data.AddTransactionDropdownMenu
import com.example.spendwise.data.AppUiState
import com.example.spendwise.data.CategoryDropdownMenu
import com.example.spendwise.data.SpendingDropdownMenu
import com.example.spendwise.model.CategoryWeekly
import com.example.spendwise.model.Spending
import com.example.spendwise.ui.theme.AppViewModel

@Composable
fun SpendingsScreen(
    viewModel: AppViewModel,
    modifier: Modifier = Modifier
){
    val uiState by viewModel.uiState.collectAsState()

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text("My Spendings",
            fontWeight = FontWeight.SemiBold,
            fontSize = 28.sp)
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround,
            modifier = Modifier.fillMaxWidth()
        ){
            Text("Select category:")
            CategoryDropdownMenu(viewModel)
        }

        BreakDownList(uiState)
        Spacer(modifier = Modifier.height(16.dp))

        AddTransactionCard(viewModel)
        Spacer(modifier = Modifier.height(16.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround,
            modifier = Modifier.fillMaxWidth()
        ){
            Text("Spending recap:")
            SpendingDropdownMenu(viewModel)
        }
        Spacer(modifier = Modifier.height(8.dp))
        SpendingRecapList()
    }
}


@Composable
fun BreakDownList(
    uiState: AppUiState,
    modifier: Modifier = Modifier
){
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        uiState.breakDownListSample.forEach(){
            ItemList(spending = it)
        }
    }
}

@Composable
fun ItemList(
    spending: Spending,
    modifier: Modifier = Modifier
){
    Column {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
        ) {
            Column(
                verticalArrangement = Arrangement.SpaceAround,
                modifier = Modifier.fillMaxHeight()
            ) {
                Text(spending.date)
                Text(spending.description)
            }
            Text(spending.amount.toString())
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(color = MaterialTheme.colorScheme.onBackground)
        )
    }
}

@Composable
fun AddTransactionCard(
    viewModel: AppViewModel,
    modifier: Modifier = Modifier
){
    
    var amount by remember {mutableStateOf("")}
    var date by remember {mutableStateOf("")}
    var description by remember {mutableStateOf("")}

    Card{
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ){
                TextField(
                    value = description,
                    placeholder = { Text("Description") },
                    onValueChange = { value -> description = value },
                    modifier = Modifier.weight(1F),
                )
                Spacer(modifier = Modifier.width(16.dp))
                TextField(
                    value = date,
                    placeholder = { Text("Date") },
                    onValueChange = { value -> date = value },
                    modifier = Modifier.weight(1F)
                )
            }

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                TextField(
                    value = amount,
                    placeholder = { Text("Amount") },
                    onValueChange = { value -> amount = value },
                    modifier = Modifier.width(100.dp),
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
                )
                AddTransactionDropdownMenu(viewModel)
            }

            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {viewModel.AddNewTransaction(Spending(description, date, amount.toFloat()))},
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("Add transaction")
            }
        }
    }
}

@Composable
fun SpendingRecapList(

){

    val list: List<CategoryWeekly> = listOf(
        CategoryWeekly("Groceries", 150.25F, 207F),
        CategoryWeekly("Entertainment", 250F, 207F),
        CategoryWeekly("Utilities", 197.56F, 207F)
    )

    Column(
    ) {
        list.forEach {
            SpendingRecapItem(category = it)
        }
    }
}

@Composable
fun SpendingRecapItem(
    category: CategoryWeekly
){

    val displaValue: String = if(category.spent < category.limit) "$${category.spent.toString()}"
                        else "($${category.spent - category.limit} over limit) - $${category.spent.toString()}"

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
    ){
        Row {
            Icon(
                imageVector = Icons.Filled.CheckCircle,
                contentDescription = ""
            )
            Text(category.description)
        }
        Column(){
            Text(displaValue,
                textAlign = TextAlign.End,
                color =
                if(category.spent > category.limit){
                    Color.Red }
                else{
                    MaterialTheme.colorScheme.primary
                },
                modifier = Modifier.fillMaxWidth())
            Text("Weekly limit: \$" + category.limit.toString(),
                textAlign = TextAlign.End,
                modifier = Modifier.fillMaxWidth())
        }
    }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(1.dp)
            .background(color = MaterialTheme.colorScheme.onBackground)
    )
}

@Preview
@Composable
fun PreviewSpendingsScreen(){
    SpendingsScreen(viewModel = AppViewModel())
}