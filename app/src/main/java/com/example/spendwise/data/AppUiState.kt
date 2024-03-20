package com.example.spendwise.data

import com.example.spendwise.model.Spending

data class AppUiState(
    //Navbar icons state
    val selectedIconIndex: Int = 0,

    //Main page counter - test
    val counter: Int = 0,

    //Sample list to show items in "Spendings" screen - first list
    val breakDownListSample: List<Spending> = listOf(
        Spending("Groceries","Walmart", "Feb 18, 2024", 54F),
        Spending("Groceries","Costco", "Feb 12, 2024", 56.12F),
        Spending("Groceries","Safeway - Pharmacy", "Feb 12, 2024", 54.98F),
        Spending("Utilities","Enmax", "Feb 6, 2024", 114.99F),
        Spending("Utilities","Telus", "Feb 9, 2024", 69.5F),
        Spending("Entertainment","Skate park", "Feb 25, 2024", 75.30F)
    )
)
