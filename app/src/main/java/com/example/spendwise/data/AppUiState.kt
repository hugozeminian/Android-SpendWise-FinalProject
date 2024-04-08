package com.example.spendwise.data

import com.example.spendwise.model.RewardItem
import com.example.spendwise.model.Spending
import com.example.spendwise.model.SpendingsCategories
import com.example.spendwise.model.User
import com.example.spendwise.network.Response

data class AppUiState(
    //Navbar icons state
    val selectedIconIndex: Int = 0,

    //Sample list to show items in "Spendings" screen - first list
    val breakDownListSample: List<Spending> = listOf(),

    //List of user's credentials
    val userListSample: List<User> = listOf(),

    val loggedUser : User = User("","","",""),

    //Set if the user is successfully logged or not
    val isLogged: Boolean = true,

    //Dark mode settings
    val isDarkMode: Boolean = false,

    //Monthly income
    val income: Float = 0F,

    //Monthly budget
    val monthlyBudget: Float = 0F,
    val weeklyBudget: Float = 0F,
    val budgetAlert: Float = 0F,
    val savingsPercentage: Float = 0F,

    //Alert Limit
    val showAlert: Boolean = false,

    //Spendings Categories
    val spendingsCategoriesList: List<SpendingsCategories> = listOf(
        SpendingsCategories("Insert new category", 0F),
    ),

    //Selection of recap list (monthly or weekly)
    val spendingRecap: String = "Weekly",

    //Rewards list
    val rewardsBalance: Float = 0F,
    val rewardsList: List<RewardItem> = listOf(),

    val response: List<Response> = listOf()
)
