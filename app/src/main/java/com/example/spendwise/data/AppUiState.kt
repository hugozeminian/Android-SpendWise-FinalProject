package com.example.spendwise.data

import com.example.spendwise.model.RewardItem
import com.example.spendwise.model.Spending
import com.example.spendwise.model.SpendingsCategories
import com.example.spendwise.model.User

data class AppUiState(

    //Navbar icons state
    val selectedIconIndex: Int = 0,

    //Sample list to show items in "Spendings" screen - first list
    val breakDownListSample: List<Spending> = listOf(
        Spending("Groceries","Walmart", "Feb 25, 2024", 100F),
        Spending("Groceries","Costco", "Mar 12, 2024", 50F),
        Spending("Groceries","Safeway - Pharmacy", "Feb 12, 2024", 50F),
        Spending("Utilities","Enmax", "Mar 6, 2024", 100F),
        Spending("Utilities","Telus", "Mar 9, 2024", 70F),
        Spending("Entertainment","Skate park", "Mar 25, 2024", 70F),
        Spending("Entertainment","Skate park", "Mar 26, 2024", 50F)
    ),

    //List of user's credentials
    val userListSample: List<User> = listOf(
        User ("Aurora Wang", "Aurora","aurorawang@gmail.com", "1234" ),
        User("User", "User", "user@gmail.com", "123"),
        User("John Doe", "john.doe", "john@gmail.com", "12"),
        User("H C", "hc", "h@c.com", "hc"),
    ),
    val loggedUser : User = User("","","",""),

    //Set if the user is successfully logged or not
    val isLogged: Boolean = false,

    //Dark mode settings
    val isDarkMode: Boolean = false,

    //Monthly income
    val income: Float = 2500F,

    //Monthly budget
    val monthlyBudget: Float = 2000F,
    val weeklyBudget: Float = 500F,
    val budgetAlert: Float = 90F,
    val savingsPercentage: Float = 0F,

    //Alert Limit
    val showAlert: Boolean = false,

    //Spendings Categories
    val spendingsCategoriesList: List<SpendingsCategories> = listOf(
        SpendingsCategories("Groceries", 100F),
        SpendingsCategories("Utilities", 300F),
        SpendingsCategories("Entertainment", 200F),
    ),

    //Selection of recap list (monthly or weekly)
    val spendingRecap: String = "Weekly",

    //Rewards list
    val rewardsBalance: Float = 0F,
    val rewardsList: List<RewardItem> = listOf(
        RewardItem("Reward-1", "10"),
        RewardItem("Reward-2", "20"),
        RewardItem("Reward-3", "30"),
    ),
)
