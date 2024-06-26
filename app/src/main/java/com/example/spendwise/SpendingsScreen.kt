package com.example.spendwise

import android.app.DatePickerDialog
import android.content.Context
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.spendwise.data.CustomDropdownMenu
import com.example.spendwise.data.NumericAlertMessage
import com.example.spendwise.model.Spending
import com.example.spendwise.ui.theme.AppViewModel
import com.example.spendwise.ui.theme.Shapes
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.input.ImeAction
import com.example.spendwise.data.checkEmptyOrNullOrNegative


//Composable to set the screen layout
@Composable
fun SpendingsScreen(
    viewModel: AppViewModel,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()

    //Gets the spending list sorted
    val sortedList = viewModel.getSortedSpendingList(false, uiState)

    //Helper variable to tell the list which category to display
    var breakdownCategory by remember {
        mutableStateOf("")
    }

    //Helper variable to set categories that will be displayed in the dropdown menu
    var categories by remember {
        mutableStateOf(listOf(""))
    }

    //Helper variable to pass the spending recap list selection
    var monthOrWeek by remember {
        mutableStateOf("")
    }

    //Gets current categories list
    categories = viewModel.GetCategories()

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .padding(dimensionResource(id = R.dimen.padding_medium))
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            stringResource(id = R.string.spending_screen_title),
            style = TextStyle(
                fontFamily = FontFamily(Font(R.font.montserrat_regular)),
                fontSize = 25.sp,
            ),
        )
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround,
            modifier = Modifier.fillMaxWidth()
        ){
            Text(stringResource(id = R.string.select_category) ,   style = TextStyle(
                    fontFamily = FontFamily(Font(R.font.montserrat_regular)),
                fontSize = 16.sp,
            ),)

            breakdownCategory = CustomDropdownMenu(categories)

        }

        //Pass the item selected from menu to the breakdown list
        BreakDownList(breakdownCategory, sortedList, viewModel)

        Spacer(modifier = Modifier.height(40.dp))

        AddTransactionCard(viewModel)

        Spacer(modifier = Modifier.height(60.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround,
            modifier = Modifier.fillMaxWidth()
        ){
            Text(stringResource(id = R.string.spending_recap),
                 style = TextStyle(
                fontFamily = FontFamily(Font(R.font.montserrat_regular)),
                fontSize = 16.sp,
                )
            )
            monthOrWeek = CustomDropdownMenu(listOf("Weekly", "Monthly"))
            viewModel.SetSpendingRecap(monthOrWeek)
        }
        Spacer(modifier = Modifier.height(8.dp))
        SpendingRecapList(viewModel)
        Spacer(modifier = Modifier.height(8.dp))
    }
}

//Displays all spending in the category selected
@Composable
fun BreakDownList(
    category: String,
    sortedList: List<Spending>,
    viewModel: AppViewModel,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        sortedList.forEach(){
            if(it.category == category) ItemList(spending = it, viewModel)
        }
    }
}

//Composable to set the item format in the list
@Composable
fun ItemList(
    spending: Spending,
    viewModel: AppViewModel,
    modifier: Modifier = Modifier
) {
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
                Text(spending.date, style = TextStyle(
                    fontFamily = FontFamily(Font(R.font.montserrat_regular)),
                    fontSize = 16.sp,
                ))
                Text(spending.description, style = TextStyle(
                    fontFamily = FontFamily(Font(R.font.montserrat_regular)),
                    fontSize = 16.sp,
                ))
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("$${spending.amount}", style = TextStyle(
                fontFamily = FontFamily(Font(R.font.montserrat_regular)),
                fontSize = 16.sp,
                    )
                )
                Spacer(modifier = Modifier.width(8.dp))
                IconButton(
                    onClick = {
                        //Remove clicked object from the list
                        viewModel.DeleteSpending(spending)
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
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(color = MaterialTheme.colorScheme.onBackground)
        )
    }
}

//Composable to set the add transaction card
@Composable
fun AddTransactionCard(
    viewModel: AppViewModel,
    modifier: Modifier = Modifier
) {

    //Helper variables to set a new transaction to be added to the list
    var amount by remember { mutableStateOf("") }
    var manipulatedAmount by remember { mutableStateOf("") }
    var selectedFormattedDate by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("") }
    var showAlertMessage by remember { mutableStateOf(false) }
    var selectedDate by remember { mutableStateOf(Calendar.getInstance()) }

    //Gets the list of categories
    var categories = viewModel.GetCategories()

    Card {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(16.dp)
        ) {
            DateSelectionDialog(
                context = LocalContext.current,
                selectedDate = selectedDate,
                onFormattedDateChanged = { formattedDate ->
                    selectedFormattedDate = formattedDate
                }
            )

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                TextField(
                    value = description,
                    placeholder = { Text("Description", style = TextStyle(
                        fontFamily = FontFamily(Font(R.font.montserrat_regular)),
                        fontSize = 16.sp,
                    )) },
                    onValueChange = { value -> description = value },
                    modifier = Modifier.weight(1F),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Next,
                        keyboardType = KeyboardType.Text
                    ),
                )
            }

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                TextField(
                    value = manipulatedAmount,
                    placeholder = { Text("Amount", style = TextStyle(
                        fontFamily = FontFamily(Font(R.font.montserrat_regular)),
                        fontSize = 16.sp,
                    )) },
                    onValueChange = { newValue ->
                        // Only allow numeric input and limit to two decimal places
                        val validatedText =
                            newValue.takeIf { text -> text.matches(Regex("^\\d*\\.?\\d{0,2}$")) }
                                ?: manipulatedAmount
                        manipulatedAmount = validatedText
                        showAlertMessage = false
                    },
                    modifier = Modifier.width(100.dp),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Next,
                        keyboardType = KeyboardType.Number
                    ),

                    )
                category = CustomDropdownMenu(categories)
            }

            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    if (description.isNotBlank() && manipulatedAmount.isNotBlank()) {
                        amount = checkEmptyOrNullOrNegative(manipulatedAmount)
                        viewModel.AddNewTransaction(
                            Spending(category, description, selectedFormattedDate, amount.toFloat())
                        )
                        // Clear fields after adding
                        description = ""
                        amount = ""
                        manipulatedAmount = ""
                        showAlertMessage = false
                    } else {
                        showAlertMessage = true
                    }
                },
                shape = Shapes.extraSmall
            ) {
                Text("Add transaction", style = TextStyle(
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

//Composable to set the spendings recap list
@Composable
fun SpendingRecapList(
    viewModel: AppViewModel,
    modifier: Modifier = Modifier
) {

    val uiState by viewModel.uiState.collectAsState()

    //Gets both weekly and monthly lists sorted
    val filteredList = viewModel.FilterList(uiState.spendingRecap)

    Column(
    ) {
        filteredList.forEach { item ->
            //Gets the weekly limit for the category in the iteration
            val findCategoryLimit = uiState.spendingsCategoriesList.find{ it.name == item.key }
            if(findCategoryLimit != null){

                //Pass all parameters to spending recap item to be displayed
                SpendingRecapItem(
                    category = Pair(item.key, item.value), findCategoryLimit.weeklyLimit, viewModel)
            }
        }
    }
}

//Composable to define the item layout of spending recap list
@Composable
fun SpendingRecapItem(
    category: Pair<String, Float>,
    limit: Float,
    viewModel: AppViewModel,
    modifier: Modifier = Modifier
){

    val uiState by viewModel.uiState.collectAsState()

    //Sets the message displayed when a spending is over a weekly limit
    val displaValue: String =
            if(category.second < limit) "$${String.format("%.2f", category.second)}"
            else "($${String.format("%.2f", category.second - limit)} over limit) - $${String.format("%.2f", category.second)}"

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
    ) {
        Row {
            Text(category.first, style = TextStyle(
                fontFamily = FontFamily(Font(R.font.montserrat_regular)),
                fontSize = 16.sp,
            ))
        }
        Column(
            verticalArrangement = Arrangement.Center
        ){

            //If statement to define which list to display (weekly or monthly)
            if(uiState.spendingRecap == "Weekly"){
                Text(displaValue,
                    textAlign = TextAlign.End,
                    color =
                    if(category.second > limit){
                        Color.Red }
                    else{
                        MaterialTheme.colorScheme.primary
                    },
                     style = TextStyle(
                    fontFamily = FontFamily(Font(R.font.montserrat_regular)),
                    fontSize = 16.sp,
                    ),
                    modifier = Modifier.fillMaxWidth())
                Text("Weekly limit: \$" + limit.toString(),
                    textAlign = TextAlign.End,
                    modifier = Modifier.fillMaxWidth())
            }
            else if(uiState.spendingRecap == "Monthly"){
                Text("$${String.format("%.2f", category.second)}",
                    textAlign = TextAlign.End,
                    color = MaterialTheme.colorScheme.primary,
                     style = TextStyle(
                    fontFamily = FontFamily(Font(R.font.montserrat_regular)),
                    fontSize = 16.sp,
                    ),
                    modifier = Modifier.fillMaxWidth())
            }
        }
    }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(1.dp)
            .background(color = MaterialTheme.colorScheme.onBackground)
    )
}

//@Preview
//@Composable
//fun PreviewSpendingsScreen(){
//    SpendingsScreen(viewModel = AppViewModel())
//}

//Composable responsible for displaying a calendar
@Composable
fun DateSelectionDialog(
    context: Context,
    selectedDate: Calendar,
    onFormattedDateChanged: (String) -> Unit
) {
    var selectedDateState by remember { mutableStateOf(selectedDate) }
    var formattedDate by remember { mutableStateOf("") }

    // Function to update the formatted date and notify the parent component about the change
    fun updateFormattedDate() {
        formattedDate =
            SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(selectedDateState.time)
        onFormattedDateChanged(formattedDate)
    }

    updateFormattedDate()

    // Function to show the date picker dialog
    val showDatePicker = { date: Calendar ->
        val datePickerDialog = DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                val selectedCalendar = Calendar.getInstance().apply {
                    set(year, month, dayOfMonth)
                }
                selectedDateState = selectedCalendar
                updateFormattedDate()
            },
            date.get(Calendar.YEAR),
            date.get(Calendar.MONTH),
            date.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.show()
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth()
    ) {
        // Show the date picker dialog when clicked
        Button(
            onClick = { showDatePicker(selectedDateState) },
            shape = Shapes.extraSmall) {
            Text(text = "Select Date",  style = TextStyle(
                fontFamily = FontFamily(Font(R.font.montserrat_regular)),
                fontSize = 16.sp,
            ),)
        }

        // Display the selected date
        Text(
            text = formattedDate,
            style = TextStyle(
                fontFamily = FontFamily(Font(R.font.montserrat_regular)),
                fontSize = 16.sp,
            ),
            modifier = Modifier.padding(top = 8.dp, start = 8.dp)
        )
    }
}