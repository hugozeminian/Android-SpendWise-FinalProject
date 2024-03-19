package com.example.spendwise.data

import com.example.spendwise.model.Spending

data class AppUiState(
    //Navbar icons state
    val selectedIconIndex: Int = 0,

    //Main page counter - test
    val counter: Int = 0,

    //Dropwdown category list
    val list: List<String> = listOf("Add item 0", "Add item 1", "Add item 2"),
    val isExpanded: Boolean = false,
    var selectedText: String = list[0],

    //Dropwdown transaction list
    val transactionList: List<String> = listOf("Add item 0", "Add item 1", "Add item 2"),
    val transactionIsExpanded: Boolean = false,
    var transactionSelectedText: String = transactionList[0],

    //Dropwdown spending recap
    val recapList: List<String> = listOf("Weekly", "Monthly"),
    val recapIsExpanded: Boolean = false,
    var recapSelectedText: String = recapList[0],

    //Sample list to show items in "Spendings" screen - first list
    val breakDownListSample: List<Spending> = listOf(
        Spending("Walmart", "Feb 18, 2024", 54.98F),
        Spending("Walmart", "Feb 15, 2024", 34.55F),
        Spending("Costco", "Feb 12, 2024", 56.12F),
        Spending("Safeway - Pharmacy", "Feb 12, 2024", 54.98F),
    )
)
